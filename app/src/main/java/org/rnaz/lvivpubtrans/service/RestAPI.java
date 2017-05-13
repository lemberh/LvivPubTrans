package org.rnaz.lvivpubtrans.service;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.rnaz.lvivpubtrans.model.PathPoint;
import org.rnaz.lvivpubtrans.model.RouteModel;
import org.rnaz.lvivpubtrans.model.StopModel;
import org.rnaz.lvivpubtrans.model.VehicleCoordinatesModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Roman on 1/19/2017.
 */

public class RestAPI {
    private static final String BASE_URL = "http://82.207.107.126:13541/SimpleRide/LAD/SM.WebApi/api/";

    public static synchronized LvivPubTransportOldAPI getAPI() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor1 = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                String bodyString = response.body().string().replace("\\\\\\","\\\\").replaceAll("\\\\\"","\"");
                MediaType contentType = response.body().contentType();
                ResponseBody body = ResponseBody.create(contentType,bodyString.substring(1,bodyString.length()-1));
                return response.newBuilder().body(body).build();
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(interceptor1)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
        Call<List<PathPoint>> getRoutePath(@Query("code") String code);

        /**
         * @param code value taken from this field {@link RouteModel#getCode()}
         * @return list list with points that form route in String representation
         */
        @GET("path/")
        @Deprecated
        Call<String> getRoutePathRaw(@Query("code") String code);

        @Deprecated
        @GET("CompositeRoute/")
        Call<String> getRoutesRaw();

        @GET("CompositeRoute/")
        Call<List<StopModel>> getStopsByRouteId(@Query("code") String code);

        @GET("RouteMonitoring/")
        Call<List<VehicleCoordinatesModel>> getRouteMonitoringByCode(@Query("code") String code);
    }

    private static class CF extends Converter.Factory {
        static Converter.Factory INSTANCE = new CF();

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException {
                        String body = value.string().replace("\\\\\\","\\\\").replaceAll("\\\\\"","\"");
//                        String body = StringEscapeUtils.unescapeJson(value.string());
                        return body.substring(1,body.length()-1);
                    }
                };
            } else {
                return super.responseBodyConverter(type, annotations, retrofit);
            }
        }
    }
}
