package com.cum.chat.acmap.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.baidu.mapapi.map.MapView;
import com.cum.chat.acmap.R;
import com.cum.chat.acmap.trackshow.TrackApplication;
import com.cum.chat.acmap.trackshow.TrackQueryFragment;
import com.cum.chat.acmap.trackshow.TrackUploadFragment;

/**
 * Created by 2-1Ping on 2017/2/15.
 */

public class MapFragment extends Fragment implements View.OnClickListener{

    private TrackApplication trackApp = null;

    private Button btnTrackUpload;

    private Button btnTrackQuery;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    private TrackUploadFragment mTrackUploadFragment;

    private TrackQueryFragment mTrackQueryFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main,
                container, false);
        trackApp = (TrackApplication) getContext();
        // 初始化组件
        initComponent();


        return view;
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 设置默认的Fragment
        setDefaultFragment();
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        // 初始化控件
        btnTrackUpload = (Button) getActivity().findViewById(R.id.btn_trackUpload);
        btnTrackQuery = (Button) getActivity().findViewById(R.id.btn_trackQuery);

        btnTrackUpload.setOnClickListener(this);
        btnTrackQuery.setOnClickListener(this);

        fragmentManager = getChildFragmentManager();

        trackApp.initBmap((MapView)getActivity(). findViewById(R.id.bmapView));

    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {
        handlerButtonClick(R.id.btn_trackUpload);
    }

    /**
     * 点击事件
     */
    public void onClick(View v) {
        // TODO Auto-generated method stub
        handlerButtonClick(v.getId());
    }

    /**
     * 处理tab点击事件
     *
     * @param id
     */
    private void handlerButtonClick(int id) {
        // 重置button状态
        onResetButton();
        // 开启Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏Fragment
        hideFragments(transaction);

        switch (id) {

            case R.id.btn_trackQuery:

                TrackUploadFragment.isInUploadFragment1 = false;

                if (mTrackQueryFragment == null) {
                    mTrackQueryFragment = TrackQueryFragment.newInstance(trackApp);
                    transaction.add(R.id.fragment_content, mTrackQueryFragment);
                } else {
                    transaction.show(mTrackQueryFragment);
                }
                if (null != mTrackUploadFragment) {
                    mTrackUploadFragment.startRefreshThread1(false);
                }
                mTrackQueryFragment.addMarker1();
                btnTrackQuery.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                btnTrackQuery.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                trackApp.getmBaiduMap().setOnMapClickListener(null);
                break;

            case R.id.btn_trackUpload:

                TrackUploadFragment.isInUploadFragment1 = true;

                if (mTrackUploadFragment == null) {
                    mTrackUploadFragment = TrackUploadFragment.newInstance(trackApp);
                    transaction.add(R.id.fragment_content, mTrackUploadFragment);
                } else {
                    transaction.show(mTrackUploadFragment);
                }

                mTrackUploadFragment.startRefreshThread1(true);
                mTrackUploadFragment.addMarker1();
                if (null != mTrackUploadFragment.getGeoFence()) {
                    mTrackUploadFragment.getGeoFence().addMarker1();
                }
                btnTrackUpload.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
                btnTrackUpload.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
                trackApp.getmBaiduMap().setOnMapClickListener(null);
                break;
        }
        // 事务提交
        transaction.commit();

    }

    /**
     * 重置button状态
     */
    private void onResetButton() {
        btnTrackQuery.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        btnTrackQuery.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
        btnTrackUpload.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        btnTrackUpload.setBackgroundColor(Color.rgb(0xFF, 0xFF, 0xFF));
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (mTrackQueryFragment != null) {
            transaction.hide(mTrackQueryFragment);
        }
        if (mTrackUploadFragment != null) {
            transaction.hide(mTrackUploadFragment);
        }
        // 清空地图覆盖物
        trackApp.getmBaiduMap().clear();
    }

    @Override
    public void onResume() {
        trackApp.getBmapView().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        trackApp.getBmapView().onPause();
        TrackUploadFragment.isInUploadFragment1 = false;
    }

    @Override
   public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        trackApp.getClient().onDestroy();
        trackApp.getBmapView().onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 获取设备IMEI码
     *
     * @param context
     * @return
     */
    protected static String getImei(Context context) {
        String mImei = "NULL";
        try {
            mImei = ((TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            System.out.println("获取IMEI码失败");
            mImei = "NULL";
        }
        return mImei;
    }
}
