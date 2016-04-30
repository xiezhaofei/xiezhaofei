/**
 * 
 * @original author xiaanming
 * update by wangyun 2013/7/24
 * 增加图片自动播放
 * 解决图片少时崩溃的情形
 * 屏蔽单张图片是的点点
 * 图片为1时显示有问题
 * to do:需要处理size()为0时的异常
 */

package com.example.viewpagerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ViewPagerDemoActivity extends Activity {
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;

	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private ImageView[][] mImageViews;

	/**
	 * 图片资源id
	 */
	private int[] imgIdArray;

	private static final int MSG_CHANGE_PHOTO = 1;

	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 5000;
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = viewPager.getCurrentItem();
				viewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						PHOTO_CHANGE_TIME);
				break;
			}
			super.dispatchMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		// 载入图片资源ID
		// imgIdArray = new int[]{R.drawable.item01, R.drawable.item02,
		// R.drawable.item03,R.drawable.item04,R.drawable.item05,R.drawable.item06,
		// R.drawable.item07, R.drawable.item08};
		// 载入图片资源ID
		imgIdArray = new int[] { R.drawable.item1,R.drawable.item2};

		// 将点点加入到ViewGroup中
		tips = new ImageView[imgIdArray.length];

		if (imgIdArray.length <= 1)
			group.setVisibility(View.GONE);
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}

			group.addView(imageView);
		}

		mImageViews = new ImageView[2][];
		// 将图片装载到数组中,其中一组类似缓冲，防止图片少时出现黑色图片，即显示不出来
		mImageViews[0] = new ImageView[imgIdArray.length];
		mImageViews[1] = new ImageView[imgIdArray.length];

		for (int i = 0; i < mImageViews.length; i++) {
			for (int j = 0; j < mImageViews[i].length; j++) {
				ImageView imageView = new ImageView(this);
				imageView.setBackgroundResource(imgIdArray[j]);
				mImageViews[i][j] = imageView;
				Log.i("TwoActivity_WY", i + "," + j + "\t");
			}
		}

		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int arg0) {
				setImageBackground(arg0 % imgIdArray.length);
			}
		});
		
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(imgIdArray.length==0||imgIdArray.length==1)
					return true;
				else 
					return false;
			}
		});

		// 设置ViewPager的默认项, 设置为长度的50倍，这样子开始就能往左滑动
		viewPager.setCurrentItem((imgIdArray.length) * 50);
		if(imgIdArray.length>1){
			mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
		}
		
	}

	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
 
		@Override
		public void destroyItem(View container, int position, Object object) {
			if (imgIdArray.length == 1)
				((ViewPager) container).removeView(mImageViews[position
						/ imgIdArray.length % 2][0]);
			else
				((ViewPager) container).removeView(mImageViews[position
						/ imgIdArray.length % 2][position % imgIdArray.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			if (imgIdArray.length == 1){
				((ViewPager) container).addView(mImageViews[position / imgIdArray.length % 2][0]);
				return mImageViews[position / imgIdArray.length % 2][0];
			}
			else
				((ViewPager) container).addView(mImageViews[position
						/ imgIdArray.length % 2][position % imgIdArray.length],
						0);
			return mImageViews[position / imgIdArray.length % 2][position
					% imgIdArray.length];
		}

	}



	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItemsIndex) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

}
