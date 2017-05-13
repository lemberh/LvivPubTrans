package org.rnaz.lvivpubtrans.model;

/**
 * Created by Roman on 2/6/2017.
 */

public interface IRouteModel {
    String getCode();
    void setCode(String code);
    @RouteModel.TransportType
    Integer getType();
    void setType(@RouteModel.TransportType Integer type);
    String getName();
    void setName(String name);
    Integer getId();
    void setId(Integer id);
    Integer getColor();
    void setColor(Integer color);
    boolean isFavorite();
}
