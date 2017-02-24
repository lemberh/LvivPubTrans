package org.rnaz.lvivpubtrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Roman on 1/26/2017.
 */

public class PathPoint implements IPathModel {

    @SerializedName("X")
    @Expose
    private Double X;
    @SerializedName("Y")
    @Expose
    private Double Y;

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }
}
