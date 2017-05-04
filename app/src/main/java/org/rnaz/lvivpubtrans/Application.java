package org.rnaz.lvivpubtrans;

import android.content.Context;

import org.rnaz.lvivpubtrans.repository.CommonRepository;
import org.rnaz.lvivpubtrans.repository.CommonRepositoryIml;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Roman on 2/6/2017.
 */

public class Application extends android.app.Application {

    public static final String PREFS_NAME = "local";
    private static Application instance;
    private CommonRepository commonRepository;

    public static Application getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Initialize Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("main-db")
                .build();
        Realm.setDefaultConfiguration(config);
        initHelpers();

        commonRepository.setDataUpdateNeeded(true);
//        Realm.getDefaultInstance().beginTransaction();
//        Realm.getDefaultInstance().deleteAll();
//        Realm.getDefaultInstance().commitTransaction();

    }

    private void initHelpers(){
        commonRepository = new CommonRepositoryIml(getSharedPreferences(PREFS_NAME,MODE_PRIVATE));
    }

    public CommonRepository getCommonRepository() {
        return commonRepository;
    }
}
