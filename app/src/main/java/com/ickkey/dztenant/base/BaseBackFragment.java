package com.ickkey.dztenant.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ickkey.dztenant.R;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by hhj .
 */
public class BaseBackFragment extends SwipeBackFragment {
    private static final String TAG = "Fragmentation";
    public boolean  enableSwipeBack(){
        return  false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setParallaxOffset(0.5f);
        if(!enableSwipeBack()){
            getSwipeBackLayout().setEnableGesture(false);
        }
    }

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}
