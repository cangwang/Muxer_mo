package cangwang.com.record_setting.setting;

/**
 * Created by air on 16/8/29.
 */

public interface ISettingsView {

    String getRecordHeight();
    void setRecordHeigth(String height);

    String getRecordWidth();
    void setRecordWidth(String width);

    String getRecordDensity();
    void setRecordDesnsity(String desnsity);

    void setRecordDir(String dir);
    String getRecordDir();

    void setCustomSettingsVisible(boolean isVisible);

    boolean getIfDfSettings();
}
