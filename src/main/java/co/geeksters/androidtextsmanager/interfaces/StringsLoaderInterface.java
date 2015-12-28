package co.geeksters.androidtextsmanager.interfaces;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public interface StringsLoaderInterface {

    @GET("/")
    void getStrings(Callback<JsonElement> cb);
}
