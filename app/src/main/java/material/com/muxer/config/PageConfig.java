package material.com.muxer.config;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cangwang.com.record_record.record.RecordFragment;
import material.com.muxer.R;

/**
 * Created by air on 16/7/6.
 */
public class PageConfig {
    public static List<String> pageTitles = new ArrayList<String>();

    public static List<String> getPageTitles(Context context) {
        pageTitles.clear();
        pageTitles.add(context.getString(R.string.action_record));
        pageTitles.add(context.getString(R.string.action_read));
        pageTitles.add(context.getString(R.string.action_settings));
        return pageTitles;
    }

    private static final String RecordFragment = "cangwang.com.record_record.record.RecordFragment";

    private static final String ReadFragment = "cangwang.com.record_read.read.ReadFragment";

    private static final String SettingFragment = "cangwang.com.record_setting.setting.SettingFragment";

    public static String[] fragmentNames = {
            RecordFragment,
            ReadFragment,
            SettingFragment,
    };

}
