package material.com.muxer.activity.presenter;

import android.content.Context;
import android.view.Menu;
import android.view.ViewGroup;

import material.com.muxer.activity.view.IScreenRecordView;
import material.com.muxer.activity.view.ScreenRecordView;

/**
 * Created by air on 16/8/11.
 */
public class ScreenRecordPresenter {
    private IScreenRecordView screenRecordView;

    public ScreenRecordPresenter(Context context,ViewGroup view){
        screenRecordView = new ScreenRecordView(context,view);
    }

    public void setToolbarMenu(Menu menu){
//        screenRecordView.setToolbarMenu(menu);
    }
}
