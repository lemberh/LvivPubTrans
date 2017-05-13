package org.rnaz.lvivpubtrans.model.realm;

import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.RouteModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRouteModel extends RealmObject implements IRouteModel {
    public static final String CODE_FIELD_NAME = "code";
    public static final String NAME_FIELD_NAME = "name";
    public static final String ID_FIELD_NAME = "id";

    @RouteModel.TransportType
    private Integer type;
    private String code;
    private String name;
    @PrimaryKey
    private Integer id;
    private RealmList<RealmPathModel> path;
    private RealmList<RealmStopModel> stops;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override public Integer getColor() {
        return null;
    }

    @Override public void setColor(Integer color) {

    }

    @Override public boolean isFavorite() {
        return false;
    }

    public RealmList<RealmPathModel> getPath() {
        return path;
    }

    public void setPath(RealmList<RealmPathModel> path) {
        this.path = path;
    }

    public RealmList<RealmStopModel> getStops() {
        return stops;
    }

    public void setStops(RealmList<RealmStopModel> stops) {
        this.stops = stops;
    }

    @RouteModel.TransportType
    public Integer getType() {
        return type;
    }

    public void setType(@RouteModel.TransportType Integer type) {
        this.type = type;
    }
}
