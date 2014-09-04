package com.xhm.hangzhoubike.http;

import com.xhm.hangzhoubike.object.BikeStation;
import com.xhm.hangzhoubike.object.BikeStationHistory;
import com.xhm.hangzhoubike.object.Page;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

import java.util.List;

/**
 * <P></P>
 * User: <a href="mailto:xhm.xuhm@alibaba-inc.com">苍旻</a>
 * Date: 14/9/3
 * Time: 下午4:33
 */
public interface BikeStationService {
//    @Headers("Content-type: application/json")
    @GET("/page.json")
    Page<BikeStation> queryByPage(@Query("page") Integer page);

    @GET("/realTimeQuery.json")
    List<BikeStation> realTimeQuery(@Query("name") String name);

    @GET("/history.json")
    List<BikeStationHistory> history(@Query("stationId") Long stationId);
}
