package co.geeksters.androidtextsmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public class Basehelper{

    private static Basehelper INSTANCE = null;

    public static Map<Object, Object> events=new HashMap<Object, Object>();
    public static Bus bus;
    private static Context context;
    private static Context appContext;
    private static String preferencesName = "stringsSharedPreferences";
    public static String remoteUrl;

    public  Basehelper(Context context, String remoteUrl) {
        bus = new Bus();
        this.context = context;
        this.appContext = context.getApplicationContext();
        this.remoteUrl = remoteUrl;
    }

    public  Basehelper() {
        bus = new Bus();
    }

    public static Basehelper getInstance(Context context, String remoteUrl){
        if (INSTANCE == null){
            INSTANCE = new Basehelper(context, remoteUrl);
        }
        return INSTANCE;
    }

    public static Basehelper getInstance(){
        if (INSTANCE == null){
            INSTANCE =  new Basehelper();
        }
        return INSTANCE;
    }

    public static Context getContext() {
        return context;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static synchronized void register(Object object) {
        if (events.containsKey(object)) {
            bus.unregister(events.get(object));
        }
        events.put(object, object);
        bus.register(object);
    }

    public  static  synchronized void unregister(Object object) {
        events.remove(object);
        bus.unregister(object);
    }

    public static  synchronized boolean isRegistered(Object object) {
        return events.containsKey(object);
    }

    public static synchronized Set<Object> getRegistered() {
        return new HashSet<Object>(events.keySet());
    }

    public static synchronized void unregisterAll() {
        for (Object o : events.keySet()) {
            bus.unregister(o);
        }
        events.clear();
    }

    public static void post(Object event) {
        bus.post(event);
    }


    public static void storeObject(Object object, String key) {
        SharedPreferences sharedPref = getAppContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String objectString = "";
        try {
            objectString =  gson.toJson(object);
            editor.putString(key, objectString);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String retrieveObject(String key) {
        SharedPreferences sharedPref = getAppContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        String objectString = sharedPref.getString(key, "");
        return objectString;
    }

    public static void removeObject(String key) {
        SharedPreferences sharedPref = getAppContext().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        sharedPref.edit().remove(key).commit();
    }

    public static void setLocale(final Context ctx, final String lang)
    {
        final Locale loc = new Locale(lang);
        Locale.setDefault(loc);
        final Configuration cfg = new Configuration();
        cfg.locale = loc;
        ctx.getResources().updateConfiguration(cfg, null);
    }

}
