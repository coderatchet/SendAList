package com.thenaglecode.sendalist.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.thenaglecode.sendalist.client.contacts.ContactsView;
import com.thenaglecode.sendalist.client.header.MenuView;
import com.thenaglecode.sendalist.client.home.HomeView;
import com.thenaglecode.sendalist.client.list.ListView;
import com.thenaglecode.sendalist.client.navigation.NavigationView;
import com.thenaglecode.sendalist.client.registration.RegistrationView;
import com.thenaglecode.sendalist.client.settings.SettingsView;
import com.thenaglecode.sendalist.client.support.SupportView;
import com.thenaglecode.sendalist.shared.dto.GWTUserAccount;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 3:48 PM
 */
public interface ClientFactory {
    EventBus getEventBus();
    PlaceController getPlaceController();
    HomeView getHomeView();
    MenuView getMenuView();
    NavigationView getNavigationView();
    ListView getListView();
    ContactsView getContactsView();
    GWTUserAccount getLoggedInUser();
    SupportView getSupportView();
    SettingsView getSettingsView();
    RegistrationView getRegistrationView();
}
