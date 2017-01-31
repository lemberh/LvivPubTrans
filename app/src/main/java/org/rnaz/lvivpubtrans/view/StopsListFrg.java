package org.rnaz.lvivpubtrans.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.rnaz.lvivpubtrans.R;
import org.rnaz.lvivpubtrans.controller.StopsAdapter;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.service.RestAPI;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Roman on 1/25/2017.
 */

public class StopsListFrg extends Fragment{
    private static final String ROUTE_KEY = "ROUTE_KEY";

    private RecyclerView mRecyclerView;
    private List<StopModel> stops;

    public static Fragment newInstance(RouteModel route){
        Fragment fragment = new StopsListFrg();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROUTE_KEY,route);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){

            RouteModel model = (RouteModel) args.getSerializable(ROUTE_KEY);
            RestAPI.getAPI().getStopsByRouteId(model.getCode()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Gson gson = new Gson();
                    StopModel[] models = gson.fromJson(response.body(), StopModel[].class);
                    ((StopsAdapter)mRecyclerView.getAdapter()).setModels(Arrays.asList(models));
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {}
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stops_list_frg,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(new StopsAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }
}
