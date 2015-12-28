package co.geeksters.androidtextsmanager.services;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import co.geeksters.androidtextsmanager.Basehelper;
import co.geeksters.androidtextsmanager.events.StringsLoadFailedEvent;
import co.geeksters.androidtextsmanager.events.StringsLoadedEvent;
import co.geeksters.androidtextsmanager.interfaces.StringsLoaderInterface;
import co.geeksters.androidtextsmanager.model.RessourceList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public class StringLoaderService<T>  extends BaseService<T>{

    private StringsLoaderInterface api;

    public void startService(){
        super.startService();
        this.api = restAdapter.create(StringsLoaderInterface.class);
    }

    public void loadString() {
        stopAll();
        this.api.getStrings(new Callback<JsonElement>() {

            @Override
            public void success(JsonElement jsonObject, Response response) {
                Type listType = new TypeToken<ArrayList<RessourceList>>() {
                }.getType();

                ArrayList<RessourceList> stringRessources = (ArrayList<RessourceList>) parsListJsonElement(jsonObject.getAsJsonArray(), listType);
                Basehelper.getInstance().post(new StringsLoadedEvent(stringRessources));
            }

            @Override
            public void failure(RetrofitError error) {
                Basehelper.getInstance().post(new StringsLoadFailedEvent());
            }
        });
    }


}
