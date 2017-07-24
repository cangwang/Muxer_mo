package cangwang.com.record_read.read;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.base.greenDao.SettingsInfo;
import cangwang.com.base.greenDao.SettingsInfoDao;
import cangwang.com.base.modulebus.ELModuleApi;
import cangwang.com.base.modulebus.ELPublicApi;
import cangwang.com.base.modulebus.IReadClient;
import cangwang.com.base.modulebus.ModuleBus;
import cangwang.com.base.modulebus.ModuleEvent;
import cangwang.com.base.utils.DBManager;
import cangwang.com.base.utils.ReadUtil;
import cangwang.com.record_read.adapter.ReadRecylerAdapter;
import event.ChangeSettingEvent;


/**
 * Created by air on 16/10/13.
 */

public class ReadPresenter extends BasePresenter implements ELPublicApi.ReadApi{
    public static final String DEFAULT_PATH = "/sdcard/Movies/YYRecord";
    private IReadView readView;
    private Context mContext;
    private ReadRecylerAdapter adapter;

    private SettingsInfoDao mSIDao;
    private SettingsInfo si;
    private String recordPath = DEFAULT_PATH;

    @Override
    public Map<Class<? extends ELModuleApi>, ELModuleApi> getModuleApi() {
        Map<Class<? extends ELModuleApi>,ELModuleApi> map = new HashMap<>();
        map.put(ELPublicApi.ReadApi.class,this);
        return map;
    }


    public ReadPresenter(IReadView readView){
        this.readView = readView;
    }

    public ReadPresenter(Context context,IReadView readView){
        super();
        this.readView = readView;
        this.mContext = context;
        mSIDao = DBManager.getInstance(context).getDaoSession().getSettingsInfoDao();
        ModuleBus.getInstance().register(this);
        EventBus.getDefault().register(this);
    }

    public void RecycleViewInit(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReadRecylerAdapter(mContext);
        adapter.setDatas(ReadUtil.getRecordFiles(recordPath));
        recyclerView.setAdapter(adapter);
    }

    public void refreshData(){
        adapter.clear();
        adapter.setDatas(ReadUtil.getRecordFiles(recordPath));
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEventMainThread(ChangeSettingEvent evt){
        if(!evt.isCheck){
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }else {
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(1)).unique();
            if(si == null)
                si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }
        recordPath = si.getRecordPath();
    }

    public void Destroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ModuleBus.getInstance().unregister(this);
    }

    @Override
    public void changeSetting(boolean isCheck) {
        if(!isCheck){
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }else {
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(1)).unique();
            if(si == null)
                si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }
        recordPath = si.getRecordPath();
    }

    @ModuleEvent(coreClientClass = IReadClient.class)
    public void changeSettingClient(boolean isCheck){
        if(!isCheck){
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }else {
            si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(1)).unique();
            if(si == null)
                si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        }
        recordPath = si.getRecordPath();
    }
}
