package com.cum.chat.acmap;

import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by 2-1Ping on 2017/2/10.
 */

public interface FileUploadService {
    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"")RequestBody imgs);

}
