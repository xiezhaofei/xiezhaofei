package com.jg.mycarema;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** ����������ͷԤ���� */

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder mHolder;

	private Camera mCamera;

	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {

		super(context);

		mCamera = camera;

		// ��װһ��SurfaceHolder.Callback��

		// �������������ٵײ�surfaceʱ�ܹ����֪ͨ��

		mHolder = getHolder();

		mHolder.addCallback(this);

		// �ѹ��ڵ����ã����汾����3.0��Android����Ҫ
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	public void surfaceCreated(SurfaceHolder holder) {

		// surface�ѱ����������ڰ�Ԥ�������λ��֪ͨ����ͷ

		try {

			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();

		} catch (IOException e) {

			Log.d("TAG", "Error setting camera preview: " + e.getMessage());

		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		// �մ��롣ע����activity���ͷ�����ͷԤ������
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		// ���Ԥ���޷����Ļ���ת��ע��˴����¼�

		// ȷ�������Ż�����ʱֹͣԤ��

		if (mHolder.getSurface() == null) {

			// Ԥ��surface������

			return;

		}

		// ����ʱֹͣԤ��

		try {

			mCamera.stopPreview();

		} catch (Exception e) {

			// ���ԣ���ͼֹͣ�����ڵ�Ԥ��

		}

		// �ڴ˽������š���ת��������֯��ʽ
		// ���µ���������Ԥ��
		try {

			mCamera.setPreviewDisplay(mHolder);

			mCamera.startPreview();

		} catch (Exception e) {

			Log.d("TAG", "Error starting camera preview: " + e.getMessage());

		}

	}

}