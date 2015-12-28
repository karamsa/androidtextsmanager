package co.geeksters.androidtextsmanager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import co.geeksters.androidtextsmanager.events.StringsLoadFailedEvent;
import co.geeksters.androidtextsmanager.events.StringsLoadedEvent;
import co.geeksters.androidtextsmanager.interfaces.RessourceLoadedListener;
import co.geeksters.androidtextsmanager.model.RessourceList;
import co.geeksters.androidtextsmanager.model.StringRessource;
import co.geeksters.androidtextsmanager.services.StringLoaderService;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public class TextManager {

    public static TextManager INSTANCE = null;

    private ArrayList<RessourceLoadedListener> listeners = new ArrayList<RessourceLoadedListener>();

    private TextManager(){}

    public static TextManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new TextManager();
        }
        return INSTANCE;
    }

    private void initBaseHelper(Context context, String remoteUrl){
        //Register object
        Basehelper.getInstance(context, remoteUrl);
        Basehelper.getInstance().unregisterAll();
        Basehelper.getInstance().register(this);
    }

    public void refreshRessources(Context context, String remoteUrl){
        initBaseHelper(context, remoteUrl);
        //Send request to load strings from remot url
        requestRessource();
    }

    public void requestRessource(){
        StringLoaderService<RessourceList> stringLoaderService = new StringLoaderService<RessourceList>();
        stringLoaderService.loadString();
    }

    @Subscribe
    public void stringsLoaded(StringsLoadedEvent stringsLoadedEvent){
        //Stock string to shared preferences
        HashMap<String, ArrayList<StringRessource>> languageRessources = new HashMap<>();
        for (RessourceList ressourceList : stringsLoadedEvent.stringRessources){
            languageRessources.put(ressourceList.language, ressourceList.strings);
        }

        Basehelper.getInstance().storeObject(languageRessources, "languageRessources");
        for (RessourceLoadedListener ressourceListener : listeners){
            if (ressourceListener != null){
                ressourceListener.ressourcesLoaded();
            }
        }
    }

    @Subscribe
    public void stringsLoadFailed(StringsLoadFailedEvent stringsLoadFailedEvent){
        //String load failed, Removal of progress bar.
        for (RessourceLoadedListener ressourceListener : listeners){
            if (ressourceListener != null){
                ressourceListener.ressourcesLoadFailed();
            }
        }
    }

    public  String getString(String name, String language){
        String value = getStringFromStore(name,language);
        if (value == null){
            int id = Basehelper.getAppContext().getResources().getIdentifier(name,"string", Basehelper.getAppContext().getPackageName());
            if (id == 0) return "";
            return  Basehelper.getAppContext().getResources().getString(id);
        }

        return value;
    }

    private  String getStringFromStore(String name, String language){
        String  stringlanguageRessources = Basehelper.getInstance().retrieveObject("languageRessources");
        if (stringlanguageRessources.compareToIgnoreCase("") != 0){
            Type listType = new TypeToken<HashMap<String, ArrayList<StringRessource>>>() {
            }.getType();
            HashMap<String, ArrayList<StringRessource>>  languageRessources = new Gson().fromJson(stringlanguageRessources, listType);
            if (languageRessources.get(language) == null){
                return null;
            }

            ArrayList<StringRessource> stringRessources = languageRessources.get(language);

            String value = "";
            for (StringRessource stringRessource : stringRessources){
                if (stringRessource.key.compareToIgnoreCase(name)==0){
                    return stringRessource.value;
                }
            }
        }
        return null;
    }

    public  void refreshViewTexts(Context context, String language){
        String firstLanguage = "";
        if (language.compareToIgnoreCase("") == 0){
            Locale currentLocale = Basehelper.getAppContext().getResources().getConfiguration().locale;
            firstLanguage = currentLocale.getDefault().getLanguage().toLowerCase();
            language = firstLanguage;
        }else{
            Basehelper.setLocale(Basehelper.getAppContext(), language);
        }
        ViewGroup root = (ViewGroup)((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        makeRefreshOnViews(root, language);
        Basehelper.setLocale(Basehelper.getAppContext(), firstLanguage);
    }

    public  void refreshViewTexts(Context context){
        refreshViewTexts(context, "");
    }


    private  void makeRefreshOnViews(ViewGroup root, String language){
        HashMap<String, ArrayList<View>> views_ = new HashMap<>();

//        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
//                views.addAll(getAllViews((ViewGroup) child));
                makeRefreshOnViews((ViewGroup) child, language);
            }

            final Object tagObj = child.getTag();
            if (tagObj != null) {
//                views.add(child);

                //        Chronometer
                //        RadioButton
                //        TextView
                //        Button
                //        EditText
                //        CheckBox
                //        Switch
                //        ToggleButton
                //        CheckedTextView
                //        MultiAutoCompleteTextView
                //        android.inputmethodservice.ExtractEditText
                //        AutoCompleteTextView

                if (child instanceof TextView) ((TextView)child).setText(getString(tagObj.toString(), language));
                if (child instanceof Button) ((Button)child).setText(getString(tagObj.toString(), language));
                if (child instanceof EditText) ((EditText)child).setText(getString(tagObj.toString(), language));
                if (child instanceof CheckBox) ((CheckBox)child).setText(getString(tagObj.toString(), language));
                if (child instanceof RadioButton) ((RadioButton)child).setText(getString(tagObj.toString(), language));
                if (child instanceof ToggleButton) ((ToggleButton)child).setText(getString(tagObj.toString(), language));
                if (child instanceof Switch) ((Switch)child).setText(getString(tagObj.toString(), language));
                if (child instanceof CheckedTextView) ((CheckedTextView)child).setText(getString(tagObj.toString(), language));
                if (child instanceof AutoCompleteTextView) ((AutoCompleteTextView)child).setText(getString(tagObj.toString(), language));
                if (child instanceof Chronometer) ((Chronometer)child).setText(getString(tagObj.toString(), language));
                if (child instanceof MultiAutoCompleteTextView) ((MultiAutoCompleteTextView)child).setText(getString(tagObj.toString(), language));
                if (child instanceof android.inputmethodservice.ExtractEditText) ((android.inputmethodservice.ExtractEditText)child).setText(getString(tagObj.toString(), language));
            }

        }
//        return views;
    }

    public void setOnRessourceLoaded(RessourceLoadedListener ressourceLoaded){
        listeners.add(ressourceLoaded);
    }

}
