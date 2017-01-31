package org.rnaz.lvivpubtrans.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.rnaz.lvivpubtrans.model.RouteModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Roman on 1/19/2017.
 */

public class RestAPI {
    private static final String BASE_URL = "http://82.207.107.126:13541/SimpleRide/LAD/SM.WebApi/api/";

    public static synchronized LvivPubTransportOldAPI getAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(CF.INSTANCE)
                .build();

        LvivPubTransportOldAPI service = retrofit.create(LvivPubTransportOldAPI.class);
        return service;
    }

    public interface LvivPubTransportOldAPI {

        @GET("CompositeRoute/")
        Call<List<RouteModel>> getRoutes();

        /**
         * @param code value taken from this field {@link RouteModel#getCode()}
         * @return list list with points that form route
         */
        @GET("path/")
        Call<List<RouteModel>> getRoutePath(@Query("code") String code);

        /**
         * @param code value taken from this field {@link RouteModel#getCode()}
         * @return list list with points that form route in String representation
         */
        @GET("path/")
        Call<String> getRoutePathRaw(@Query("code") String code);

        @GET("CompositeRoute/")
        Call<String> getRoutesRaw();

        @GET("CompositeRoute/")
        Call<String> getStopsByRouteId(@Query("code") String code);

        @GET("RouteMonitoring/")
        Call<String> getRouteMonitoringByCode(@Query("code") String code);
    }

    private static class CF extends Converter.Factory {
        static Converter.Factory INSTANCE = new CF();

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        String body = StringEscapeUtils.unescapeJson(value.string());
                        return body.substring(1,body.length()-1);
                    }
                };
            } else {
                return super.responseBodyConverter(type, annotations, retrofit);
            }
        }
    }
}
