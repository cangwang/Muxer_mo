package event;

/**
 * Created by air on 16/10/11.
 */

public class UpdateRecordViewEvent {
    public boolean isRecording;
    public boolean isPausing;
    public UpdateRecordViewEvent(boolean isRecording,boolean isPausing){
        this.isRecording = isRecording;
        this.isPausing =  isPausing;
    }
}
