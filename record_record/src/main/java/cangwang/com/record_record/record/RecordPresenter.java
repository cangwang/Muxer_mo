package cangwang.com.record_record.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.base.modulebus.ELModuleApi;
import cangwang.com.base.modulebus.ELPublicApi;
import cangwang.com.base.service.ScreenRecorderService;
import cangwang.com.record_record.R;
import event.UpdateRecordViewEvent;

/**
 * Created by zjl on 16/8/29.
 */
public class RecordPresenter extends BasePresenter implements ELPublicApi.RecordApi{

    private IRecordView recordView;
    private Context mContent;
    private boolean DEBUG = true;
    private static final String TAG = "RecordPresenter";

    private boolean isRecording = false;
    private boolean isPausing = false;

    @Override
    public Map<Class<? extends ELModuleApi>, ELModuleApi> getModuleApi() {
        Map<Class<? extends ELModuleApi>,ELModuleApi> map = new HashMap<>();
        map.put(ELPublicApi.RecordApi.class,this);
        return map;
    }

    public RecordPresenter(Context context, IRecordView view){
        super();
        mContent = context;
        this.recordView = view;
        EventBus.getDefault().register(this);
    }

    public void record(Context context){
        if (!isRecording) {
            final MediaProjectionManager manager
                    = (MediaProjectionManager)context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            final Intent permissionIntent = manager.createScreenCaptureIntent();
//            ((AppCompatActivity)context).startActivityForResult(permissionIntent, RecordFragment.REQUEST_CODE_SCREEN_CAPTURE);
            ((AppCompatActivity)context).startActivityForResult(permissionIntent, RecordFragment.REQUEST_CODE_SCREEN_CAPTURE);
//            recordView.startRecordIntent(permissionIntent);
//                    createNotification();
        } else {
            final Intent intent = new Intent(context, ScreenRecorderService.class);
            intent.setAction(ScreenRecorderService.ACTION_STOP);
            context.startService(intent);
//                    removeNotification();
        }
    }


    public void pause(Context context){
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
        if (resultCode != Activity.RESULT_OK) {
            // when no permission
            Toast.makeText(mContent, "permission denied", Toast.LENGTH_LONG).show();
            return;
        }

        final Intent intent = new Intent(mContent, ScreenRecorderService.class);
        intent.setAction(ScreenRecorderService.ACTION_START);
        intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode);
        intent.putExtras(data);
        mContent.startService(intent);
    }

    public void updateRecording(boolean isRecording, boolean isPausing) {
        if (DEBUG) Log.v(TAG, "updateRecording:isRecording=" + isRecording + ",isPausing=" + isPausing);
        this.isRecording = isRecording;
        this.isPausing = isPausing;
        if(recordView == null )return;
        if(!isRecording){
            recordView.setRecordText(true,mContent.getResources().getDrawable(R.drawable.record_btn_selector),mContent.getString(R.string.record));
            recordView.setPauseText(false,mContent.getResources().getDrawable(R.drawable.pause_btn_selector),mContent.getString(R.string.pause));
        }else {
            recordView.setRecordText(true,mContent.getResources().getDrawable(R.drawable.stop_btn_selector),mContent.getString(R.string.stop));
            if(!isPausing){
                recordView.setPauseText(true,mContent.getResources().getDrawable(R.drawable.pause_btn_selector),mContent.getString(R.string.pause));
            }else{
                recordView.setPauseText(true,mContent.getResources().getDrawable(R.drawable.resume_btn_selector),mContent.getString(R.string.resume));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateRecordViewEvent evt){
        updateRecording(evt.isRecording,evt.isPausing);
    }

    @Override
    public void updateRecordView(boolean isRecording, boolean isPausing) {
        updateRecording(isRecording,isPausing);
    }

    public void Destroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
