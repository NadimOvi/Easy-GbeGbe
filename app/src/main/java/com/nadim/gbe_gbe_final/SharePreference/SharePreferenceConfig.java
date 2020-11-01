package com.nadim.gbe_gbe_final.SharePreference;

import android.content.Context;
import android.content.SharedPreferences;


import com.nadim.gbe_gbe_final.R;

public class SharePreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharePreferenceConfig(Context context) {

        this.context = context;
        sharedPreferences= context.getSharedPreferences(context.getResources().getString(R.string.login_preference),Context.MODE_PRIVATE);

    }

    public void writeLoginStatus(boolean status){

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preference),status);
        editor.commit();
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status= sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference),false);
        return status;
    }
}
