package cangwang.com.record_setting.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cangwang.com.base.basecomponent.BaseFragment;
import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.record_setting.R;

/**
 * Created by air on 16/7/6.
 */
public class SettingFragment extends BaseFragment implements ISettingsView{
    private Context mContext;
    private SwitchCompat recordSwitch;
    private RelativeLayout settingDetailLayout;
    private TextInputEditText recordWidth;
    private TextInputEditText recordHeight;
    private TextInputEditText recordDensity;
    private TextView recordDir;
    private Button chooseDirBtn;
    private Button confirmBtn;

    private SettingPresenter settingPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout,container,false);
        mContext = view.getContext();
        initUI(view);
        if(settingPresenter == null)
            settingPresenter = new SettingPresenter(getContext(),this);
        return view;
    }

    public void initUI(View view){
        recordSwitch = (SwitchCompat) view.findViewById(R.id.set_default);
        recordSwitch.setChecked(true);
        recordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingPresenter.setSWitchCheck(isChecked);
            }
        });
        settingDetailLayout = (RelativeLayout)view.findViewById(R.id.record_detail);
        settingDetailLayout.setVisibility(View.GONE);

        recordWidth = (TextInputEditText)view.findViewById(R.id.record_width);
        recordWidth.addTextChangedListener(new SettingPresenter.WidthTextWatcher());
        recordHeight = (TextInputEditText)view.findViewById(R.id.record_height);
        recordHeight.addTextChangedListener(new SettingPresenter.HeightTextWatcher());
        recordDensity = (TextInputEditText)view.findViewById(R.id.record_density);
        recordHeight.addTextChangedListener(new SettingPresenter.DensityTextWatcher());

        recordDir = (TextView) view.findViewById(R.id.record_dir);
        recordDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPresenter.chooseRecordDir(recordDir.getText().toString());
            }
        });
        chooseDirBtn = (Button) view.findViewById(R.id.record_dir_choose);
        chooseDirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPresenter.chooseRecordDir(recordDir.getText().toString());
            }
        });

        confirmBtn = (Button)view.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingPresenter.confirmCustomSettings();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        settingPresenter.Destroy();
        super.onDestroyView();
    }

    @Override
    public String getRecordHeight() {
        return recordHeight.getText().toString();
    }

    @Override
    public void setRecordHeigth(String height) {
        recordHeight.setText(height);
    }

    @Override
    public String getRecordWidth() {
        return recordWidth.getText().toString();
    }

    @Override
    public void setRecordWidth(String width) {
        recordWidth.setText(width);
    }

    @Override
    public String getRecordDensity() {
        return recordDensity.getText().toString();
    }

    @Override
    public void setRecordDesnsity(String desnsity) {
        recordDensity.setText(desnsity);
    }

    @Override
    public boolean getIfDfSettings() {
        return recordSwitch.isChecked();
    }

    @Override
    public void setCustomSettingsVisible(boolean isVisible) {
        if(isVisible)
            settingDetailLayout.setVisibility(View.VISIBLE);
        else
            settingDetailLayout.setVisibility(View.GONE);
    }

    @Override
    public void setRecordDir(String dir) {
        recordDir.setText(dir);
    }

    @Override
    public String getRecordDir() {
        return recordDir.getText().toString();
    }

    @Override
    public BasePresenter getPresenter() {
        return settingPresenter;
    }
}
