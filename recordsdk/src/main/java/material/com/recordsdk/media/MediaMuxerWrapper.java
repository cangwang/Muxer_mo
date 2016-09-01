package material.com.recordsdk.media;
/*
 * ScreenRecordingSample
 * Sample project to cature and save audio from internal and video from screen as MPEG4 file.
 *
 * Copyright (c) 2014-15 saki t_saki@serenegiant.com
 *
 * File name: MediaMuxerWrapper.java
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

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MediaMuxerWrapper {
	private static final boolean DEBUG = true;	// TODO set false on release
	private static final String TAG = "MediaMuxerWrapper";
	//录制文件保存地址
	private static  String DIR_NAME = "YYRecord";
	//日期格式
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    //输入路径
	private String mOutputPath;
	//编码混合
	private final MediaMuxer mMediaMuxer;// API >= 18
	//编码个数
	private int mEncoderCount, mStatredCount;
    //录制开始标志
	private boolean mIsStarted;
	//录制状态
	private volatile boolean mIsPaused;
	private MediaEncoder mVideoEncoder, mAudioEncoder;

	/**
	 * Constructor
	 * @param ext extension of output file
	 * @throws IOException
	 */
	public MediaMuxerWrapper(String ext) throws IOException {
		if (TextUtils.isEmpty(ext)) ext = ".mp4";
		try {
			//初始化输出地址
			mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES, ext).toString();
		} catch (final NullPointerException e) {
			throw new RuntimeException("This app has no permission of writing external storage");
		}
		//初始化编码
		mMediaMuxer = new MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
		mEncoderCount = mStatredCount = 0;
		mIsStarted = false;
	}

	/**
	 * 带地址的输出方法
	 * @param directory 目录
	 * @param filename 文件名
	 * @param ext 格式
	 * @throws IOException
     */
	public MediaMuxerWrapper(String directory,String filename,String ext) throws IOException {
		if (TextUtils.isEmpty(ext)) ext = ".mp4";
		try {
			mOutputPath = setFile(directory,filename, ext).toString();
		} catch (final NullPointerException e) {
			throw new RuntimeException("This app has no permission of writing external storage");
		}
		mMediaMuxer = new MediaMuxer(mOutputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
		mEncoderCount = mStatredCount = 0;
		mIsStarted = false;
	}

	/**
	 * 获取输出地址
	 * @return
     */
	public String getOutputPath() {
		return mOutputPath;
	}

	/**
	 * 编码准备
	 * @throws IOException
     */
	public void prepare() throws IOException {
		if (mVideoEncoder != null)
			//初始化屏幕录制
			mVideoEncoder.prepare();
		if (mAudioEncoder != null)
			//初始化声音录制
			mAudioEncoder.prepare();
	}

	/**
	 * 开始录制
	 */
	public void startRecording() {
		if (mVideoEncoder != null)
	     	//屏幕录制开始
			mVideoEncoder.startRecording();
		if (mAudioEncoder != null)
			//声音录制开始
			mAudioEncoder.startRecording();
	}

	/**
	 * 停止录制
	 */
	public void stopRecording() {
		if (mVideoEncoder != null)
			mVideoEncoder.stopRecording();
		mVideoEncoder = null;
		if (mAudioEncoder != null)
			mAudioEncoder.stopRecording();
		mAudioEncoder = null;
	}

	/**
	 * 返回是否在录制
	 * @return
     */
	public synchronized boolean isStarted() {
		return mIsStarted;
	}

	/**
	 * 返回是否暂停
	 * @return
     */
	public synchronized boolean isPaused() {
		return mIsPaused;
	}


	/**
	 * 暂停录制
	 */
	public synchronized void pauseRecording() {
		mIsPaused = true;
		if (mVideoEncoder != null)
			mVideoEncoder.pauseRecording();
		if (mAudioEncoder != null)
			mAudioEncoder.pauseRecording();
	}

	/**
	 * 恢复录制
	 */
	public synchronized void resumeRecording() {
		if (mVideoEncoder != null)
			mVideoEncoder.resumeRecording();
		if (mAudioEncoder != null)
			mAudioEncoder.resumeRecording();
		mIsPaused = false;
	}


//**********************************************************************
//**********************************************************************
	/**
	 * 绑定视频和音频编码器是否加入到混合器中
	 * assign encoder to this class. this is called from encoder.
	 * @param encoder instance of MediaVideoEncoderBase
	 */
	/*package*/ void addEncoder(final MediaEncoder encoder) {
		if (encoder instanceof MediaVideoEncoderBase) {
			if (mVideoEncoder != null)
				throw new IllegalArgumentException("Video encoder already added.");
			mVideoEncoder = encoder;
		} else if (encoder instanceof MediaAudioEncoder) {
			if (mAudioEncoder != null)
				throw new IllegalArgumentException("Video encoder already added.");
			mAudioEncoder = encoder;
		} else
			throw new IllegalArgumentException("unsupported encoder");
		//编码器个数
		mEncoderCount = (mVideoEncoder != null ? 1 : 0) + (mAudioEncoder != null ? 1 : 0);
	}

	/**
	 * 编码器启动
	 * request start recording from encoder
	 * @return true when muxer is ready to write
	 */
	/*package*/ synchronized boolean start() {
		if (DEBUG) Log.v(TAG,  "start:");
		mStatredCount++;
		if ((mEncoderCount > 0) && (mStatredCount == mEncoderCount)) {
			mMediaMuxer.start();
			mIsStarted = true;
			notifyAll();
			if (DEBUG) Log.v(TAG,  "MediaMuxer started:");
		}
		return mIsStarted;
	}

	/**
	 * request stop recording from encoder when encoder received EOS
	*/
	/*package*/ synchronized void stop() {
		if (DEBUG) Log.v(TAG,  "stop:mStatredCount=" + mStatredCount);
		mStatredCount--;
		if ((mEncoderCount > 0) && (mStatredCount <= 0)) {
			mMediaMuxer.stop();
			mMediaMuxer.release();
			mIsStarted = false;
			if (DEBUG) Log.v(TAG,  "MediaMuxer stopped:");
		}
	}

	/**
	 * assign encoder to muxer
	 * @param format
	 * @return minus value indicate error
	 */
	/*package*/ synchronized int addTrack(final MediaFormat format) {
		if (mIsStarted)
			throw new IllegalStateException("muxer already started");
		final int trackIx = mMediaMuxer.addTrack(format);
		if (DEBUG) Log.i(TAG, "addTrack:trackNum=" + mEncoderCount + ",trackIx=" + trackIx + ",format=" + format);
		return trackIx;
	}

	/**
	 * write encoded data to muxer
	 * @param trackIndex
	 * @param byteBuf
	 * @param bufferInfo
	 */
	/*package*/ synchronized void writeSampleData(final int trackIndex, final ByteBuffer byteBuf, final MediaCodec.BufferInfo bufferInfo) {
		if (mStatredCount > 0) {
			mMediaMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
		}
	}

//**********************************************************************
//**********************************************************************
    /**
	 * 组合默认的输出文件地址
     * generate output file
     * @param type Environment.DIRECTORY_MOVIES / Environment.DIRECTORY_DCIM etc.
     * @param ext .mp4(.m4a for audio) or .png
     * @return return null when this app has no writing permission to external storage.
     */
    public static final File getCaptureFile(final String type, final String ext) {
		final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
		Log.d(TAG, "path=" + dir.toString());
		dir.mkdirs();
        if (dir.canWrite()) {
        	return new File(dir, getDateTimeString() + ext);
        }
    	return null;
    }

	/**
	 * 组合自定义输出文件地址
	 * @param directory
	 * @param filename
	 * @param ext
     * @return
     */
	public static final File setFile(String directory,String filename,final String ext){
		final File dir;
		if(directory != null){
			dir = new File(Environment.getExternalStorageDirectory(),directory);
		}else{
			dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),directory);
		}

		dir.mkdirs();
		if (dir.canWrite()){
			if (filename != null) {
				return new File(dir, filename + ext);
			}else{
				return new File(dir, getDateTimeString() + ext);
			}
		}
		return null;
	}

    /**
	 * 获取当前日期时间
     * get current date and time as String
     * @return
     */
    private static final String getDateTimeString() {
    	final GregorianCalendar now = new GregorianCalendar();
    	return mDateTimeFormat.format(now.getTime());
    }

}