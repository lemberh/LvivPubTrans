package org.rnaz.lvivpubtrans;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.google.firebase.crash.FirebaseCrash;

import org.rnaz.lvivpubtrans.model.IPathModel;
import org.rnaz.lvivpubtrans.model.IStopModel;
import org.rnaz.lvivpubtrans.model.MapModel;
import org.rnaz.lvivpubtrans.model.VehicleCoordinatesModel;
import org.rnaz.lvivpubtrans.model.realm.RealmRouteModel;
import org.rnaz.lvivpubtrans.service.RestAPI;
import org.rnaz.lvivpubtrans.utils.MapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

import static org.rnaz.lvivpubtrans.utils.MapUtils.convertToGMapCoordinates;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String MAP_MODEL_KEY = "MAP_MODEL_KEY";
    private static final String ROUTE_CODE_KEY = "ROUTE_CODE_KEY";
    public static final int VEHICLE_MARKER_IC_SIZE = 100;
    private String routeCode;

    public static Intent getIntent(Context context, String routeCode) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(ROUTE_CODE_KEY, routeCode);
        return intent;
    }

    private GoogleMap mMap;
    private MapModel mapModel;//TODO change
    private RealmRouteModel routeModel;
    private MapUtils.MapFrameCalculator mapFrameCalculator;
    private List<Marker> vehicles = new ArrayList<>();

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("MapsActivity created");
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState != null) {
            mapModel = savedInstanceState.getParcelable(MAP_MODEL_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MAP_MODEL_KEY, mapModel);
        super.onSaveInstanceState(outState);
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
        Log.i(MapsActivity.class.getName(), "onMAp Ready");
        loadVehicleMarkerIc();
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.i(MapsActivity.class.getName(), "onMapLoaded");
                if (mapModel == null) {
                    final ProgressDialog dialog = ProgressDialog.show(MapsActivity.this, "Loading ...", null, true, false);
                    routeCode = getIntent().getExtras().getString(ROUTE_CODE_KEY);

                    Realm realm = Realm.getDefaultInstance();
                    routeModel = realm.where(RealmRouteModel.class).
                            equalTo(RealmRouteModel.CODE_FIELD_NAME, routeCode).findFirstAsync();
                    routeModel.addChangeListener(new RealmChangeListener<RealmModel>() {
                        @Override
                        public void onChange(RealmModel element) {
                            initFromModel(routeModel);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Realm realm = Realm.getDefaultInstance();
//                    final RealmRouteModel routeModel = realm.where(RealmRouteModel.class).
//                            equalTo(RealmRouteModel.CODE_FIELD_NAME, routeCode).findFirst();
//
////                        mapModel = MapModel.newInstance();
////                        mapModel.addItem(new MapModel.Item(routeModel, routeModel.getStops(), routeModel.getPath()));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            initFromModel(routeModel);
//                            dialog.dismiss();
//                        }
//                    });
////                    realm.close();
//
//                }
//            }).start();
//    } else
//
//    {
////            initFromModel();
//    }

}

    public void initFromModel(RealmRouteModel model) {
        mapFrameCalculator = MapUtils.getMapFrameCalculator();
        if (!model.getStops().isEmpty()) {
            addStopsOnMap(model.getStops());
        }
        if (!model.getPath().isEmpty()) {
            addRoutePathOnMap(model.getPath());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapFrameCalculator.createBounds(), 50));
        executorService.scheduleAtFixedRate(updateVehiclePosTask, 0, 5, TimeUnit.SECONDS);
    }

//    public void initFromModel() {
//        mapFrameCalculator = MapUtils.getMapFrameCalculator();
//        for (MapModel.Item item :
//                mapModel.getItems()) {
//            if (!item.getStops().isEmpty()) {
//                addStopsOnMap(item.getStops());
//            }
//            if (!item.getRoutePath().isEmpty()) {
//                addRoutePathOnMap(item.getRoutePath());
//            }
//        }
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapFrameCalculator.createBounds(), 50));
//        executorService.scheduleAtFixedRate(updateVehiclePosTask, 0, 5, TimeUnit.SECONDS);
//    }

    private void addRoutePathOnMap(List<? extends IPathModel> routePath) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.DKGRAY);
        polylineOptions.width(10);
        polylineOptions.addAll(convertToGMapCoordinates(routePath));
        mMap.addPolyline(polylineOptions);
        for (IPathModel point : routePath) {
            mapFrameCalculator.addPoint(point.getY(), point.getX());
        }
    }

    private void addStopsOnMap(List<? extends IStopModel> stops) {
        for (IStopModel stopModel : stops) {
            LatLng latLng = new LatLng(stopModel.getY(), stopModel.getX());
            mMap.addMarker(new MarkerOptions().position(latLng).title(stopModel.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop)));
            mapFrameCalculator.addPoint(latLng);
        }
    }

    private void addVehiclesOnMap(List<VehicleCoordinatesModel> coordinates) {
        if (executorService.isShutdown()) {
            return;
        }
        for (Marker marker :
                vehicles) {
            marker.remove();
        }
        vehicles.clear();
        for (VehicleCoordinatesModel vehicle : coordinates) {
            vehicles.add(addVehicleMarker(vehicle));
//            vehicles.add(mMap.addMarker(new MarkerOptions().position(new LatLng(vehicle.getY(),vehicle.getX()))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_outline))));
        }
    }

    private Marker addVehicleMarker(VehicleCoordinatesModel vehicle) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(VEHICLE_MARKER_IC_SIZE, VEHICLE_MARKER_IC_SIZE, conf);
        Canvas canvas = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint paint = new Paint();
        paint.setTextSize(35);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

// modify canvas
        canvas.save();
        float courseDirection = vehicle.getAngle().floatValue() * -1 - 90;
        canvas.rotate(courseDirection, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawBitmap(vehicleMarkerIc, 0, 0, paint);
        canvas.restore();
//        canvas.drawText("User Name!", 30, 40, color);

// add marker to Map
        return mMap.addMarker(new MarkerOptions().position(new LatLng(vehicle.getY(), vehicle.getX()))
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 0.5f)
                .zIndex(2));
    }

    private Bitmap vehicleMarkerIc;

    private void loadVehicleMarkerIc() {
        vehicleMarkerIc = BitmapFactory.decodeResource(getResources(), R.drawable.marker_outline);
        vehicleMarkerIc = Bitmap.createScaledBitmap(vehicleMarkerIc, VEHICLE_MARKER_IC_SIZE, VEHICLE_MARKER_IC_SIZE, true);
    }

    private Runnable updateVehiclePosTask = new Runnable() {

        @Override
        public void run() {
            final List<VehicleCoordinatesModel> coordinates = new ArrayList<>();//TODO new model
//            for (MapModel.Item item :
//                    mapModel.getItems()) {
//                if (item.getRouteModel() == null) {
//                    continue;
//                }
            try {
                Log.i("RouteMonitoringByCode", "start loading vehicles coordinates " + routeCode);
                List<VehicleCoordinatesModel> c = RestAPI.getAPI()
                        .getRouteMonitoringByCode(routeCode).execute().body();
                Log.i("RouteMonitoringByCode", "size " + c.size());
                Log.i("RouteMonitoringByCode", c.toString());
                coordinates.addAll(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addVehiclesOnMap(coordinates);
                }
            });
        }
    };
}
