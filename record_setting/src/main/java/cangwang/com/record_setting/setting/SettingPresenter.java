package cangwang.com.record_setting.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cangwang.com.base.adapter.RecordDirAdapter;
import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.base.greenDao.SettingsInfo;
import cangwang.com.base.greenDao.SettingsInfoDao;
import cangwang.com.base.modulebus.ELModuleApi;
import cangwang.com.base.modulebus.ELPublicApi;
import cangwang.com.base.modulebus.ELPublicHelper;
import cangwang.com.base.modulebus.IReadClient;
import cangwang.com.base.modulebus.ModuleBus;
import cangwang.com.base.service.ScreenRecorderService;
import cangwang.com.base.ui.RecyclerDialog;
import cangwang.com.base.utils.DBManager;
import cangwang.com.record_setting.R;
import event.ChangeSettingEvent;


/**
 * Created by air on 16/8/29.
 */

public class SettingPresenter extends BasePresenter{
    public static final String RECORD_HEIGHT = "RECORD_HEIGHT";
    public static final String RECORD_WIDTH = "RECORD_WIDTH";
    public static final String RECORD_DENSITY = "RECORD_DENSITY";
    public static final String RECORD_PATH = "RECORD_PATH";
    public static final String DEFAULT_PATH = "/sdcard/Movies/YYRecord";
    private ISettingsView settingsView;
    private Context mContext;
    private RecordDirAdapter adapter;

    private SettingsInfoDao mSIDao;
    DisplayMetrics metrics;
    SettingsInfo si;

    @Override
    public Map<Class<? extends ELModuleApi>, ELModuleApi> getModuleApi() {
        return null;
    }

    public SettingPresenter(ISettingsView view){
        settingsView = view;
    }

    public SettingPresenter(Context context, ISettingsView view){
        super();
        mContext = context;
        settingsView = view;
        metrics = mContext.getResources().getDisplayMetrics();
        mSIDao = DBManager.getInstance(context).getDaoSession().getSettingsInfoDao();
        si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).unique();
        if (si == null)
            mSIDao.insert(new SettingsInfo(0, metrics.widthPixels, metrics.heightPixels, metrics.densityDpi, DEFAULT_PATH));
    }

    public void setDefaultSettings(){
        if(!settingsView.getIfDfSettings()){
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            int density = metrics.densityDpi;
            Intent intent = new Intent(mContext, ScreenRecorderService.class);
            intent.putExtra(RECORD_HEIGHT,height);
            intent.putExtra(RECORD_WIDTH,width);
            intent.putExtra(RECORD_DENSITY,density);
            intent.setAction(ScreenRecorderService.ACTION_CUSTOM_SETTING);
            mContext.startService(intent);
        }
    }

    public void setSWitchCheck(boolean isChecked){
//        EventBus.getDefault().post(new ChangeSettingEvent(isChecked));
//        ELPublicHelper.getInstance().getModuleApi(ELPublicApi.ReadApi.class).changeSetting(isChecked);
        ModuleBus.getInstance().post(IReadClient.class,"changeSettingClient",isChecked);
        if(isChecked) {
            settingsView.setCustomSettingsVisible(false);
            setDefaultSettings();

            SettingsInfo si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(0)).build().unique();
            changeSettingIntent(mContext,0,si.getWidth(),si.getHeight(),si.getDensity(),si.getRecordPath());
        }
        else {
            settingsView.setCustomSettingsVisible(true);
//            settingsView.setRecordDir("/sdcard/Movies/YYRecord");

            SettingsInfo si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(1)).build().unique();
            if (si != null) {
                settingsView.setRecordWidth(String.valueOf(si.getWidth()));
                settingsView.setRecordHeigth(String.valueOf(si.getHeight()));
                settingsView.setRecordDesnsity(String.valueOf(si.getDensity()));
                settingsView.setRecordDir(si.getRecordPath());
            }
            else
                settingsView.setRecordDir(DEFAULT_PATH);

            confirmCustomSettings();
        }
    }


    public void confirmCustomSettings(){
        int height;
        int width;
        int density;
        String path;
        if(settingsView.getRecordHeight() == null || settingsView.getRecordHeight().equals(""))
            height = metrics.heightPixels;
        else
            height = Integer.parseInt(settingsView.getRecordHeight());
        if(settingsView.getRecordWidth() == null || settingsView.getRecordWidth().equals(""))
            width = metrics.widthPixels;
        else
            width = Integer.parseInt(settingsView.getRecordWidth());
        if(settingsView.getRecordDensity() == null || settingsView.getRecordDensity().equals(""))
            density = metrics.densityDpi;
        else
            density  = Integer.parseInt(settingsView.getRecordDensity());
        if(settingsView.getRecordDensity() == null || settingsView.getRecordDensity().equals(""))
            path = DEFAULT_PATH;
        else
            path = settingsView.getRecordDir();


        changeSettingIntent(mContext,1,width,height,density,path);

//        mSIDao = DBManager.getInstance(mContext).getDaoSession().getSettingsInfoDao();
//        SettingsInfo  si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.ge(1)).build().unique();
//        if(si == null) {
//            mSIDao.update(new SettingsInfo(1, width, height, density, path));
//        }else {
//            mSIDao.insert(new SettingsInfo(1, width, height, density, path));
//        }
//
//        Intent intent = new Intent(mContext, ScreenRecorderService.class);
//        intent.putExtra(RECORD_HEIGHT,height);
//        intent.putExtra(RECORD_WIDTH,width);
//        intent.putExtra(RECORD_DENSITY,density);
//        intent.putExtra(RECORD_PATH,path);
//        intent.setAction(ScreenRecorderService.ACTION_CUSTOM_SETTING);
//        mContext.startService(intent);
    }

    public void changeSettingIntent(Context context,int id,int width,int height, int density,String path){
        si = mSIDao.queryBuilder().where(SettingsInfoDao.Properties.Id.eq(id)).build().unique();
        if(si != null) {
            mSIDao.update(new SettingsInfo(id, width, height, density, path));
        }else {
            mSIDao.insert(new SettingsInfo(id, width, height, density, path));
        }

        Intent intent = new Intent(context, ScreenRecorderService.class);
        intent.putExtra(RECORD_HEIGHT,height);
        intent.putExtra(RECORD_WIDTH,width);
        intent.putExtra(RECORD_DENSITY,density);
        intent.putExtra(RECORD_PATH,path);
        intent.setAction(ScreenRecorderService.ACTION_CUSTOM_SETTING);
        context.startService(intent);
    }

    public void chooseRecordDir(String curPath){
        createRecyclerAlerDialog(curPath);
    }

    public void createRecyclerAlerDialog(String curPath){
        View dialog = LayoutInflater.from(mContext).inflate(R.layout.record_dir_item,null);
        RecyclerView recycler = (RecyclerView)dialog.findViewById(R.id.record_recyle_dir);
        LinearLayoutManager mLinearManager = new LinearLayoutManager(recycler.getContext());
        recycler.setLayoutManager(mLinearManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecordDirAdapter(mContext,curPath);
        recycler.setAdapter(adapter);

        final RecyclerDialog.Builder builder = new RecyclerDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.choose_dir_txt));
        builder.setView(dialog);
        builder.setPositiveButton(mContext.getResources().getString(R.string.setting_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                settingsView.setRecordDir(adapter.getChoosePath());
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.setting_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void createRecyclerDialog(String curPath){
        final RecyclerDialog confirmDialog = new RecyclerDialog(mContext, "选择录制保存的目录", "保存", "取消",curPath);
        confirmDialog.show();
        confirmDialog.setClicklistener(new RecyclerDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(String path) {
                settingsView.setRecordDir(path);
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
            }

            @Override
            public void doCancel() {
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
            }
        });
    }

    public void Destroy(){
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

//    void chooseDir(String curPath){
//        File f = new File(curPath);
//        File[] file = f.listFiles();
//        final List<Map<String, Object>> listItem = new ArrayList<>();
//
//        if(!curPath.equals("/")){//如果不是根目录的话就在要显示的列表中加入此项
//            Map map1=new HashMap();
//            map1.put("name", mContext.getResources().getString(R.string.setting_return_content));
//            map1.put("image", android.R.drawable.star_big_off);
//            map1.put("path",f.getParent());
//            map1.put("isDire", true);
//            listItem.add(map1);
//        }
//
//        if(file != null){//必须判断 否则目录为空的时候会报错
//            for(int i = 0; i < file.length; i++){
//                Map map=new HashMap();
//                map.put("name", file[i].getName());
//                map.put("image", (file[i].isDirectory()) ? android.R.drawable.star_big_on : android.R.drawable.star_on);
//                map.put("path",file[i].getPath());
//                map.put("isDire", file[i].isDirectory());
//                listItem.add(map);
//            }
//        }
//
//        SimpleAdapter adapter = new SimpleAdapter(mContext,listItem,R.layout.record_dir_adapter_layout,
//                new String[]{"name","image"},new int[]{R.id.dir_txt,R.id.dir_img});
//
//        final AlertDialog.Builder b = new AlertDialog.Builder(mContext);
//        b.setAdapter(adapter, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                if((Boolean) listItem.get(arg1).get("isDire")){
//                    chooseRecordDir((String)listItem.get(arg1).get("path"));
//                }else{
////                    fileDire.setText((String)listItem.get(arg1).get(path));
//                    settingsView.setRecordDir((String)listItem.get(arg1).get("path"));
//                }
//            }
//        });
//        b.show();
//    }

    static class WidthTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    static class HeightTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    static class DensityTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
