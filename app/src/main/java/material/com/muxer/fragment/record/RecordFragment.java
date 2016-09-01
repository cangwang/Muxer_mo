package material.com.muxer.fragment.record;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import material.com.muxer.R;

/**
 * Created by zjl on 16/7/6.
 */
public class RecordFragment extends Fragment implements IRecordView{
    private static final boolean DEBUG = true;
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
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
////                    createNotification();
//                } else {
//                    final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//                    intent.setAction(ScreenRecorderService.ACTION_STOP);
//                    view.getContext().startService(intent);
////                    removeNotification();
//                }
                recordPresenter.record(mContent,isRecording);
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
                recordPresenter.pause(mContent,isPausing);
            }
        });
        updateRecording(false,false);
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
            if (resultCode != Activity.RESULT_OK) {
                // when no permission
                Toast.makeText(mContent, "permission denied", Toast.LENGTH_LONG).show();
                return;
            }
            recordPresenter.startScreenRecorder(resultCode, data);
        }
    }

//    /**
//     * 查询状态
//     */
//    private void queryRecordingStatus() {
//        if (DEBUG) Log.v(TAG, "queryRecording:");
//        final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//        intent.setAction(ScreenRecorderService.ACTION_QUERY_STATUS);
//        mContent.startService(intent);
//    }
//
//    /**
//     * 开始录制
//     * @param resultCode
//     * @param data
//     */
//    private void startScreenRecorder(final int resultCode, final Intent data) {
//        final Intent intent = new Intent(mContent, ScreenRecorderService.class);
//        intent.setAction(ScreenRecorderService.ACTION_START);
//        intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode);
//        intent.putExtras(data);
//        mContent.startService(intent);
//    }

    /**
     * 更新录制界面
     * @param isRecording
     * @param isPausing
     */
    @Override
    public void updateRecording(boolean isRecording, boolean isPausing) {
        if (DEBUG) Log.v(TAG, "updateRecording:isRecording=" + isRecording + ",isPausing=" + isPausing);

        this.isRecording = isRecording;
        this.isPausing = isPausing;
        if(!isRecording){
            mRecrodBtn.setText(mContent.getString(R.string.record));
            mRecrodBtn.setBackground(mContent.getDrawable(R.drawable.record_btn_selector));

            mPausepBtn.setText(mContent.getString(R.string.pause));
            mPausepBtn.setEnabled(false);
            mPausepBtn.setBackground(mContent.getDrawable(R.drawable.pause_btn_selector));
        }else {
            mRecrodBtn.setText(mContent.getString(R.string.stop));
            mRecrodBtn.setBackground(mContent.getDrawable(R.drawable.stop_btn_selector));
            mPausepBtn.setEnabled(true);
            if(!isPausing){
                mPausepBtn.setText(mContent.getString(R.string.pause));
                mPausepBtn.setBackground(mContent.getDrawable(R.drawable.pause_btn_selector));
            }else{
                mPausepBtn.setText(mContent.getString(R.string.resume));
                mPausepBtn.setBackground(mContent.getDrawable(R.drawable.resume_btn_selector));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
