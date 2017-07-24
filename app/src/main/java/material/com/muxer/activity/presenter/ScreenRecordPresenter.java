package material.com.muxer.activity.presenter;

import android.content.Context;
import java.util.Map;

import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.base.modulebus.ELModuleApi;
import material.com.muxer.activity.view.IScreenRecordView;

/**
 * Created by air on 16/8/11.
 */
public class ScreenRecordPresenter extends BasePresenter{
    private IScreenRecordView screenRecordView;
    private Context mContext;

    public ScreenRecordPresenter(Context context,IScreenRecordView screenRecordView){
        this.screenRecordView = screenRecordView;
        this.mContext = context;
    }

    @Override
    public Map<Class<? extends ELModuleApi>, ELModuleApi> getModuleApi() {
        return null;
    }

    public void Destroy(){

    }
}
