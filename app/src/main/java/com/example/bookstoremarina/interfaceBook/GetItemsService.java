package com.example.bookstoremarina.interfaceBook;

import com.example.bookstoremarina.models.Item;
import com.example.bookstoremarina.models.Result;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetItemsService {

    @GET("volumes")
    Call<Result> getItems(@Query("q") String item);

    @GET("volumes/{id}") // Parametro que passamos para o serviço, {valor} e colocamos o "valor" no metodo
    Call<Item> getItemDetail(@Path("id") String id); // valor que passamos

}
