package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.ReviewAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * Created by ASANDA on 2018/10/03.
 * for Pandaphic
 */
public class ReviewsManager extends CacheManager {
    public static final String DB_DATABASE_NAME = "reviews.db", DB_TABLE_NAME = "reviews";
    private final ServerCon serverCon;

    public ReviewsManager(Context context) {
        super(context);
        this.serverCon = ServerCon.getInstance(context);
    }

    @Override
    public String getDatabaseName() {
        return DB_DATABASE_NAME;
    }

    @Override
    public String getTableName() {
        return DB_TABLE_NAME;
    }

    @Override
    public void init(Context context) {
        this.fetchReviews(new ManagerRequestListener<ReviewAfroObject[]>() {
            @Override
            public void onRespond(ReviewAfroObject[] result) {
                cacheData(result);
            }

            @Override
            public void onApiError(ApiError apiError) {
                //todo error
            }
        });
    }

    @Override
    public void beginManagement() {
        this.getCachePointer().swapCursor(this.afroObjectDatabaseHelper.getAll());
    }

    @Override
    public void notifyCacheChanged() {
        this.getCachePointer().swapCursor(this.afroObjectDatabaseHelper.getAll());
    }

    @Override
    public void requestRefresh(final ManagerRequestListener<CacheManager> callback) {
        isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    fetchReviews(new ManagerRequestListener<ReviewAfroObject[]>() {
                        @Override
                        public void onRespond(ReviewAfroObject[] result) {
                            cacheData(result);
                            if(callback != null)
                                callback.onRespond(ReviewsManager.this);
                        }

                        @Override
                        public void onApiError(ApiError apiError) {
                            //todo error
                        }
                    });
                }else{
                    if(callback != null)
                        callback.onRespond(ReviewsManager.this);
                }
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });
    }

    @Override
    public void clearCache() {
        this.afroObjectDatabaseHelper.clear();
    }

    @Override
    protected void isDataInSync(ManagerRequestListener<Boolean> callback) {
        callback.onRespond(true);
    }

    private void fetchReviews(final ManagerRequestListener<ReviewAfroObject[]> callback){
        String url = ServerCon.BASE_URL + "/afroturf/reviews?userId=" + ServerCon.DEBUG_SALON_OID + "&reviewId=" + ServerCon.DEBUG_SALON_REVIEW_ID;
        this.serverCon.HTTP(0, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();

               /* JsonArray reviews = parser.parse(response.data).getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("reviewsIn");

                ReviewAfroObject[] reviewObjects = new ReviewAfroObject[0]'//reviews.size()];
                for(int pos  = 0; pos < reviews.size(); pos++){
                    JsonObject review = reviews.get(pos).getAsJsonObject();
                    ReviewAfroObject reviewAfroObject = new ReviewAfroObject();//ReviewsManager.this.afroObjectDatabaseHelper.objectMapper.readValue(review.toString(), ReviewAfroObject.class);
                    reviewAfroObject.set(parser, review.toString());
                    reviewObjects[pos] = reviewAfroObject;
                }*/
                if(callback != null)
                    callback.onRespond(new ReviewAfroObject[0]);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


}
