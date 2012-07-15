package com.thenaglecode.sendalist.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.thenaglecode.sendalist.client.contacts.ContactsActivity;
import com.thenaglecode.sendalist.client.contacts.ContactsPlace;
import com.thenaglecode.sendalist.client.list.ListActivity;
import com.thenaglecode.sendalist.client.list.ListPlace;
import com.thenaglecode.sendalist.client.registration.RegistrationActivity;
import com.thenaglecode.sendalist.client.registration.RegistrationPlace;
import com.thenaglecode.sendalist.client.settings.SettingsActivity;
import com.thenaglecode.sendalist.client.settings.SettingsPlace;
import com.thenaglecode.sendalist.client.support.SupportActivity;
import com.thenaglecode.sendalist.client.support.SupportPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 13/06/12
 * Time: 12:10 AM
 */
public class MainActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public MainActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }
    @Override
    public Activity getActivity(Place place) {
        if (place instanceof ContactsPlace){
            return new ContactsActivity((ContactsPlace) place, clientFactory);
        }
        else if (place instanceof ListPlace) {
           return new ListActivity((ListPlace) place, clientFactory);
        }
        else if (place instanceof SupportPlace){
            return new SupportActivity((SupportPlace) place, clientFactory);
        }
        else if (place instanceof SettingsPlace){
            return new SettingsActivity((SettingsPlace) place, clientFactory);
        }
        else if (place instanceof RegistrationPlace){
            return new RegistrationActivity((RegistrationPlace) place, clientFactory);
        }
        return null;
    }
}
