package cc.wo_mo.dubi.data;

import java.util.List;

import cc.wo_mo.dubi.data.Model.*;
import cc.wo_mo.dubi.data.Model.LoginResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by womo on 2016/12/8.
 */

public interface DubiService {


    /*
    * Registration, Login and Logout
    * */

    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("username") String username,
                              @Field("password") String password);

    @POST("/logout")
    Call<BaseResponse> logout();

    @FormUrlEncoded
    @POST("/registration")
    Call<BaseResponse> registration(@Field("username") String username,
                            @Field("password") String password);


    /*
    * User Api
    * */

    @GET("/users/{user_id}")
    Call<User> getUserInfo(@Path("user_id") int user_id);

    /*
    * Tweet API
    * */

    @GET("/users/{user_id}/tweet")
    Call<List<Tweet>> listUserTweets(@Path("user_id") int user_id,
                                     @Query("last_id") int last_id,
                                     @Query("limit") int limit);

    @GET("/users/{user_id}/friends/tweet")
    Call<List<Tweet>> listFriendsTweets(@Path("user_id") int user_id,
                                        @Query("last_id") int last_id,
                                        @Query("limit") int limit);

    @GET("/tweet")
    Call<List<Tweet>> listAllTweets(@Query("last_id") int last_id,
                                    @Query("limit") int limit);

    @GET("/tweet/{tweet_id}")
    Call<Tweet> getTweet(@Path("tweet_id") int tweet_id);

    @POST("/users/{user_id}/tweet")
    Call<Tweet> createTweet(@Path("user_id") int user_id,
                            @Body Tweet tweet);

    @DELETE("/users/{user_id}/tweet/{tweet_id}")
    Call<BaseResponse> deleteTweet(@Path("user_id") int user_id,
                                   @Path("tweet_id") int tweet_id);


    /*
    * Comment API
    * */
    @POST("/tweet/{tweet_id}/comment")
    Call<Comment> createComment(@Path("tweet_id") int tweet_id, @Body Comment comment);

    @GET("/tweet/{tweet_id}/comment")
    Call<List<Comment>> getComments(@Path("tweet_id") int tweet_id);

    @GET("/comment/{comment_id}")
    Call<Comment> getComment(@Path("comment_id") int comment_id);

    @DELETE("/tweet/{tweet_id}/comment/{comment_id}")
    Call<BaseResponse> deleteComment(@Path("tweet_id") int tweet_id,
                                     @Path("comment_id") int comment_id);

    /*
    * Relationship API
    * */
    @FormUrlEncoded
    @POST("/users/{user_id}/friends")
    Call<BaseResponse> follow(@Path("user_id") int user_id,
                              @Field("follow_user_id") int follow_user_id);

    @GET("/users/{user_id}/friends")
    Call<List<User>> listFriends(@Path("user_id") int user_id);

    @GET("/users/{user_id}/fans")
    Call<List<User>> listFans(@Path("user_id") int user_id);

    @DELETE("/users/{user_id}/friends/{remove_user_id}")
    Call<BaseResponse> unfollow(@Path("user_id") int user_id,
                                @Path("remove_user_id") int remove_user_id);

    /*
    * Upload Image
    * */
    @Multipart
    @POST("/image")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part part,
                                     @Part("user_id") int user_id);
}
