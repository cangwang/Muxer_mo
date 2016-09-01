package material.com.muxer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import material.com.muxer.R;
import material.com.muxer.activity.view.IScreenRecordView;
import material.com.muxer.config.PageConfig;
import material.com.muxer.fragment.read.ReadFragment;
import material.com.muxer.fragment.record.RecordFragment;
import material.com.muxer.adapter.RecordPagerAdapter;
import material.com.muxer.fragment.setting.SettingFragment;
import material.com.muxer.receiver.MyBroadcastReceiver;
import material.com.muxer.service.ScreenRecorderService;

import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public final class ScreenRecordActivity extends AppCompatActivity implements IScreenRecordView{
    private static final boolean DEBUG = false;
    private static final String TAG = "ScreenRecordActivity";

    private MyBroadcastReceiver mReceiver;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<String> pageTitles = new ArrayList<String>();
    private List<Fragment> pageFagments = new ArrayList<Fragment>();

    private RecordPagerAdapter recordPagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.v(TAG, "onCreate:");
        setContentView(R.layout.activity_main);

        initView();

        if (mReceiver == null)
            mReceiver = new MyBroadcastReceiver(this);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ScreenRecorderService.ACTION_QUERY_STATUS_RESULT);
        registerReceiver(mReceiver, intentFilter);
    }

    public void initView(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);

        mTabLayout = (TabLayout) findViewById(R.id.record_tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        pageTitles = PageConfig.getPageTitles(this);

        for(String title:pageTitles){
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }

        Fragment tab0 = new RecordFragment();
        Fragment tab1 = new ReadFragment();
        Fragment tab2 = new SettingFragment();
        pageFagments.add(tab0);
        pageFagments.add(tab1);
        pageFagments.add(tab2);

        mViewPager = (ViewPager) findViewById(R.id.record_view_pager);
        recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(),pageFagments,pageTitles);
        mViewPager.setAdapter(recordPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentPage = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(currentPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(recordPagerAdapter);
    }



    public void updateRecording(boolean isRecording, boolean isPausing){
        for (Fragment fragment:pageFagments){
            if(fragment instanceof RecordFragment){
                ((RecordFragment) fragment).updateRecording(isRecording,isPausing);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_record:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.action_read:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.action_settings:
                    mViewPager.setCurrentItem(2);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.v(TAG, "onResume:");

    }

    @Override
    protected void onPause() {
        if (DEBUG) Log.v(TAG, "onPause:");

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
