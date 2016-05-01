package com.example.adsdemo;

import java.util.Timer;
import java.util.TimerTask;

import com.example.adsdemo.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	FragmentAdapter mAdapter;
	ViewPager mPager;

	TextView title;
	TextView text;
	
	Timer mTimer;
	TimerTask mTask;
	int pageIndex = 1;
	boolean isTaskRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("AdsDemo by QQ472950043");

		/* 标题 */
		title = (TextView) findViewById(R.id.title);

		// 设置ViewPager
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new FragmentAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			/* 更新手动滑动时的位置 */
			@Override
			public void onPageSelected(int position) {
				pageIndex = position;
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
					//startTask();
				} else if (state == 1 && isTaskRun)
					stopTask();
			}
		});

		/* 其他内容 */
		text = (TextView) findViewById(R.id.text);
		text.setText("这里是你自己的内容");
	}
	
	/**
	 * 开启定时任务
	 */
	private void startTask() {
		// TODO Auto-generated method stub
		isTaskRun = true;
		mTimer = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
				pageIndex++;
				mHandler.sendEmptyMessage(0);
			}
		};
		mTimer.schedule(mTask, 2 * 1000, 2 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
	}

	// 处理EmptyMessage(0)
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setCurrentItem();
		}
	};

	/**
	 * 处理Page的切换逻辑
	 */
	private void setCurrentItem() {
		if (pageIndex == 0) {
			pageIndex = 2;
		} else if (pageIndex == 3) {
			pageIndex = 1;
		}
		mPager.setCurrentItem(pageIndex, false);// 取消动画
	}

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		mTimer.cancel();
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
}
