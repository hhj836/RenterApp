package com.ickkey.dztenant.fragment.home;

import com.ickkey.dztenant.R;
import com.ickkey.dztenant.base.BaseFragment;

/**
 * Created by hhj on 2017/7/27.
 */

public class HomePersonalFragment extends BaseFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fm_home_personal;
    }

    @Override
    public void initView() {
          setTitleGone();
    }
}
