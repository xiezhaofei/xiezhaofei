package com.jg.mycarema;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.jg.mycarema.R.id;

public class MainActivity extends Activity {
	private int MEDIA_TYPE_VIDEO = 1;
	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;
	private Button captureButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 创建Camera实例
		mCamera = Camera.open();
		// 创建Preview view并将其设为activity中的内容
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(id.camera_preview);
		preview.addView(mPreview);
		captureButton = (Button) findViewById(id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isRecording) {
					// 停止录像并释放camera
					mMediaRecorder.stop(); // 停止录像
					releaseMediaRecorder(); // 释放MediaRecorder对象
					mCamera.lock(); // 将控制权从MediaRecorder 交回camera
					// 通知用户录像已停止
					captureButton.setBackgroundResource(R.drawable.start);
					isRecording = false;
				} else {
					// 初始化视频camera
					if (prepareVideoRecorder()) {
						// Camera已可用并解锁，MediaRecorder已就绪,
						// 现在可以开始录像
						mMediaRecorder.start();
						// 通知用户录像已开始
						captureButton.setBackgroundResource(R.drawable.stop);
						isRecording = true;
					} else {
						// 准备未能完成，释放camera
						releaseMediaRecorder();
						// 通知用户
					}
				}
			}
		});
	}

	private boolean prepareVideoRecorder() {
		mCamera.release();
		mCamera = Camera.open();
		mMediaRecorder = new MediaRecorder();
		// 第1步：解锁并将摄像头指向MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);
		// 第2步：指定源
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// 第3步：指定CamcorderProfile（需要API Level 8以上版本）
		mMediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH));
		// 第4步：指定输出文件
		mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO)
				.toString());
		// 第5步：指定预览输出
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
		// 第6步：根据以上配置准备MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d("TAG",
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d("TAG",
					"IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}


	/** 为保存视频创建File */
	private static File getOutputMediaFile(int type) {
		// 安全起见，在使用前应该
		// 用Environment.getExternalStorageState()检查SD卡是否已装入
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// 如果期望图片在应用程序卸载后还存在、且能被其它应用程序共享，
		// 则此保存位置最合适
		// 如果不存在的话，则创建存储目录
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// 创建媒体文件名
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
			Log.i("path============", mediaFile.getAbsolutePath());
		return mediaFile;
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder(); // 如果正在使用MediaRecorder，首先需要释放它。
		releaseCamera(); // 在暂停事件中立即释放摄像头
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // 清除recorder配置
			mMediaRecorder.release(); // 释放recorder对象
			mMediaRecorder = null;
			mCamera.lock(); // 为后续使用锁定摄像头
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // 为其它应用释放摄像头
			mCamera = null;
		}
	}
}
