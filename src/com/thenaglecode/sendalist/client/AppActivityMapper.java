package com.thenaglecode.sendalist.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.thenaglecode.sendalist.client.contacts.ContactsActivity;
import com.thenaglecode.sendalist.client.contacts.ContactsPlace;
import com.thenaglecode.sendalist.client.header.MenuActivity;
import com.thenaglecode.sendalist.client.list.ListActivity;
import com.thenaglecode.sendalist.client.list.ListPlace;
import com.thenaglecode.sendalist.client.navigation.NavigationActivity;
import com.thenaglecode.sendalist.client.navigation.NavigationPlace;
import com.thenaglecode.sendalist.client.placeGroups.HeaderPlace;
import com.thenaglecode.sendalist.client.placeGroups.MainPlace;
import com.thenaglecode.sendalist.client.placeGroups.SidePlace;
import com.thenaglecode.sendalist.client.registration.RegistrationActivity;
import com.thenaglecode.sendalist.client.registration.RegistrationPlace;
import com.thenaglecode.sendalist.client.settings.SettingsActivity;
import com.thenaglecode.sendalist.client.settings.SettingsPlace;
import com.thenaglecode.sendalist.client.support.SupportActivity;
import com.thenaglecode.sendalist.client.support.SupportPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 5:03 PM
 */
public class AppActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public AppActivityMapper(ClientFactory clientFactory) {
        super();
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof MainPlace) {
            if (place instanceof ContactsPlace) {
                return new ContactsActivity((ContactsPlace) place, clientFactory);
            } else if (place instanceof ListPlace) {
                return new ListActivity((ListPlace) place, clientFactory);
            } else if (place instanceof SupportPlace) {
                return new SupportActivity((SupportPlace) place, clientFactory);
            } else if (place instanceof SettingsPlace) {
                return new SettingsActivity((SettingsPlace) place, clientFactory);
            } else if (place instanceof RegistrationPlace) {
                return new RegistrationActivity((RegistrationPlace) place, clientFactory);
            }
        } else if (place instanceof HeaderPlace) {
            return new MenuActivity(clientFactory);
        } else if (place instanceof SidePlace) {
            return new NavigationActivity((NavigationPlace) place, clientFactory);
        }
        return null;
    }
}
