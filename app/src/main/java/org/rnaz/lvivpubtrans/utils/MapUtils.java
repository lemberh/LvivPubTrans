package org.rnaz.lvivpubtrans.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.rnaz.lvivpubtrans.model.IPathModel;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.realm.RealmPathModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 1/26/2017.
 */

public class MapUtils {

    public static List<LatLng> convertToGMapCoordinates(List<? extends IPathModel> routePath){
        List<LatLng> res = new ArrayList<>(routePath.size());
        for (IPathModel point :
                routePath) {
            res.add(new LatLng(point.getY(), point.getX()));
        }
        return res;
    }

    public static MapFrameCalculator getMapFrameCalculator(){
        return new MapFrameCalculator();
    }

    public static class MapFrameCalculator {
        private static final int ABS_MAX_LATITUDE = 85;
        private static final int ABS_MIN_LATITUDE = -85;
        private static final int ABS_MAX_LONGITUDE = 180;
        private static final int ABS_MIN_LONGITUDE = -180;

        double maxLat = ABS_MIN_LATITUDE;
        double minLat = ABS_MAX_LATITUDE;
        double maxLng = ABS_MIN_LONGITUDE;
        double minLng = ABS_MAX_LONGITUDE;

        private MapFrameCalculator (){}

        public void addPoint (double latitude, double longitude){
            maxLat = Math.max(latitude,maxLat);
            minLat = Math.min(latitude,minLat);
            maxLng = Math.max(longitude,maxLng);
            minLng = Math.min(longitude,minLng);
        }

        public void addPoint (LatLng latLng){
            addPoint(latLng.latitude,latLng.longitude);
        }

        public LatLngBounds createBounds(){
            return new LatLngBounds(new LatLng(minLat,minLng), new LatLng(maxLat,maxLng));
        }

        @Override
        public String toString() {
            return "MapFrameCalculator{" +
                    "maxLat=" + maxLat +
                    ", minLat=" + minLat +
                    ", maxLng=" + maxLng +
                    ", minLng=" + minLng +
                    '}';
        }
    }

}
