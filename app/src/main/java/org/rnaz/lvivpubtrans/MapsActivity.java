package org.rnaz.lvivpubtrans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.rnaz.lvivpubtrans.model.MapModel;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.model.VehicleCoordinatesModel;
import org.rnaz.lvivpubtrans.service.RestAPI;
import org.rnaz.lvivpubtrans.utils.MapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.rnaz.lvivpubtrans.utils.MapUtils.convertToGMapCoordinates;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String MAP_MODEL_KEY = "MAP_MODEL_KEY";

    public static Intent getIntent(Context context,MapModel mapModel) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(MAP_MODEL_KEY,mapModel);
        return intent;
    }

    private GoogleMap mMap;
    private MapModel mapModel;
    private MapUtils.MapFrameCalculator mapFrameCalculator;
    private List<Marker> vehicles = new ArrayList<>();

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getExtras() != null){
            mapModel = getIntent().getExtras().getParcelable(MAP_MODEL_KEY);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        executorService.shutdown();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapFrameCalculator.createBounds(),50));
            }
        });
        initFromModel();
    }

    public void initFromModel(){
        mapFrameCalculator = MapUtils.getMapFrameCalculator();
        for (MapModel.Item item :
                mapModel.getItems()) {
//            if (!item.getStops().isEmpty()){
//                addStopsOnMap(item.getStops());
//            }
            if (!item.getRoutePath().isEmpty()){
                addRoutePathOnMap(item.getRoutePath());
            }
        }
        executorService.scheduleAtFixedRate(updateVehiclePosTask,0,5, TimeUnit.SECONDS);
    }

    private void addRoutePathOnMap(List<PathPoint> routePath) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.DKGRAY);
        polylineOptions.width(10);
        polylineOptions.addAll(convertToGMapCoordinates(routePath));
        mMap.addPolyline(polylineOptions);
        for (PathPoint point :
                routePath) {
            mapFrameCalculator.addPoint(point.getY(),point.getX());
        }
    }

    private void addStopsOnMap(List<StopModel> stops) {
        for (StopModel stopModel :
                stops) {
            LatLng latLng = new LatLng(stopModel.getY(),stopModel.getX());
            mMap.addMarker(new MarkerOptions().position(latLng).title(stopModel.getName()));
            mapFrameCalculator.addPoint(latLng);
        }
    }

    private void addVehiclesOnMap (List<VehicleCoordinatesModel> coordinates){
        if (executorService.isShutdown()){
            return;
        }
        for (Marker marker :
                vehicles) {
            marker.remove();
            vehicles.remove(marker);
        }
        for (VehicleCoordinatesModel vehicle :
                coordinates) {
            addVehicleMarker(vehicle);
//            vehicles.add(mMap.addMarker(new MarkerOptions().position(new LatLng(vehicle.getY(),vehicle.getX()))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_outline))));
        }
    }

    private void addVehicleMarker (VehicleCoordinatesModel vehicle){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(150, 150, conf);
        Canvas canvas = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLUE);

// modify canvas
        canvas.save();
        canvas.rotate(vehicle.getAngle().floatValue(),canvas.getWidth()/2,canvas.getHeight()/2);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.marker_outline), 0,0, color);
        canvas.restore();
//        canvas.drawText("User Name!", 30, 40, color);

// add marker to Map
        mMap.addMarker(new MarkerOptions().position(new LatLng(vehicle.getY(),vehicle.getX()))
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
    }

    private Runnable updateVehiclePosTask = new Runnable() {

        @Override
        public void run() {
            Gson gson = new Gson();
            final List<VehicleCoordinatesModel> coordinates = new ArrayList<>();
            for (MapModel.Item item:
                    mapModel.getItems()) {
                if (item.getRouteModel() == null){
                    continue;
                }
                try {
                    String body = RestAPI.getAPI().getRouteMonitoringByCode(item.getRouteModel().getCode()).execute().body();
                    VehicleCoordinatesModel[] c = gson.fromJson(body, VehicleCoordinatesModel[].class);
                    Log.i("RouteMonitoringByCode","size " + c.length);
                    Log.i("RouteMonitoringByCode",Arrays.toString(c));
                    coordinates.addAll(Arrays.asList(c));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addVehiclesOnMap(coordinates);
                }
            });
        }
    };
}
