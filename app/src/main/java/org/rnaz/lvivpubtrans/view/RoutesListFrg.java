package org.rnaz.lvivpubtrans.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.rnaz.lvivpubtrans.MainActivity;
import org.rnaz.lvivpubtrans.MapsActivity;
import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.controller.RoutesAdapter;
import org.rnaz.lvivpubtrans.loaders.RoutesLoader;
import org.rnaz.lvivpubtrans.model.MapModel;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.service.RestAPI;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Roman on 1/25/2017.
 */

public class RoutesListFrg extends Fragment {

    public interface IOnItemSelected {
        void onSelected(RouteModel model);
    }

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.routes_list_frg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MapsActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(new RoutesAdapter(new IOnItemSelected() {
            @Override
            public void onSelected(final RouteModel model) {
//                ((MainActivity)getActivity()).showStopsList(model);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

                        try {
                            Response<String> response = RestAPI.getAPI().getStopsByRouteId(model.getCode()).execute();
                            List<StopModel> modelsS = Arrays.asList(gson.fromJson(response.body(), StopModel[].class));

                            response = RestAPI.getAPI().getRoutePathRaw(model.getCode()).execute();
                            List<PathPoint> modelsP = Arrays.asList(gson.fromJson(response.body(), PathPoint[].class));

                            final MapModel mapModel = MapModel.newInstance();
                            mapModel.addItem(new MapModel.Item(model, modelsS, modelsP));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    startActivity(MapsActivity.getIntent(getActivity(), mapModel));
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        }));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<List<RouteModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<RouteModel>>() {
        @Override
        public Loader<List<RouteModel>> onCreateLoader(int id, Bundle args) {
            return new RoutesLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<List<RouteModel>> loader, List<RouteModel> data) {
            ((RoutesAdapter) mRecyclerView.getAdapter()).setModels(data);
        }

        @Override
        public void onLoaderReset(Loader<List<RouteModel>> loader) {
            ((RoutesAdapter) mRecyclerView.getAdapter()).setModels(null);
        }
    };

}
