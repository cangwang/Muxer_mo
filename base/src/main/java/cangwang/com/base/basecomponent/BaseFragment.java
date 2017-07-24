package cangwang.com.base.basecomponent;

import android.support.v4.app.Fragment;

/**
 * Created by air on 16/10/19.
 */

public abstract class BaseFragment extends Fragment {
    public abstract BasePresenter getPresenter();
}
