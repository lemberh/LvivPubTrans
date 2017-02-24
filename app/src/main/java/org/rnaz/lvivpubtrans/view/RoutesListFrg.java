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

import org.rnaz.lvivpubtrans.MapsActivity;
import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.controller.RoutesAdapter;
import org.rnaz.lvivpubtrans.loaders.RoutesLoader;
import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.MapModel;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.service.RestAPI;

import java.io.IOException;
import java.util.List;

/**
 * Created by Roman on 1/25/2017.
 */

public class RoutesListFrg extends Fragment {

    public interface IOnItemSelected {
        void onSelected(IRouteModel model);
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
            public void onSelected(final IRouteModel model) {
//                ((MainActivity)getActivity()).showStopsList(model);
                startActivity(MapsActivity.getIntent(getActivity(), model.getCode()));
            }
        }));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<List<IRouteModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<IRouteModel>>() {
        @Override
        public Loader<List<IRouteModel>> onCreateLoader(int id, Bundle args) {
            return new RoutesLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<List<IRouteModel>> loader, List<IRouteModel> data) {
            ((RoutesAdapter) mRecyclerView.getAdapter()).setModels(data);
        }

        @Override
        public void onLoaderReset(Loader<List<IRouteModel>> loader) {
            ((RoutesAdapter) mRecyclerView.getAdapter()).setModels(null);
        }
    };

}
