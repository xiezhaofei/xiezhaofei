package com.example.xunhuandemo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.adsdemo.R;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyMainActivity extends FragmentActivity {

	MyfragmentAdapter<PictureInfo> mAdapter;
	ViewPager mPager;

	TextView title;
	TextView text;
	
	int pageIndex = 1;
	boolean isTaskRun;
	List<PictureInfo> datas;
	LinearLayout llWelcomeDot;
	ImageView[] dots;
	private ScheduledExecutorService scheduledExecutorService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initData();
		/* 标题 */
		title = (TextView) findViewById(R.id.title);
		title.setText("广告条");
		// 设置ViewPager
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new MyfragmentAdapter<PictureInfo>(getSupportFragmentManager(),datas);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			/* 更新手动滑动时的位置 */
			@Override
			public void onPageSelected(int position) {
				pageIndex = position;
				Log.d("dddd", "-----:"+position);
				setCurrentDot();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			/* state: 0空闲，1是滑行中，2加载完毕 */
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				System.out.println("state:" + state);
				if (state == 0 && !isTaskRun) {
					setCurrentItem();
					startTask();
				} else if (state == 1 && isTaskRun)
					stopTask();
			}
		});

		/* 其他内容 */
		text = (TextView) findViewById(R.id.text);
		text.setText("这里是你自己的内容");
		
		initDots();
	}
	
	/**
     * 底部圆点初始化
     */
    private void initDots()
    {
    	if(datas==null){
    		throw new RuntimeException("you should init datas parameter");
    	}
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.llyt_welcome_dot);

        dots = new ImageView[datas.size()];

        for (int i = 0; i < dots.length; i++)
        {
            dots[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
            		(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            dots[i].setLayoutParams(params);
            /*if(dotImg==null){
            	dotImg = BitmapFactory.decodeResource(getResources(), R.drawable.welcome_dot);
            }
            dots[i].setBackgroundDrawable(new BitmapDrawable(getResources(), dotImg));*/
            dots[i].setBackgroundResource(R.drawable.welcome_dot);
            // 设置为灰色
            dots[i].setEnabled(true);
           // dots[i].setTag(i);
            mLinearLayout.addView(dots[i]);
        }
    }
    
    private void setCurrentDot(){
    	if(pageIndex == 0){
			setCurrentDot(datas.size()-1);
		}else if(pageIndex == datas.size()+1){
			setCurrentDot(0);
		}else{
			setCurrentDot(pageIndex-1);
		}
    }
    
    /**
     * 设置底部圆点的高亮
     * @param position
     *            将要显示的view的位置
     */
    private void setCurrentDot(int position)
    {	
    	
    	Log.d("dddd", "dot index-----:"+position);
        if (position < 0 || position > dots.length - 1)
        {
            return;
        }
        dots[position].setEnabled(false);
        for(int i=0;i<dots.length;i++){
        	if(i!=position){
        		dots[i].setEnabled(true);
        	}
        }
        
    }

    
    
	private void initData(){
		if(datas==null){
			datas = new ArrayList<PictureInfo>();
		}
		datas.clear();
		PictureInfo info0= new PictureInfo();
		info0.setImgResource(R.drawable.item1);
		datas.add(info0);
		PictureInfo info1= new PictureInfo();
		info1.setImgResource(R.drawable.item2);
		datas.add(info1);
		PictureInfo info2= new PictureInfo();
		info2.setImgResource(R.drawable.item3);
		datas.add(info2);
		PictureInfo info3= new PictureInfo();
		info3.setImgResource(R.drawable.item4);
		datas.add(info3);
	}
	
	/**
	 * 开启定时任务
	 */
	private void startTask() {
		// TODO Auto-generated method stub
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 2, 3,
					TimeUnit.SECONDS);
			
		isTaskRun = true;
	}
	
	
	
		private class ScrollTask implements Runnable {

			public void run() {
				synchronized (mPager) {
					pageIndex++;
					mHandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
				}
			}

		}

		
		
	// 处理EmptyMessage(0)
	Handler mHandler = new MyHandler(this);

	/**
	 * 处理Page的切换逻辑
	 */
	private void setCurrentItem() {
		if (pageIndex == 0) {
			pageIndex = datas.size();
		} else if (pageIndex == datas.size()+1) {
			pageIndex = 1;
		}
		mPager.setCurrentItem(pageIndex, false);// 取消动画s
	}

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		scheduledExecutorService.shutdownNow();
	}

	public void onResume() {
		super.onResume();
		setCurrentItem();
		startTask();
	}
    
	@Override
	public void onPause() {
		super.onPause();
		stopTask();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	public static class MyHandler extends Handler {
		private final WeakReference<MyMainActivity> myactivity;

		public MyHandler(MyMainActivity activity) {
			myactivity = new WeakReference<MyMainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (myactivity == null) {
				return;
			}
			MyMainActivity thisactivity = myactivity.get();
			thisactivity.setCurrentItem();// 切换当前显示的图片
		}
	}
	
}
