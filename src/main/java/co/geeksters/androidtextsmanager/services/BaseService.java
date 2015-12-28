package co.geeksters.androidtextsmanager.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.geeksters.androidtextsmanager.Basehelper;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.converter.GsonConverter;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public class BaseService<T> {

    private ExecutorService mExecutorService;
    public RestAdapter restAdapter;

    public Gson gson;

    public BaseService(){
        gson = new GsonBuilder()
                .create();
        startService();
    }


    public void stopAll(){
        mExecutorService.shutdownNow();
        //mExecutorService.shutdown();
        // probably await for termination.
        startService();
    }

    public ArrayList<T> parsListJsonElement(JsonElement response, Type listType){
        return gson.fromJson(response.toString(), listType);
    }

    public T parsJsonElement(JsonElement response, Type listType){
        return gson.fromJson(response.toString(), listType);
    }

    public void startService(){
        mExecutorService = Executors.newCachedThreadPool();
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(Basehelper.getInstance().remoteUrl)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .setConverter(new GsonConverter(gson))
                .setExecutors(mExecutorService, new MainThreadExecutor())
                .build();
    }
}