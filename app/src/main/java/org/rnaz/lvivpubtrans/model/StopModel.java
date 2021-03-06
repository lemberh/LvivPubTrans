package org.rnaz.lvivpubtrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopModel implements IStopModel{

    @SerializedName("X")
    @Expose
    private Double x;
    @SerializedName("Y")
    @Expose
    private Double y;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ComplexEditor")
    @Expose
    private Object complexEditor;

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

    public Object getComplexEditor() {
        return complexEditor;
    }

    public void setComplexEditor(Object complexEditor) {
        this.complexEditor = complexEditor;
    }

    @Override
    public String toString() {
        return "Code: " + code +
                " Name: " + name +
                " Id: " + id +
                " X:" + x + " Y:" + y;
    }
}


