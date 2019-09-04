package com.effective.android.geo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.effective.android.geo.service.LocationService;

import java.util.LinkedList;

/***
 * 定位滤波demo，实际定位场景中，可能会存在很多的位置抖动，此示例展示了一种对定位结果进行的平滑优化处理
 * 实际测试下，该平滑策略在市区步行场景下，有明显平滑效果，有效减少了部分抖动，开放算法逻辑，希望能够对开发者提供帮助
 * 注意：该示例场景仅用于对定位结果优化处理的演示，里边相关的策略或算法并不一定适用于您的使用场景，请注意！！！
 * 
 * @author baidu
 * 
 */
public class LocationFilter extends Activity {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private Button reset;
	private LocationService locService;
	private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationfilter);
		mMapView = (MapView) findViewById(R.id.bmapView);
		reset = (Button) findViewById(R.id.clear);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));
		locService = ((GeoApplication) getApplication()).getLocationService();
		LocationClientOption mOption = locService.getDefaultLocationClientOption();
		mOption.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
		mOption.setCoorType("bd09ll");
		locService.setLocationOption(mOption);
		locService.registerListener(listener);
		locService.start();
	}

	/***
	 * 定位结果回调，在此方法中处理定位结果
	 */
	BDAbstractLocationListener listener = new BDAbstractLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub

			if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
				Message locMsg = locHander.obtainMessage();
				Bundle locData;
				locData = Algorithm(location);
				if (locData != null) {
					locData.putParcelable("loc", location);
					locMsg.setData(locData);
					locHander.sendMessage(locMsg);
				}
			}
		}

	};

	/***
	 * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
	 * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
	 * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
	 *
	 * @param location
	 * @return Bundle
	 */
	private Bundle Algorithm(BDLocation location) {
		Bundle locData = new Bundle();
		double curSpeed = 0;
		if (locationList.isEmpty() || locationList.size() < 2) {
			LocationEntity temp = new LocationEntity();
			temp.location = location;
			temp.time = System.currentTimeMillis();
			locData.putInt("iscalculate", 0);
			locationList.add(temp);
		} else {
			if (locationList.size() > 5)
				locationList.removeFirst();
			double score = 0;
			for (int i = 0; i < locationList.size(); ++i) {
				LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
						locationList.get(i).location.getLongitude());
				LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
				double distance = DistanceUtil.getDistance(lastPoint, curPoint);
				curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
				score += curSpeed * Utils.EARTH_WEIGHT[i];
			}
			if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
				location.setLongitude(
						(locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
								/ 2);
				location.setLatitude(
						(locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
								/ 2);
				locData.putInt("iscalculate", 1);
			} else {
				locData.putInt("iscalculate", 0);
			}
			LocationEntity newLocation = new LocationEntity();
			newLocation.location = location;
			newLocation.time = System.currentTimeMillis();
			locationList.add(newLocation);

		}
		return locData;
	}

	/***
	 * 接收定位结果消息，并显示在地图上
	 */
	private Handler locHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				BDLocation location = msg.getData().getParcelable("loc");
				int iscal = msg.getData().getInt("iscalculate");
				if (location != null) {
					LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
					// 构建Marker图标
					BitmapDescriptor bitmap = null;
					if (iscal == 0) {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_openmap_mark); // 非推算结果
					} else {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_openmap_focuse_mark); // 推算结果
					}

					// 构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
					// 在地图上添加Marker，并显示
					mBaiduMap.addOverlay(option);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		locService.unregisterListener(listener);
		locService.stop();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBaiduMap != null)
					mBaiduMap.clear();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();

	}

	/**
	 * 封装定位结果和时间的实体类
	 * 
	 * @author baidu
	 *
	 */
	class LocationEntity {
		BDLocation location;
		long time;
	}
}
