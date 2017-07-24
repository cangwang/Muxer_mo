package material.com.muxer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.ref.WeakReference;

import cangwang.com.base.service.ScreenRecorderService;
import material.com.muxer.activity.ScreenRecordActivity;

/**
 * Created by air on 16/7/6.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private final static boolean DEBUG = true;
    private final static String TAG = "MyBroadcastReceiver";
    private final WeakReference<ScreenRecordActivity> mWeakParent;
    public MyBroadcastReceiver(final ScreenRecordActivity parent) {
        mWeakParent = new WeakReference<ScreenRecordActivity>(parent);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DEBUG) Log.v(TAG, "onReceive:" + intent);
        final String action = intent.getAction();
        if (ScreenRecorderService.ACTION_QUERY_STATUS_RESULT.equals(action)) {
            Boolean isRecording = intent.getBooleanExtra(ScreenRecorderService.EXTRA_QUERY_RESULT_RECORDING, false);
            Boolean isPausing = intent.getBooleanExtra(ScreenRecorderService.EXTRA_QUERY_RESULT_PAUSING, false);

            final ScreenRecordActivity parent = mWeakParent.get();
            if (parent != null) {
                parent.updateRecording(isRecording, isPausing);
            }
        }
    }
}
