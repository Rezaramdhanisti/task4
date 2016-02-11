package com.task.taskfour.transactionbookkeepping;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Personal on 2/4/2016.
 */

public interface SyncApi {
    @GET("/transactions")
    Call<Transactions> getTransactions();


    @GET("/transactions")

    Call<Transactions> getTransactions(@Path("id") String transaction_id);


    @PUT("/transactions")

    Call<Transactions> updateTransactions(@Path("id") int transaction_id, @Body Transaction transaction);


    @POST("/transactions")

    Call<Transaction> syncTransactions(@Body Transaction transaction);

}

