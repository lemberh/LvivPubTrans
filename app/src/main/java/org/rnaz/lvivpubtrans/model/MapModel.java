package org.rnaz.lvivpubtrans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Roman on 1/26/2017.
 */

public class MapModel implements Parcelable{

    public static MapModel newInstance(){
        return new MapModel();
    }

    List<Item> items = new ArrayList<>();

    private MapModel() {}

    protected MapModel(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<MapModel> CREATOR = new Creator<MapModel>() {
        @Override
        public MapModel createFromParcel(Parcel in) {
            return new MapModel(in);
        }

        @Override
        public MapModel[] newArray(int size) {
            return new MapModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(items);
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public static class Item implements Parcelable{
        IRouteModel routeModel;
        List<IStopModel> stops;
        List<IPathModel> routePath;

        public Item(IRouteModel routeModel) {

        }

        public Item(IRouteModel routeModel, List<IStopModel> stops, List<IPathModel> routePath) {
            this.routeModel = routeModel;
            this.stops = stops;
            this.routePath = routePath;
        }

        Item(Parcel in) {
            Gson gson = new Gson();
            routeModel = gson.fromJson(in.readString(),RouteModel.class);
            stops = Arrays.asList(gson.fromJson(in.readString(),IStopModel[].class));
            routePath = Arrays.asList(gson.fromJson(in.readString(),IPathModel[].class));
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            Gson gson = new Gson();
            parcel.writeString(gson.toJson(routeModel,RouteModel.class));
            parcel.writeString(gson.toJson(stops.toArray(),StopModel[].class));
            parcel.writeString(gson.toJson(routePath.toArray(),PathPoint[].class));
        }

        public IRouteModel getRouteModel() {
            return routeModel;
        }

        public List<IStopModel> getStops() {
            return stops;
        }

        public List<IPathModel> getRoutePath() {
            return routePath;
        }
    }
}
