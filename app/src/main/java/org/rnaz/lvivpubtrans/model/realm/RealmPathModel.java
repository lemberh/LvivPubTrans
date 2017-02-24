package org.rnaz.lvivpubtrans.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import org.rnaz.lvivpubtrans.model.IPathModel;

import java.util.Locale;

/**
 * Created by Roman on 2/24/2017.
 */

public class RealmPathModel extends RealmObject implements IPathModel {
    private static final String X_FIELD_NAME  = "x";
    private static final String Y_FIELD_NAME  = "y";

    private Double x;
    private Double y;
    @PrimaryKey
    private String id;

    public RealmPathModel() {}
    public RealmPathModel(Double x, Double y) {
        this.x = x;
        this.y = y;
        setId(generateId());
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public void setX(Double x) {
        this.x = x;
        setId(generateId());
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public void setY(Double y) {
        this.y = y;
        setId(generateId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String generateId(){
        return String.format(Locale.getDefault(),"%f%f",x,y);
    }

}
