package org.rnaz.lvivpubtrans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.view.RoutesListFrg;
import org.rnaz.lvivpubtrans.view.StopsListFrg;

public class MainActivity extends AppCompatActivity implements IActCallbacks {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showRoutesList();
        }
    }

    @Override
    public void showRoutesList() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new RoutesListFrg()).commit();
    }

    @Override
    public void showStopsList(RouteModel route) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, StopsListFrg.newInstance(route))
                .addToBackStack(null).commit();
    }
}
