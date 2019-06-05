package com.test.qrcodeloginandroid;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wangyongxin on 2018/1/30.
 */

public interface HttpService {
//    @POST("/181-1")
//    @FormUrlEncoded
//    Call<WeiXinBean> showDataByPost(@Field("showapi_appid") String showapi_appid, @Field("showapi_sign") String showapi_sign, @Field("num") String num);

//    @GET("/output/channels_topics_timeline.json")
//    Call<List<VideoBean>> showDataByGet(@Query("topic_name") String       topic_name, @Query("page")int page);


    @POST("/login/confirm")
    Call<CodeMsg<Map<String, Object>, String>> comfirmLogin(@Query("secret_code") String secret_code);
}
