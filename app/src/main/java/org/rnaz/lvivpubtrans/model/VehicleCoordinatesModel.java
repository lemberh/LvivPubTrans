package org.rnaz.lvivpubtrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class VehicleCoordinatesModel {

    @SerializedName("Angle")
    @Expose
    private Double angle;
    @SerializedName("EndPoint")
    @Expose
    private String endPoint;
    @SerializedName("IterationEnd")
    @Expose
    private Object iterationEnd;
    @SerializedName("IterationStart")
    @Expose
    private Object iterationStart;
    @SerializedName("RouteCode")
    @Expose
    private Object routeCode;
    @SerializedName("RouteId")
    @Expose
    private Integer routeId;
    @SerializedName("RouteName")
    @Expose
    private String routeName;
    @SerializedName("StartPoint")
    @Expose
    private String startPoint;
    @SerializedName("State")
    @Expose
    private Integer state;
    @SerializedName("TimeToPoint")
    @Expose
    private Integer timeToPoint;
    @SerializedName("VehicleId")
    @Expose
    private Integer vehicleId;
    @SerializedName("VehicleName")
    @Expose
    private String vehicleName;
    @SerializedName("X")
    @Expose
    private Double x;
    @SerializedName("Y")
    @Expose
    private Double y;
    @SerializedName("LowFloor")
    @Expose
    private Boolean lowFloor;

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Object getIterationEnd() {
        return iterationEnd;
    }

    public void setIterationEnd(Object iterationEnd) {
        this.iterationEnd = iterationEnd;
    }

    public Object getIterationStart() {
        return iterationStart;
    }

    public void setIterationStart(Object iterationStart) {
        this.iterationStart = iterationStart;
    }

    public Object getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(Object routeCode) {
        this.routeCode = routeCode;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTimeToPoint() {
        return timeToPoint;
    }

    public void setTimeToPoint(Integer timeToPoint) {
        this.timeToPoint = timeToPoint;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Boolean getLowFloor() {
        return lowFloor;
    }

    public void setLowFloor(Boolean lowFloor) {
        this.lowFloor = lowFloor;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}