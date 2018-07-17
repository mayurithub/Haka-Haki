package com.mayurit.hakahaki.Helpers;


import com.mayurit.hakahaki.Model.AudioModel;
import com.mayurit.hakahaki.Model.CategoryModel;
import com.mayurit.hakahaki.Model.DateModel;
import com.mayurit.hakahaki.Model.NewsListModel;


import com.mayurit.hakahaki.Model.NoticeListModel;
import com.mayurit.hakahaki.Model.VideoModel;
import com.mayurit.hakahaki.Model.ProjectModel;


import java.util.List;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Krevilraj on 4/8/2018.
 */

public class RetrofitAPI {
    public static final String url = "http://hakahakionline.com/np/";
    public static final String auth_key = "Auth-Key:simplerestapi";
    public static final String content_type = "Content-Type:application/x-www-form-urlencoded";
    public static final String client_service = "Client-Service:frontend-client";
    public static final String cache_control = "Cache-Control:no-cache";

    public static PostService postService = null;

    public static PostService getService() {

        if (postService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);

        }
        return postService;


    }

    public interface PostService {


        @GET("./news-api/category/")
        Call<List<CategoryModel>> getCategoryList();

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/category/")
        Call<List<NewsListModel>> getCategoryLimitNews(@Query("category") int category, @Query("offset") int offset, @Query("limit") int limit);

        /*@Headers("Cache-Control:no-cache")
        @GET("./news-api/detail/?id=7396")
        Call<List<NewsListModel>> getPostDetail();
*/
        @Headers("Cache-Control:no-cache")
        @GET("./news-api/detail/")
        Call<NewsListModel> getPostDetail(@Query("id") String id);


        @Headers("Cache-Control:no-cache")
        @GET("./news-api/videolist/")
        Call<List<VideoModel>> getVideoList(@Query("category") int category, @Query("offset") int offset, @Query("limit") int limit);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/detail/")
        Call<VideoModel> getVideoDetail(@Query("ptype") String ptype, @Query("id") String id);


        @Headers("Cache-Control:no-cache")
        @GET("./news-api/project/")
        Call<List<ProjectModel>> getProjectDetail(@Query("project_id") String project_id);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/project/")
        Call<List<ProjectModel>> getProjectList();


        @Headers("Cache-Control:no-cache")
        @GET("./news-api/")
        Call<List<AudioModel>> getAudioList(@Query("ptype") String ptype, @Query("offset") int offset, @Query("limit") int limit);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/detail/")
        Call<AudioModel> getAudioDetail(@Query("ptype") String ptype, @Query("id") String id);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/")
        Call<List<NewsListModel>> getNEEFEJList(@Query("ptype") String ptype, @Query("offset") int offset, @Query("limit") int limit);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/detail/")
        Call<NewsListModel> getNEEFEJDetail(@Query("ptype") String ptype, @Query("id") String id);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/")
        Call<List<NoticeListModel>> getNoticeList(@Query("ptype") String ptype, @Query("offset") int offset, @Query("limit") int limit);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/lokpriya/")
        Call<List<NewsListModel>> getLokpriyaNews(@Query("offset") int offset, @Query("limit") int limit);

        @Headers("Cache-Control:no-cache")
        @GET("./news-api/nepali-date/")
        Call<DateModel> getDateDetail();


        @POST("./news-api/feedback")
        @FormUrlEncoded
        Call<String> sendFeedback(
                @Field("fullname") String fullname,
                @Field("email") String email,
                @Field("message") String message

        );
    }
}
