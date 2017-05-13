
package org.rnaz.lvivpubtrans.model;


import android.support.annotation.IntDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RouteModel implements Serializable, IRouteModel{

    @SerializedName("Code")
    @Expose
    private String code;
    @Expose
    @TransportType
    private Integer type;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ComplexEditor")
    @Expose
    private Object complexEditor;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @TransportType
    public Integer getType() {
        return type;
    }

    public void setType(@TransportType Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name.startsWith("А")){
            type = BUS;
        }else if (name.startsWith("Тр")){
            type = TROLLEYBUS;
        }else if(name.startsWith("Т")){
            type = TRAM;
        }else{
            type = BUS;
        }
    }

    public Integer getId() {
        return id;
    }

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
                " Id: " + id;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BUS,TRAM, TROLLEYBUS})
    public @interface TransportType{}

    public static final int BUS = 0;
    public static final int TRAM = 1;
    public static final int TROLLEYBUS = 2;

}