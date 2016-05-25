package biz.home.api;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import biz.home.R;
import biz.home.SpeakToitActivity;
import biz.home.bean.MagicInfo;
import biz.home.bean.MagicMapLocation;
import biz.home.bean.MagicResult;
import biz.home.model.UserSetting;
import biz.home.util.MagicUserInfoDao;
import biz.home.util.UserSettingDao;

/**
 * Created by admin on 2015/8/27.
 * 该类专门用于定位，是一个异步任务
 */
public class MapLocation extends AsyncTask implements
        AMapLocationListener {
    private UserSettingDao dao;
    private MagicUserInfoDao dao2;
    private static final String TAG = "MapLocation";
    private LocationManagerProxy mLocationManagerProxy;
    private Random mRandom=new Random();
    Activity activity;
    public static MagicMapLocation location;
    Context context;

    public MapLocation(Activity activity,Context context) {
        super();
        this.activity=activity;
        this.context=context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            location=new MagicMapLocation();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
//        Toast.makeText(context,location,Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPostExecute location"+location);
        super.onPostExecute(o);
    }

    /**
     * 初始化定位
     */
    private void init() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(activity);
        mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            Log.i(TAG, "onLocationChanged amapLocation:"+amapLocation);
            if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                //设置经度
                Log.i(TAG, "onLocationChanged amapLocation.getLatitude():" + amapLocation.getLatitude());
                location.setLatitude(amapLocation.getLatitude());
                location.setLongitude(amapLocation.getLongitude());
                location.setAddress(amapLocation.getAddress());
                location.setCountry(amapLocation.getCountry());
                location.setProvince(amapLocation.getProvince());
                location.setCity(amapLocation.getCity());
                location.setCityCode(amapLocation.getCityCode());
                location.setDistrict(amapLocation.getDistrict());
                location.setRoad(amapLocation.getRoad());
                location.setStreet(amapLocation.getStreet());
                location.setPoiName(amapLocation.getPoiName());
                location.setCountry(amapLocation.getCountry());
                location.setAdCode(amapLocation.getAdCode());
                location.setAddress(amapLocation.getAddress());
    //            magicInfo.setLatitude(amapLocation.getLatitude() + "");
    //            magicInfo.setLongitude(amapLocation.getLongitude() + "");
    ////            magicInfo.setLocation(amapLocation.getAddress());
    //            magicInfo.setLocation(amapLocation.getCity() + " " + amapLocation.getDistrict());
                Log.i(TAG, "onLocationChanged getAddress:" + amapLocation.getAddress());
                Log.i(TAG, "onLocationChanged getAdCode:"+amapLocation.getAdCode());
                Log.i(TAG, "onLocationChanged getCity:"+amapLocation.getCity());
                Log.i(TAG, "onLocationChanged getCityCode:"+amapLocation.getCityCode());
                Log.i(TAG, "onLocationChanged getCountry:"+amapLocation.getCountry());
                Log.i(TAG, "onLocationChanged getDistrict:"+amapLocation.getDistrict());
                Log.i(TAG, "onLocationChanged getFloor:"+amapLocation.getFloor());
                Log.i(TAG, "onLocationChanged getPoiId:"+amapLocation.getPoiId());
                Log.i(TAG, "onLocationChanged getPoiName:"+amapLocation.getPoiName());
                Log.i(TAG, "onLocationChanged getProvince:"+amapLocation.getProvince());
                Log.i(TAG, "onLocationChanged getRoad:"+amapLocation.getRoad());
                Log.i(TAG, "onLocationChanged getStreet:"+amapLocation.getStreet());
                Log.i(TAG, "onLocationChanged getAccuracy:"+amapLocation.getAccuracy());

                Log.i(TAG, "onLocationChanged getAddress:"+amapLocation.getAddress());




                /*
                mLocationLatlngTextView.setText(amapLocation.getLatitude() + "  "
                        + amapLocation.getLongitude());
                mLocationAccurancyTextView.setText(String.valueOf(amapLocation
                        .getAccuracy()));
                mLocationMethodTextView.setText(amapLocation.getProvider());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());

                mLocationTimeTextView.setText(df.format(date));

                mLocationDesTextView.setText(amapLocation.getAddress());
                if (amapLocation.getProvince() == null) {
                    mLocationProvinceTextView.setText("null");
                } else {
                    mLocationProvinceTextView.setText(amapLocation.getProvince());
                }
                mLocationCityTextView.setText(amapLocation.getCity());
                mLocationCountyTextView.setText(amapLocation.getDistrict());
                mLocationCityCodeTextView.setText(amapLocation.getCityCode());
                mLocationAreaCodeTextView.setText(amapLocation.getAdCode());
                */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged location:"+location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
