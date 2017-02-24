package org.rnaz.lvivpubtrans.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.rnaz.lvivpubtrans.Application;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.model.mappers.RouteModelMapper;
import org.rnaz.lvivpubtrans.model.realm.RealmPathModel;
import org.rnaz.lvivpubtrans.model.realm.RealmRouteModel;
import org.rnaz.lvivpubtrans.model.realm.RealmStopModel;
import org.rnaz.lvivpubtrans.repository.CommonRepository;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class DataDownloadService extends IntentService {

    public static final String LOAD_DATA_ACTION = "LOAD_DATA_ACTION";
    public static final String LOAD_DATA_FINISHED_ACTION = "LOAD_DATA_FINISHED_ACTION";
    public static final String LOAD_DATA_PROGRESS_ACTION = "LOAD_DATA_PROGRESS_ACTION";
    public static final String EXTRA_KEY_PERCENTAGE = "EXTRA_KEY_PERCENTAGE";
    public static final String EXTRA_KEY_OUT_OF = "EXTRA_KEY_OUT_OF";
    private final LocalBroadcastManager broadcastManager;
    private final CommonRepository commonRepository;

    public static Intent newIntent(Context c){
        return new Intent(c,DataDownloadService.class);
    }

    public DataDownloadService() {
        super(DataDownloadService.class.getSimpleName());
        broadcastManager = LocalBroadcastManager.getInstance(this);
        commonRepository = Application.getInstance().getCommonRepository();
    }

    @Override protected void onHandleIntent(@Nullable Intent intent) {
        if (!commonRepository.isDataUpdateNeeded()){
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        try {
            List<RouteModel> cloudModels = RestAPI.getAPI().getRoutes().execute().body();
            realm.beginTransaction();
            for (RouteModel model :
                    cloudModels) {
                RealmRouteModel routeModel = realm
                        .copyToRealmOrUpdate(RouteModelMapper.toRealmObject(model));
                loadRouteData(routeModel);

                broadcastManager.sendBroadcast(getLoadingProgressIntent(cloudModels.size(),cloudModels.indexOf(model)));
            }
            realm.commitTransaction();
            broadcastManager.sendBroadcast(getLoadingProgressIntent(cloudModels.size(),cloudModels.size()));
            broadcastManager.sendBroadcast(new Intent(LOAD_DATA_FINISHED_ACTION));
            commonRepository.setDataUpdateNeeded(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent getLoadingProgressIntent(int size,int current){
        Intent i = new Intent(LOAD_DATA_PROGRESS_ACTION);
        i.putExtra(EXTRA_KEY_PERCENTAGE,String.valueOf((float) current/size * 100));
        i.putExtra(EXTRA_KEY_OUT_OF,current + "/" + size);
        return i;
    }

    private void loadRouteData(RealmRouteModel routeModel) {
        try {
            Realm realm = Realm.getDefaultInstance();
            if (!realm.isInTransaction()) {
                throw new RuntimeException("not in transaction");
            }
            List<StopModel> stopsRest = RestAPI.getAPI().getStopsByRouteId(routeModel.getCode()).execute().body();
            List<PathPoint> pathRest = RestAPI.getAPI().getRoutePath(routeModel.getCode()).execute().body();

            RealmList<RealmStopModel> stops = new RealmList<>();
            for (StopModel model :
                    stopsRest) {
                stops.add(realm.copyToRealmOrUpdate(RouteModelMapper.copyData(model, new RealmStopModel())));
            }

            RealmList<RealmPathModel> path = new RealmList<>();
            for (PathPoint model :
                    pathRest) {
                path.add(realm.copyToRealmOrUpdate(new RealmPathModel(model.getX(), model.getY())));
            }

            routeModel.setPath(path);
            routeModel.setStops(stops);
            realm.insertOrUpdate(routeModel);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
