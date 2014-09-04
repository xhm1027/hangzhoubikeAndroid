package com.xhm.hangzhoubike.http;

import com.squareup.okhttp.OkHttpClient;
import com.xhm.hangzhoubike.http.converter.JsonConverter;
import com.xhm.hangzhoubike.http.interceptor.RequestInterceptor;
import com.xhm.hangzhoubike.object.BikeStation;
import com.xhm.hangzhoubike.object.BikeStationHistory;
import com.xhm.hangzhoubike.object.Page;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.List;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14/9/3
 * Time: 下午4:38
 */
public class Client {
    
    private String host = "http://hangzhoupubbike.duapp.com";

    private BikeStationService bikeStationService;
    
    public Client(){
        OkHttpClient okHttpClient = new OkHttpClient();
        RestAdapter builder = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(host)
                .setRequestInterceptor(new RequestInterceptor())
                .setConverter(new JsonConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        bikeStationService = builder.create(BikeStationService.class);
    }
    
    public Page<BikeStation> queryByPage(int page){
        try {
            return bikeStationService.queryByPage(page);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BikeStation> realTimeQuery(String name){
        try {
            return bikeStationService.realTimeQuery(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BikeStationHistory> history(Long stationId){
        try {
            return bikeStationService.history(stationId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] vrgs){
        Client client = new Client();
        Page<BikeStation> page = client.queryByPage(0);
        System.out.println(page);
    }
}
