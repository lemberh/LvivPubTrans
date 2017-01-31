package org.rnaz.lvivpubtrans.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.rnaz.lvivpubtrans.controller.RoutesAdapter;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.service.RestAPI;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Roman on 1/26/2017.
 */

public class RoutesLoader extends AsyncTaskLoader<List<RouteModel>> {

    List<RouteModel> models;

    public RoutesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (models == null || takeContentChanged()){
            forceLoad();
        }else {
            deliverResult(models);
        }
    }

    @Override
    public List<RouteModel> loadInBackground() {
        Response<String> response;
        try {
            response = RestAPI.getAPI().getRoutesRaw().execute();
            System.out.println(response.body());
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            models = Arrays.asList(gson.fromJson(response.body(), RouteModel[].class));
            System.out.println(models);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return models;
    }

    @Override
    protected void onReset() {
        models = null;
    }
}
