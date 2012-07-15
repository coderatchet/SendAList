package com.thenaglecode.sendalist.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.thenaglecode.sendalist.client.contacts.ContactsView;
import com.thenaglecode.sendalist.client.header.MenuView;
import com.thenaglecode.sendalist.client.home.HomeView;
import com.thenaglecode.sendalist.client.home.HomeViewImpl;
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
 * Time: 3:50 PM
 */
public class ClientFactoryImpl implements ClientFactory{
    EventBus eventBus = null;
    PlaceController placeController = null;
    GWTUserAccount loggedInUser = null;

    //views
    HomeView homeView = null;
    MenuView menuView = null;
    ListView listView = null;
    NavigationView navigationView = null;
    ContactsView contactsView = null;
    SupportView supportView = null;
    SettingsView settingsView = null;
    private RegistrationView registrationView = null;

    @Override
    public EventBus getEventBus() {
        if(eventBus == null) {
            eventBus = new SimpleEventBus();
        }
        return eventBus;
    }

    @Override
    public PlaceController getPlaceController() {
        if(placeController == null){
            placeController = new PlaceController(getEventBus());
        }
        return placeController;
    }

    @Override
    public HomeView getHomeView() {
        if(homeView == null){
            homeView = new HomeViewImpl(this);
        }
        return homeView;
    }

    @Override
    public MenuView getMenuView() {
        if(menuView == null){
            menuView = GWT.create(MenuView.class);
        }
        return menuView;
    }

    @Override
    public NavigationView getNavigationView() {
        if(navigationView == null){
            navigationView = GWT.create(NavigationView.class);
        }
        return navigationView;
    }

    @Override
    public ListView getListView() {
        if(listView == null){
            listView = GWT.create(ListView.class);
        }
        return listView;
    }

    @Override
    public ContactsView getContactsView() {
        if(contactsView == null){
            contactsView = GWT.create(ContactsView.class);
        }
        return contactsView;
    }

    @Override
    public GWTUserAccount getLoggedInUser() {
        //todo not final implementation, flawed.
        if(loggedInUser != null){
            return loggedInUser;
        }
        else return new GWTUserAccount();
    }

    @Override
    public SupportView getSupportView() {
        if(supportView == null){
            supportView = GWT.create(SupportView.class);
        }
        return supportView;
    }

    @Override
    public SettingsView getSettingsView() {
        if(settingsView == null){
            settingsView = GWT.create(SettingsView.class);
        }
        return settingsView;
    }

    @Override
    public RegistrationView getRegistrationView() {
        if(registrationView == null){
            registrationView = GWT.create(RegistrationView.class);
        }
        return registrationView;
    }
}
