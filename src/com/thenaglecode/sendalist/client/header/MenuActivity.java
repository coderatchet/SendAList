package com.thenaglecode.sendalist.client.header;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;
import com.thenaglecode.sendalist.client.Presenter;
import com.thenaglecode.sendalist.client.contacts.ContactsPlace;
import com.thenaglecode.sendalist.client.home.HomePlace;
import com.thenaglecode.sendalist.client.settings.SettingsPlace;
import com.thenaglecode.sendalist.client.support.SupportPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 5:08 PM
 */
public class MenuActivity extends AbstractActivity implements Presenter {

    private ClientFactory clientFactory;
    private MenuView menuView;

    public MenuActivity(ClientFactory clientFactory){
        this.clientFactory = clientFactory;
        menuView = clientFactory.getMenuView();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        MenuView menuView = clientFactory.getMenuView();
        menuView.setPresenter(this);
        panel.setWidget(menuView);
        //todo finish (use place or don't use it i.e. remove it)
    }

    private void init(){
        menuView.getHomeButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                goTo(new HomePlace());
            }
        });
    }

    private void onClick(ClickEvent event){
        if(event.getSource() == menuView.getHomeButton()){
            goTo(new HomePlace());
        }
        else if(event.getSource() == menuView.getContactsButton()){
            goTo(new ContactsPlace());
        }
        else if(event.getSource() == menuView.getProfileButton()){
            goTo(new SettingsPlace());
        }
        else if(event.getSource() == menuView.getSupportButton()){
            goTo(new SupportPlace());
        }
    }

    @Override
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
