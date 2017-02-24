package org.rnaz.lvivpubtrans.model.mappers;

import org.rnaz.lvivpubtrans.model.IPathModel;
import org.rnaz.lvivpubtrans.model.IRouteModel;
import org.rnaz.lvivpubtrans.model.IStopModel;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.realm.RealmRouteModel;

/**
 * Created by Roman on 2/6/2017.
 */

public class RouteModelMapper {

    public static IRouteModel toLocalObject(IRouteModel remote) {
        return copyData(remote,new RouteModel());
    }

    public static RealmRouteModel toRealmObject(IRouteModel remote){
        return copyData(remote,new RealmRouteModel());
    }

     public static <T extends IRouteModel> T copyData (IRouteModel remote, T local){
        local.setId(remote.getId());
        local.setCode(remote.getCode());
        local.setName(remote.getName());

        return local;
    }

    public static <T extends IStopModel> T copyData (IStopModel from, T to){
        to.setId(from.getId());
        to.setName(from.getName());
        to.setCode(from.getCode());
        to.setX(from.getX());
        to.setY(from.getY());
        return to;
    }

    public static <T extends IPathModel> T copyData (IPathModel from, T to){
        to.setX(from.getX());
        to.setY(from.getY());
        return to;
    }

}
