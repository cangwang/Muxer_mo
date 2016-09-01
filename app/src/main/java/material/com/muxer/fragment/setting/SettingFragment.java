package material.com.muxer.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import material.com.muxer.R;

/**
 * Created by air on 16/7/6.
 */
public class SettingFragment extends Fragment implements ISettingsView{
    private Context mContext;
    private SwitchCompat recordSwitch;
    private RelativeLayout settingDetailLayout;
    private TextInputEditText recordWidth;
    private TextInputEditText recordHeight;
    private TextInputEditText recordDensity;

    private SettingPresenter settingPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout,container,false);
        mContext = view.getContext();
        initUI(view);
        settingPresenter = new SettingPresenter(this);
        return view;
    }

    public void initUI(View view){
        recordSwitch = (SwitchCompat) view.findViewById(R.id.set_default);
        settingDetailLayout =(RelativeLayout)view.findViewById(R.id.record_detail);
        recordWidth = (TextInputEditText)view.findViewById(R.id.record_width);
        recordHeight = (TextInputEditText)view.findViewById(R.id.record_height);
        recordDensity = (TextInputEditText)view.findViewById(R.id.record_density);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
