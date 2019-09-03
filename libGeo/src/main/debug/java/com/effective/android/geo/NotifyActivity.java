package com.effective.android.geo;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

/***
 * 实例演示位置提醒功能，由于定位本身具有空间性，所以设置当前点为提醒点，用户可以通过
 * 坐标拾取系统来自定义提醒点 http://api.map.baidu.com/lbsapi/getpoint/index.html
 * @author baidu
 *
 */
public class NotifyActivity extends Activity {

    private Button startNotify;
    private Vibrator mVibrator;
    private LocationClient mLocationClient;
    private NotiftLocationListener listener;
    private double longtitude,latitude;
    private NotifyLister mNotifyLister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);
        listener = new NotiftLocationListener();
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        startNotify = (Button)findViewById(R.id.notifystart);
        mLocationClient  = new LocationClient(this);
        mLocationClient.registerLocationListener(listener);
        mNotifyLister = new NotifyLister();
        mLocationClient.registerNotify(mNotifyLister);
        startNotify.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(startNotify.getText().toString().equals("开启位置提醒")){
                    mLocationClient.start();
                    startNotify.setText("关闭位置提醒");
                }else{
                    if(mNotifyLister!=null){
                        mLocationClient.removeNotifyEvent(mNotifyLister);
                        startNotify.setText("开启位置提醒");
                    }
                }

            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mLocationClient.removeNotifyEvent(mNotifyLister);
        mLocationClient.unRegisterLocationListener(listener);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.removeNotifyEvent(mNotifyLister);
        mLocationClient.unRegisterLocationListener(listener);
        mLocationClient.stop();
    }

    private Handler notifyHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            mNotifyLister.SetNotifyLocation(latitude,longtitude, 3000,mLocationClient.getLocOption().getCoorType());//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
        }

    };
    public class NotiftLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            longtitude = location.getLongitude();
            latitude = location.getLatitude();
            notifyHandler.sendEmptyMessage(0);
        }

    }
    public class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance){
            mVibrator.vibrate(1000);//振动提醒已到设定位置附近
            Toast.makeText(NotifyActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
        }
    }
}
