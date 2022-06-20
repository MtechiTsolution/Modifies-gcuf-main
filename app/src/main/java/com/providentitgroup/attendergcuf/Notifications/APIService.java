package com.providentitgroup.attendergcuf.Notifications;



import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAdhj3TV4:APA91bF4D-6_xmBuI8EpSKQVCSNXlgU2C4PbvNB8nvnsZZ70wlUMNWSPUx1RTeS817UlHNYmGWTTegJ5uPRq8exBgnXXBrMQ_Z04d6Su2aumMThwlr8Na0FpgVuPx5itcP2pnVelBWLI"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
