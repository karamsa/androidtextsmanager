package co.geeksters.androidtextsmanager.events;

import java.util.ArrayList;

import co.geeksters.androidtextsmanager.model.RessourceList;

/**
 * Created by Karam Ahkouk on 11/09/15.
 */
public class StringsLoadedEvent {
    public ArrayList<RessourceList> stringRessources;
    public StringsLoadedEvent(ArrayList<RessourceList> stringRessources) {
        this.stringRessources = stringRessources;
    }
}
