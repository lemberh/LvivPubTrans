
package org.rnaz.lvivpubtrans.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RouteModel implements Serializable{

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

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
                " Id: " + id;
    }
}