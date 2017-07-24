package cangwang.com.base.modulebus;

/**
 * Created by air on 16/10/19.
 */

public class ELPublicApi {
    public interface RecordApi extends ELModuleApi {
        void updateRecordView(boolean isRecording, boolean isPausing);
    }
    public interface ReadApi extends ELModuleApi {
        void changeSetting(boolean isCheck);
    }
}
