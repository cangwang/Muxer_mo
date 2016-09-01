package material.com.muxer.fragment.record;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import material.com.muxer.service.ScreenRecorderService;

/**
 * Created by zjl on 16/8/29.
 */
public class RecordPresenter {
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;

    private IRecordView recordView;
    private Context mContent;
    private boolean DEBUG = true;
    private static final String TAG = "RecordPresenter";

    public RecordPresenter(Context context,IRecordView view){
        mContent = context;
        this.recordView = view;
    }

    public void record(Context context,boolean isRecording){
        if (!isRecording) {
            final MediaProjectionManager manager
                    = (MediaProjectionManager)context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            final Intent permissionIntent = manager.createScreenCaptureIntent();
            ((AppCompatActivity)context).startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
//                    createNotification();
        } else {
            final Intent intent = new Intent(context, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_STOP);
            context.startService(intent);
//                    removeNotification();
        }
    }

    public void pause(Context context,boolean isPausing){
        if (!isPausing) {
            final Intent intent = new Intent(context, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_PAUSE);
            context.startService(intent);
        } else {
            final Intent intent = new Intent(context, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_RESUME);
            context.startService(intent);
        }
    }

    /**
     * 查询状态
     */
    public void queryRecordingStatus() {
        if (DEBUG) Log.v(TAG, "queryRecording:");
        final Intent intent = new Intent(mContent, ScreenRecorderService.class);
        intent.setAction(ScreenRecorderService.ACTION_QUERY_STATUS);
        mContent.startService(intent);
    }

    /**
     * 开始录制
     * @param resultCode
     * @param data
     */
    public void startScreenRecorder(final int resultCode, final Intent data) {
        final Intent intent = new Intent(mContent, ScreenRecorderService.class);
        intent.setAction(ScreenRecorderService.ACTION_START);
        intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode);
        intent.putExtras(data);
        mContent.startService(intent);
    }
}
