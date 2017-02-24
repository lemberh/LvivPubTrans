package org.rnaz.lvivpubtrans.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rnaz.lvivpubtrans.MapsActivity;
import org.rnaz.lvivpubtrans.controller.RoutesAdapter;
import org.rnaz.lvivpubtrans.databinding.RoutesListFrgBinding;
import org.rnaz.lvivpubtrans.loaders.RoutesLoader;
import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.service.DataDownloadService;

import java.util.List;

/**
 * Created by Roman on 1/25/2017.
 */

public class RoutesListFrg extends Fragment {

    public interface IOnItemSelected {
        void onSelected(IRouteModel model);
    }

    private RoutesListFrgBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RoutesListFrgBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MapsActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        binding.recycler.setAdapter(new RoutesAdapter(new IOnItemSelected() {
            @Override
            public void onSelected(final IRouteModel model) {
//                ((MainActivity)getActivity()).showStopsList(model);
                startActivity(MapsActivity.getIntent(getActivity(), model.getCode()));
            }
        }));
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getLoaderManager().initLoader(0, null, loaderCallbacks);
    }

    @Override public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                receiver, new IntentFilter(DataDownloadService.LOAD_DATA_PROGRESS_ACTION));
    }

    @Override public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override public void onReceive(Context context, Intent intent) {
            binding.progressContainer.setVisibility(View.VISIBLE);
            binding.loadingText.setText(intent.getStringExtra(DataDownloadService.EXTRA_KEY_OUT_OF));
        }
    };

    private LoaderManager.LoaderCallbacks<List<IRouteModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<IRouteModel>>() {
        @Override
        public Loader<List<IRouteModel>> onCreateLoader(int id, Bundle args) {
            return new RoutesLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<List<IRouteModel>> loader, List<IRouteModel> data) {
            ((RoutesAdapter) binding.recycler.getAdapter()).setModels(data);
            binding.progressContainer.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<List<IRouteModel>> loader) {
            ((RoutesAdapter) binding.recycler.getAdapter()).setModels(null);
        }
    };

}
