package com.effective.android.geo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.effective.android.geo.location.LocationActivity;
import com.effective.android.geo.service.LocationService;


/***
 * 展示定位sdk配置的示例，配置选项后调用的实际上是locationActivity的定位功能，但是覆盖了新的配置项
 * 注意：有些选项存在缓存的原因，所有在选中后再取消依然会在定位结果中显示出来
 * @author baidu
 *
 */
public class LocationOption extends Activity {
	private RadioGroup selectLocMode,selectcoord;
	private EditText scanSpan;
	private CheckBox geolocation,poi,describe,director;
	private LocationClientOption option;
	private Button startLoc;
	private LocationService locService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationconfig);
		selectLocMode = (RadioGroup)findViewById(R.id.selectMode);
		selectcoord = (RadioGroup)findViewById(R.id.selectCoordinates);
		scanSpan = (EditText)findViewById(R.id.frequence);
		geolocation = (CheckBox)findViewById(R.id.geolocation);
		poi = (CheckBox)findViewById(R.id.poiCheckBox);
		describe = (CheckBox)findViewById(R.id.Describe);
		director = (CheckBox)findViewById(R.id.Director);
		startLoc = (Button)findViewById(R.id.start);
		locService =  ((GeoApplication)getApplication()).getLocationService();
		option = new LocationClientOption();
		locService.stop();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startLoc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (selectLocMode.getCheckedRadioButtonId()) {
				case R.id.radio_hight:
					option.setLocationMode(LocationMode.Hight_Accuracy);
					break;
				case R.id.radio_low:
					option.setLocationMode(LocationMode.Battery_Saving);
					break;
				case R.id.radio_device:
					option.setLocationMode(LocationMode.Device_Sensors);
					break;
				default:
					break;
				}
				switch (selectcoord.getCheckedRadioButtonId()) {
				case R.id.radio_gcj02:
					option.setCoorType(Utils.CoorType_GCJ02);
					break;
				case R.id.radio_bd09ll:
					option.setCoorType(Utils.CoorType_BD09LL);
					break;
				case R.id.radio_bd09:
					option.setCoorType(Utils.CoorType_BD09MC);
					break;
				default:
					break;
				}
				try {
					int frequence = Integer.parseInt(scanSpan.getText().toString());
					option.setScanSpan(frequence);
				} catch (Exception e) {
					// TODO: handle exception
					option.setScanSpan(3000);
				}
				/**
				 * 地理位置信息
				 */
				if(geolocation.isChecked())
					option.setIsNeedAddress(true);
				else
					option.setIsNeedAddress(false);
				/**
				 * 周边poi列表
				 */
				if(poi.isChecked())
					option.setIsNeedLocationPoiList(true);
				else
					option.setIsNeedLocationPoiList(false);
				/**
				 * 位置语意化
				 */
				if(describe.isChecked())
					option.setIsNeedLocationDescribe(true);
				else
					option.setIsNeedLocationDescribe(false);
				/**
				 * 方向
				 */
				if(director.isChecked())
					option.setNeedDeviceDirect(true);
				else
					option.setNeedDeviceDirect(false);
				
				/**
				 * 设置前需停止定位服务，设置后重启定位服务才可以生效
				 */
				locService.setLocationOption(option);
				
				Intent locIntent = new Intent(LocationOption.this, LocationActivity.class);
				locIntent.putExtra("from", 1);
				LocationOption.this.startActivity(locIntent);
			}
			
		});
	}
	
}
