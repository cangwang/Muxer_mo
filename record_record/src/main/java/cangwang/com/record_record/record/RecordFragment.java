package cangwang.com.record_record.record;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;

import cangwang.com.base.basecomponent.BaseFragment;
import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.record_record.R;
import event.UpdateRecordViewEvent;


/**
 * Created by zjl on 16/7/6.
 */
public class RecordFragment extends BaseFragment implements IRecordView{
    private static final boolean DEBUG = true;
    public static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    private static final String TAG = "RecordFragment";
    private Button mRecrodBtn;
    private Button mPausepBtn;

    private boolean isRecording = false;
    private boolean isPausing = false;
    private Context mContent;

    private RecordPresenter recordPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_layout,container,false);
        mContent = view.getContext();
        if(recordPresenter == null)
            recordPresenter = new RecordPresenter(getContext(),this);
        initUI(view);
        return view;
    }

    private void initUI(final View view){
        mRecrodBtn = (Button) view.findViewById(R.id.record_btn);
        mPausepBtn = (Button) view.findViewById(R.id.stop_btn);
        mRecrodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isRecording) {
//                    final MediaProjectionManager manager
//                            = (MediaProjectionManager)mContent.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//                    final Intent permissionIntent = manager.createScreenCaptureIntent();
//                    startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
//                } else {
//                    final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//                    intent.setAction(ScreenRecorderService.ACTION_STOP);
//                    mContent.startService(intent);
//                }
                recordPresenter.record(mContent);
            }
        });
        mPausepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!isPausing) {
//                    final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//                    intent.setAction(ScreenRecorderService.ACTION_PAUSE);
//                    view.getContext().startService(intent);
//                } else {
//                    final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//                    intent.setAction(ScreenRecorderService.ACTION_RESUME);
//                    view.getContext().startService(intent);
//                }
                recordPresenter.pause(mContent);
            }
        });
        recordPresenter.updateRecording(false,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        recordPresenter.queryRecordingStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"requsetCode = "+requestCode+",resultCode = " + resultCode+", data = "+data);
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_SCREEN_CAPTURE == requestCode) {
            recordPresenter.startScreenRecorder(resultCode, data);
        }
    }

//    /**
//     * 更新录制界面
//     * @param isRecording
//     * @param isPausing
//     */
//    public void updateRecording(boolean isRecording, boolean isPausing) {
//        if (DEBUG) Log.v(TAG, "updateRecording:isRecording=" + isRecording + ",isPausing=" + isPausing);
//        recordPresenter.updateRecording(isRecording,isPausing);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recordPresenter.Destroy();
    }

    @Override
    public void setRecordText(boolean enable, Drawable background, String text) {
        mRecrodBtn.setEnabled(enable);
        if(text != null)
            mRecrodBtn.setText(text);
        if(background != null)
            mRecrodBtn.setBackground(background);
    }

    @Override
    public void setPauseText(boolean enable, Drawable background,String text) {
        mPausepBtn.setEnabled(enable);
        if(text != null)
            mPausepBtn.setText(text);
        if(background != null)
            mPausepBtn.setBackground(background);
    }

    @Override
    public void startRecordIntent(Intent permissionIntent) {
        startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
    }

    @Subscribe
    public void onEvent(UpdateRecordViewEvent evt){
        isRecording = evt.isRecording;
        isPausing = evt.isPausing;
    }

    @Override
    public BasePresenter getPresenter() {
        return recordPresenter;
    }
}
