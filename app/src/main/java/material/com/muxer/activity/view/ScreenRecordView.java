package material.com.muxer.activity.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import material.com.muxer.R;
import material.com.muxer.adapter.RecordPagerAdapter;
import material.com.muxer.config.PageConfig;
import material.com.muxer.fragment.read.ReadFragment;
import material.com.muxer.fragment.record.RecordFragment;
import material.com.muxer.fragment.setting.SettingFragment;

/**
 * Created by air on 16/8/11.
 */
public class ScreenRecordView implements IScreenRecordView{
    private View screenRecordView;
    private Context mContext ;

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<String> pageTitles = new ArrayList<String>();
    private List<Fragment> pageFagments = new ArrayList<Fragment>();

    private RecordPagerAdapter recordPagerAdapter;

    public ScreenRecordView(Context context, ViewGroup parent){
        mContext = context;
        screenRecordView = LayoutInflater.from(context).inflate(R.layout.activity_main,parent);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle(context.getString(R.string.app_name));
        ((AppCompatActivity)context).setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);

        mTabLayout = (TabLayout) findViewById(R.id.record_tab_layout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        pageTitles = PageConfig.getPageTitles(context);

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
        recordPagerAdapter = new RecordPagerAdapter(((AppCompatActivity)context).getSupportFragmentManager(),pageFagments,pageTitles);
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

    private View findViewById(int id){
        if(screenRecordView !=null){
            return screenRecordView.findViewById(id);
        }
        return null;
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_settings:
                    break;
                case R.id.action_record:
                    break;
                case R.id.action_read:
            }
            return true;
        }
    };


    public void setToolbarMenu(Menu menu) {
        ((AppCompatActivity)mContext).getMenuInflater().inflate(R.menu.menu_main,menu);
    }
}
