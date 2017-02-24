package org.rnaz.lvivpubtrans.model.realm;

import com.google.gson.annotations.SerializedName;

import org.rnaz.lvivpubtrans.model.IStopModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Roman on 2/15/2017.
 */

public class RealmStopModel extends RealmObject implements IStopModel {

    @SerializedName("X")
    private Double x;
    @SerializedName("Y")
    private Double y;
    @SerializedName("Code")
    private String code;
    @SerializedName("Name")
    private String name;
    @SerializedName("Id")
    @PrimaryKey
    private Integer id;

    public RealmStopModel(){}

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public void setX(Double x) {
        this.x = x;
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public void setY(Double y) {
        this.y = y;
    }

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
}
