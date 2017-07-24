package cangwang.com.record_record.record;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by air on 16/8/29.
 */
public interface IRecordView {
//    void updateRecording(boolean isRecording, boolean isPausing);
    void setRecordText(boolean enable, Drawable background, String text);
    void setPauseText(boolean enable, Drawable background, String text);
    void startRecordIntent(Intent permissionIntent);
}
