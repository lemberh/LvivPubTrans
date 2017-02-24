package org.rnaz.lvivpubtrans.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;

import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.mappers.RouteModelMapper;
import org.rnaz.lvivpubtrans.model.realm.RealmRouteModel;
import org.rnaz.lvivpubtrans.service.DataDownloadService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RoutesLoader extends AsyncTaskLoader<List<IRouteModel>> {

    private final LocalBroadcastManager broadcastManager;
    List<IRouteModel> models;

    public RoutesLoader(Context context) {
        super(context);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        broadcastManager.registerReceiver(
                receiver, new IntentFilter(DataDownloadService.LOAD_DATA_FINISHED_ACTION));
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.delete(RealmRouteModel.class);
//        realm.commitTransaction();
//        realm.close();
    }

    @Override
    protected void onStartLoading() {
        if (models == null || models.isEmpty() || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(models);
        }
    }

    @Override
    public List<IRouteModel> loadInBackground() {
        models = new ArrayList<>();
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();
        List<RealmRouteModel> rdbModel = realm.where(RealmRouteModel.class).findAll();
        if (rdbModel.isEmpty()) {
            getContext().startService(DataDownloadService.newIntent(getContext()));
        }
        rdbModel = realm.where(RealmRouteModel.class).findAll();
        for (RealmRouteModel model :
                rdbModel) {
            models.add(RouteModelMapper.toLocalObject(model));
        }
        realm.close();
        return models;
    }

    @Override
    protected void onReset() {
        broadcastManager.unregisterReceiver(receiver);
        models = null;
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            onContentChanged();
        }
    };

}
