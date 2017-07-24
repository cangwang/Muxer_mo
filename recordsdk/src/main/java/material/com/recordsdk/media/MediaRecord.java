package material.com.recordsdk.media;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.IOException;

/**
 * Created by zjl on 2016/3/1.
 */
public class MediaRecord {
    private static final boolean DEBUG = true;
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    //录制单例
    public static MediaRecord instance = null;
    private static String TAG = "MediaRecord";

    private Context mContext;
    //异步堵塞
    private static Object sSync = new Object();
    //媒体编码器
    private static MediaMuxerWrapper sMuxer;
    //录制状态 录制&停止
    private boolean isRecording = false;
    //录制状态 暂停&继续
    private boolean isPausing =  false;
    //屏幕状态
    DisplayMetrics metrics;
    //录制宽度
    int recordWidth;
    //录制高度
    int recordHeigtht;
    //录制频率
    int density;
    //录制目录
    String dictoryPath = null;
    //录制文件名
    String fileName = null;
    //媒体录制管理
    private MediaProjectionManager mMediaProjectionManager;


    public MediaRecord(Context mContext){
        this.mContext = mContext;
        mMediaProjectionManager = (MediaProjectionManager)mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        metrics = mContext.getResources().getDisplayMetrics();
        recordWidth = metrics.widthPixels;
        recordHeigtht = metrics.heightPixels;
        density = metrics.densityDpi;
    }


    /**
     * 设置单例模式
     * @param mContext
     * @return
     */
    public static MediaRecord getInstance(Context mContext) {
        if(instance == null){
            synchronized (MediaRecord.class) {
                if (instance == null)
                    instance = new MediaRecord(mContext.getApplicationContext());
            }
        }
        return instance;
    }

    /**
     * 设置录制宽高
     * @param width
     * @param height
     */
    public void setSize(int width,int height){
        this.recordWidth = width;
        this.recordHeigtht = height;
    }

    /**
     * 设置录制频率
     * @param density
     */
    public void setDensity(int density){
        this.density = density;
    }

    /**
     * 设置录制目录
     * @param dictoryPath
     */
    public void setRecordDictory(String dictoryPath){
        this.dictoryPath = dictoryPath;
    }

    /**
     * 设置录制文件名
     * @param fileName
     */
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    //以下是链式接口

    /**
     * 设置录制宽高
     * @param width
     * @param height
     */
    public MediaRecord makeSize(int width,int height){
        this.recordWidth = width;
        this.recordHeigtht = height;
        return this;
    }

    /**
     * 设置录制频率
     * @param density
     */
    public MediaRecord makeDensity(int density){
        this.density = density;
        return this;
    }

    /**
     * 设置录制目录
     * @param dictoryPath
     */
    public MediaRecord makeRecordDictory(String dictoryPath){
        this.dictoryPath = dictoryPath;
        return this;
    }

    /**
     * 设置录制文件名
     * @param fileName
     */
    public MediaRecord makeFileName(String fileName){
        this.fileName = fileName;
        return this;
    }

    /**
     * 开始录制
     * start screen recording as .mp4 file
     * @param intent
     */
    public void startScreenRecord(final int resultCode,final Intent intent,Context mContext) {
        if (DEBUG) Log.v(TAG, "startScreenRecord:sMuxer=" + sMuxer);
        synchronized (sSync) {
            if (sMuxer == null) { //编码对象不为空
                // get MediaProjection

                final MediaProjection projection = mMediaProjectionManager.getMediaProjection(resultCode, intent);
                if (projection != null) {

                    if (DEBUG) Log.v(TAG, "startRecording:");
                    try {
                        //编码对象初始化

                        if(dictoryPath!=null) {
                            sMuxer = new MediaMuxerWrapper(dictoryPath, fileName, ".mp4");
                        }else {
                            sMuxer = new MediaMuxerWrapper(".mp4");	// if you record audio only, ".m4a" is also OK.
                        }
                        // 屏幕录制
                        new MediaScreenEncoder(sMuxer, mMediaEncoderListener,
                                    projection, recordWidth, recordHeigtht, density);
                        // 声音录制
                        new MediaAudioEncoder(sMuxer, mMediaEncoderListener);
                        //编码准备
                        sMuxer.prepare();
                        //编码开始
                        sMuxer.startRecording();
                    } catch (final IOException e) {
                        Log.e(TAG, "startScreenRecord:", e);
                    }
                }
            }
        }
    }

    /**
     * 停止屏幕录制
     */
    public void stopScreenRecord() {
        if (DEBUG) Log.v(TAG, "stopScreenRecord:sMuxer=" + sMuxer);
        synchronized (sSync) {
            if (sMuxer != null) {
                sMuxer.stopRecording();
                sMuxer = null;
                // you should not wait here
            }
        }
    }

    /**
     * 暂停屏幕录制
     */
    public void pauseScreenRecord() {
        if (DEBUG) Log.v(TAG, "pauseScreenRecord" );
        synchronized (sSync) {
            if (sMuxer != null) {
                sMuxer.pauseRecording();
            }
        }
    }

    /**
     * 恢复屏幕录制
     */
    public void resumeScreenRecord() {
        if (DEBUG) Log.v(TAG, "resumeScreenRecord" );
        synchronized (sSync) {
            if (sMuxer != null) {
                sMuxer.resumeRecording();
            }
        }
    }

    /**
     * 编码回调接口
     */
    private MediaEncoder.MediaEncoderListener mMediaEncoderListener;

    /**
     * 外部回调接口
     */
    public interface MediaEncoderListener extends MediaEncoder.MediaEncoderListener{

    }

    public void setMediaEncoderListener(MediaEncoderListener listener){
        mMediaEncoderListener = listener;
    }

    public boolean getRecordStatus(){
        return sMuxer != null;
    }

    public boolean getPauseStatus(){
        return sMuxer != null &&sMuxer.isPaused();
    }
}
