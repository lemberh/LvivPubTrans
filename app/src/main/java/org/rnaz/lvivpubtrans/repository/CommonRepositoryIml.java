package org.rnaz.lvivpubtrans.repository;

import android.content.SharedPreferences;

/**
 * Created by Roman on 2017-03-20.
 */

public class CommonRepositoryIml implements CommonRepository {

    public static final String IS_UPDATE_NEEDED = "IS_UPDATE_NEEDED";
    private SharedPreferences sharedPreferences;

    public CommonRepositoryIml(SharedPreferences sharedPreferences) {this.sharedPreferences = sharedPreferences;}

    @Override public boolean isDataUpdateNeeded() {
        return sharedPreferences.getBoolean(IS_UPDATE_NEEDED,true);
    }

    @Override public void setDataUpdateNeeded(boolean isNeede) {
        sharedPreferences.edit().putBoolean(IS_UPDATE_NEEDED,isNeede).apply();
    }
}
