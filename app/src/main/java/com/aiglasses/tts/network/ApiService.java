package com.aiglasses.tts.network;

import com.aiglasses.tts.model.GetTokenResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * API服务
 *
 * @author
 * @date
 */
public interface ApiService {

    /**
     * 获取鉴权认证Token
     * @param grant_type 类型
     * @param client_id API Key
     * @param client_secret Secret Key
     * @return GetTokenResponse
     */
    @FormUrlEncoded
    @POST("/oauth/2.0/token")
    Call<GetTokenResponse> getToken(@Field("grant_type") String grant_type,
                                    @Field("client_id") String client_id,
                                    @Field("client_secret") String client_secret);

    /**
     * 在线API音频合成
     * @param tok 鉴权token
     * @param ctp 客户端类型选择，web端填写固定值1
     * @param cuid 用户唯一标识，用来计算UV值。建议填写能区分用户的机器 MAC 地址或 IMEI 码，长度为60字符以内
     * @param lan 固定值zh。语言选择,目前只有中英文混合模式，填写固定值zh
     * @param tex 合成的文本，使用UTF-8编码。小于2048个中文字或者英文数字，文本在百度服务器内转换为GBK后，长度必须小于4096字节（5003、5118发音人需小于512个中文字或者英文数字）
     * @return 正常合成之后返回一个音频文件
     */
    @Streaming
    @FormUrlEncoded
    @POST("/text2audio")
    Call<ResponseBody> synthesis(@Field("tok") String tok,
                                 @Field("ctp") String ctp,
                                 @Field("cuid") String cuid,
                                 @Field("lan") String lan,
                                 @Field("tex") String tex,
                                 @Field("per") String per);



}
