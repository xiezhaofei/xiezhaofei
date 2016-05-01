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
		// ����Cameraʵ��
		mCamera = Camera.open();
		// ����Preview view��������Ϊactivity�е�����
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(id.camera_preview);
		preview.addView(mPreview);
		captureButton = (Button) findViewById(id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (isRecording) {
					// ֹͣ¼���ͷ�camera
					mMediaRecorder.stop(); // ֹͣ¼��
					releaseMediaRecorder(); // �ͷ�MediaRecorder����
					mCamera.lock(); // ������Ȩ��MediaRecorder ����camera
					// ֪ͨ�û�¼����ֹͣ
					captureButton.setBackgroundResource(R.drawable.start);
					isRecording = false;
				} else {
					// ��ʼ����Ƶcamera
					if (prepareVideoRecorder()) {
						// Camera�ѿ��ò�������MediaRecorder�Ѿ���,
						// ���ڿ��Կ�ʼ¼��
						mMediaRecorder.start();
						// ֪ͨ�û�¼���ѿ�ʼ
						captureButton.setBackgroundResource(R.drawable.stop);
						isRecording = true;
					} else {
						// ׼��δ����ɣ��ͷ�camera
						releaseMediaRecorder();
						// ֪ͨ�û�
					}
				}
			}
		});
	}

	private boolean prepareVideoRecorder() {
		mCamera.release();
		mCamera = Camera.open();
		mMediaRecorder = new MediaRecorder();
		// ��1����������������ͷָ��MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);
		// ��2����ָ��Դ
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// ��3����ָ��CamcorderProfile����ҪAPI Level 8���ϰ汾��
		mMediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH));
		// ��4����ָ������ļ�
		mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO)
				.toString());
		// ��5����ָ��Ԥ�����
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
		// ��6����������������׼��MediaRecorder
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


	/** Ϊ������Ƶ����File */
	private static File getOutputMediaFile(int type) {
		// ��ȫ�������ʹ��ǰӦ��
		// ��Environment.getExternalStorageState()���SD���Ƿ���װ��
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// �������ͼƬ��Ӧ�ó���ж�غ󻹴��ڡ����ܱ�����Ӧ�ó�����
		// ��˱���λ�������
		// ��������ڵĻ����򴴽��洢Ŀ¼
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// ����ý���ļ���
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
		releaseMediaRecorder(); // �������ʹ��MediaRecorder��������Ҫ�ͷ�����
		releaseCamera(); // ����ͣ�¼��������ͷ�����ͷ
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // ���recorder����
			mMediaRecorder.release(); // �ͷ�recorder����
			mMediaRecorder = null;
			mCamera.lock(); // Ϊ����ʹ����������ͷ
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // Ϊ����Ӧ���ͷ�����ͷ
			mCamera = null;
		}
	}
}
