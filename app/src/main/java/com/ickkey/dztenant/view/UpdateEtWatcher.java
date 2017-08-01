package com.ickkey.dztenant.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.ickkey.dztenant.R;

/**
 * Created by hhj on 2017/8/1.
 */

public class UpdateEtWatcher implements TextWatcher {
    private View updateView;
    public UpdateEtWatcher(View updateView){
        this.updateView=updateView;

    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(updateView!=null){
            if(editable.toString().trim().length()>0){
                updateView.setClickable(true);
                updateView.setBackgroundResource(R.drawable.update_btn_bg);
            }else {
                updateView.setClickable(false);
                updateView.setBackgroundResource(R.drawable.update_btn_bg_disable);
            }
        }

    }
}
