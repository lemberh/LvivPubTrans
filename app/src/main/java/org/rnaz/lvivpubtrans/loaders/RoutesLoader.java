package org.rnaz.lvivpubtrans.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.model.mappers.RouteModelMapper;
import org.rnaz.lvivpubtrans.model.realm.RealmPathModel;
import org.rnaz.lvivpubtrans.model.realm.RealmRouteModel;
import org.rnaz.lvivpubtrans.model.realm.RealmStopModel;
import org.rnaz.lvivpubtrans.service.RestAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Roman on 1/26/2017.
 */

public class RoutesLoader extends AsyncTaskLoader<List<IRouteModel>> {

    List<IRouteModel> models = new ArrayList<>();

    public RoutesLoader(Context context) {
        super(context);
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.delete(RealmRouteModel.class);
//        realm.commitTransaction();
//        realm.close();
    }

    @Override
    protected void onStartLoading() {
        if (models.isEmpty() || takeContentChanged()) {
            forceLoad();
        } else {
            deliverResult(models);
        }
    }

    @Override
    public List<IRouteModel> loadInBackground() {
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();
        List<RealmRouteModel> rdbModel = realm.where(RealmRouteModel.class).findAll();
        if (rdbModel.isEmpty()) {
            try {
                List<RouteModel> cloudModel = RestAPI.getAPI().getRoutes().execute().body();
                realm.beginTransaction();
                for (RouteModel model :
                        cloudModel) {
                    RealmRouteModel routeModel = realm
                            .copyToRealmOrUpdate(RouteModelMapper.toRealmObject(model));
                    loadRouteData(routeModel);
                }
                realm.commitTransaction();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
        models = null;
    }

    private void loadRouteData(RealmRouteModel routeModel) {
        try {
            Realm realm = Realm.getDefaultInstance();
            if (!realm.isInTransaction()){
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
