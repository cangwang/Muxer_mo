package cangwang.com.base.service;
/*
 * ScreenRecordingSample
 * Sample project to cature and save audio from internal and video from screen as MPEG4 file.
 *
 * Copyright (c) 2015 saki t_saki@serenegiant.com
 *
 * File name: ScreenRecorderService.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
*/

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import cangwang.com.base.R;
import material.com.recordsdk.media.MediaEncoder;
import material.com.recordsdk.media.MediaRecord;

public class ScreenRecorderService extends Service {
	private static final boolean DEBUG = false;
	private static final String TAG = "ScreenRecorderService";

	private static final String BASE = "com.serenegiant.service.ScreenRecorderService.";
	public static final String ACTION_START = BASE + "ACTION_START";
	public static final String ACTION_STOP = BASE + "ACTION_STOP";
	public static final String ACTION_PAUSE = BASE + "ACTION_PAUSE";
	public static final String ACTION_RESUME = BASE + "ACTION_RESUME";
	public static final String ACTION_QUERY_STATUS = BASE + "ACTION_QUERY_STATUS";
	public static final String ACTION_QUERY_STATUS_RESULT = BASE + "ACTION_QUERY_STATUS_RESULT";
	public static final String EXTRA_RESULT_CODE = BASE + "EXTRA_RESULT_CODE";
	public static final String EXTRA_QUERY_RESULT_RECORDING = BASE + "EXTRA_QUERY_RESULT_RECORDING";
	public static final String EXTRA_QUERY_RESULT_PAUSING = BASE + "EXTRA_QUERY_RESULT_PAUSING";
	public static final String ACTION_CUSTOM_SETTING = BASE + "ACTION_CUSTOM_SETTING";
	private static final int NOITIFY_ID = 10086;

	public static final String RECORD_HEIGHT = "RECORD_HEIGHT";
	public static final String RECORD_WIDTH = "RECORD_WIDTH";
	public static final String RECORD_DENSITY = "RECORD_DENSITY";
	public static final String RECORD_PATH = "RECORD_PATH";

	private static Object sSync = new Object();
	private MediaRecord mMediaRecord;
	private RemoteViews rv;
	private Notification notification;

	private NotificationManager notificationManager;

	public ScreenRecorderService() {
//		super(TAG);
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (DEBUG) Log.v(TAG, "onCreate:");
		//获取录制单例
		mMediaRecord = MediaRecord.getInstance(this);
		notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		rv = new RemoteViews(getPackageName(), R.layout.noti_layout);

		mMediaRecord.setMediaEncoderListener(new MediaRecord.MediaEncoderListener() {
			@Override
			public void onPrepared(MediaEncoder mediaEncoder) {

			}

			@Override
			public void onStopped(MediaEncoder mediaEncoder) {

			}
		});
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (DEBUG) Log.v(TAG, "onHandleIntent:intent=" + intent);
//		switch (intent.getAction()){
//			case ACTION_START:                 //启动录制
//				mMediaRecord.startScreenRecord(Activity.RESULT_OK,intent,this);
//				updateStatus();
//				createNotification();
//				break;
//			case ACTION_STOP:                 //停止录制
//				mMediaRecord.stopScreenRecord();
//				updateStatus();
//				removeNotification();
//				break;
//			case ACTION_QUERY_STATUS:
//				updateStatus();
//				break;
//			case ACTION_PAUSE:                //暂停录制
//				mMediaRecord.pauseScreenRecord();
//				updateStatus();
//				addBtnPending(R.id.pause_btn,ACTION_RESUME,"继续");
//				break;
//			case ACTION_RESUME:               //恢复录制
//				mMediaRecord.resumeScreenRecord();
//				updateStatus();
//
//				addBtnPending(R.id.pause_btn,ACTION_PAUSE,"暂停");
//				break;
//			default:
//				break;
//		}
		final String action = intent.getAction();
		if (ACTION_START.equals(action)) { //启动录制
			mMediaRecord.startScreenRecord(Activity.RESULT_OK,intent,this);
			updateStatus();
			createNotification();
		} else if (ACTION_STOP.equals(action)) { //停止录制
			mMediaRecord.stopScreenRecord();
			updateStatus();
			removeNotification();
		} else if (ACTION_QUERY_STATUS.equals(action)) { //查询录制状态
			updateStatus();
		} else if (ACTION_PAUSE.equals(action)) { //暂停录制
			mMediaRecord.pauseScreenRecord();
			updateStatus();
			addBtnPending(R.id.pause_btn,ACTION_RESUME,"继续");

		} else if (ACTION_RESUME.equals(action)) { //恢复录制
			mMediaRecord.resumeScreenRecord();
			updateStatus();

			addBtnPending(R.id.pause_btn,ACTION_PAUSE,"暂停");
		}else if(ACTION_CUSTOM_SETTING.equals(action)){
			int width = intent.getIntExtra(RECORD_WIDTH,480);
			int height = intent.getIntExtra(RECORD_HEIGHT,800);
			int density = intent.getIntExtra(RECORD_DENSITY,60);
			String path = intent.getStringExtra(RECORD_PATH);
			if(width != 0 && height != 0)
				mMediaRecord.setSize(width,height);
			if(density !=0 )
				mMediaRecord.setDensity(density);
			if(path!=null)
				mMediaRecord.setRecordDictory(path);
		}
		return super.onStartCommand(intent, flags, startId);
	}

//	@Override
//	protected void onHandleIntent(final Intent intent) {
//		if (DEBUG) Log.v(TAG, "onHandleIntent:intent=" + intent);
//		final String action = intent.getAction();
//		if (ACTION_START.equals(action)) { //启动录制
//			mMediaRecord.startScreenRecord(Activity.RESULT_OK,intent,this);
//			updateStatus();
//			createNotification();
//		} else if (ACTION_STOP.equals(action)) { //停止录制
//			mMediaRecord.stopScreenRecord();
//			updateStatus();
//			removeNotification();
//		} else if (ACTION_QUERY_STATUS.equals(action)) { //查询录制状态
//			updateStatus();
//		} else if (ACTION_PAUSE.equals(action)) { //暂停录制
//			mMediaRecord.pauseScreenRecord();
//			updateStatus();
//			addBtnPending(R.id.pause_btn,ACTION_RESUME,"resume");
//
//		} else if (ACTION_RESUME.equals(action)) { //恢复录制
//			mMediaRecord.resumeScreenRecord();
//			updateStatus();
//
//			addBtnPending(R.id.pause_btn,ACTION_PAUSE,"pause");
//		}
//	}

	/**
	 * 广播当前状态
	 */
	private void updateStatus() {
		final boolean isRecording, isPausing;
		synchronized (sSync) {
			isRecording = mMediaRecord.getRecordStatus();
			isPausing = mMediaRecord.getPauseStatus();
		}
		final Intent result = new Intent();
		result.setAction(ACTION_QUERY_STATUS_RESULT);
		result.putExtra(EXTRA_QUERY_RESULT_RECORDING, isRecording);
		result.putExtra(EXTRA_QUERY_RESULT_PAUSING, isPausing);
		if (DEBUG) Log.v(TAG, "sendBroadcast:isRecording=" + isRecording + ",isPausing=" + isPausing);
		sendBroadcast(result);
	}

	@Override
	public void onDestroy() {
		mMediaRecord.stopScreenRecord();
		super.onDestroy();
		mMediaRecord = null;
	}

	private void createNotification(){
//		if (notificationManager == null)
//			notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		NotificationCompat.Builder mBuidler = new NotificationCompat.Builder(this);

		String tickerText = "录制中";
		long when = System.currentTimeMillis();
		notification = new Notification(R.mipmap.ic_launcher, tickerText, when);
		//不能手动清理
		notification.flags= Notification.FLAG_NO_CLEAR;
		notification.contentView = rv;

		addBtnPending(R.id.record_btn,ACTION_STOP,"停止");
		addBtnPending(R.id.pause_btn,ACTION_PAUSE,"暂停");

		try {
			Class clazz = Class.forName("material.com.muxer.activity.ScreenRecordActivity");
			Intent enterIntent = new Intent(this, clazz);
			PendingIntent enterPendingIntent = PendingIntent.getActivity(this, 0, enterIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.enter_btn, enterPendingIntent);

			//把定义的notification 传递给 notificationmanager
			notificationManager.notify(NOITIFY_ID, notification);
		}catch (ClassNotFoundException e){
			Log.e(TAG,"[ScreenRecorderService][createNotification]ClassNotFoundException="+e.toString());
		}
	}

	private void removeNotification(){
		if(notificationManager != null)
			notificationManager.cancel(NOITIFY_ID);
	}

	private void changeRvText(final int id,final String text){
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				notification.contentView.setTextViewText(id,text);
				notificationManager.notify(NOITIFY_ID,notification);
			}
		});
	}

	private void addBtnPending(int id,String action,String text){
		if(notification == null)
			return;
		Intent intent = new Intent(this,ScreenRecorderService.class);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentView.setOnClickPendingIntent(id,pendingIntent);
		changeRvText(id,text);
	}
}
