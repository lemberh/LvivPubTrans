package org.rnaz.lvivpubtrans.repository;

/**
 * Created by Roman on 2017-03-20.
 */

public interface CommonRepository {

    boolean isDataUpdateNeeded();
    void setDataUpdateNeeded(boolean isNeede);

}
