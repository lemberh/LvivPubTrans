package org.rnaz.lvivpubtrans.model.realm;

import org.rnaz.lvivpubtrans.model.IRouteModel;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Roman on 2/6/2017.
 */

public class RealmRouteModel extends RealmObject implements IRouteModel {
    public static final String CODE_FIELD_NAME  = "code";
    public static final String NAME_FIELD_NAME  = "name";
    public static final String ID_FIELD_NAME  = "id";

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
}
