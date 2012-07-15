package com.thenaglecode.sendalist.client.home;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;
import com.thenaglecode.sendalist.shared.dto.GWTUserAccount;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:05 PM
 */
public class HomeActivity extends AbstractActivity implements HomeView.HomePresenter {

    private HomePlace place;
    private ClientFactory clientFactory;

    public HomeActivity(HomePlace place, ClientFactory clientFactory){
        this.place = place;
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        HomeView homeView = clientFactory.getHomeView();
        homeView.setWelcomeUserName(place.getWelcomeUserName());
        //todo implement
        panel.setWidget(homeView);
    }

    @Override
    public String getUserDisplayName() {
        String temp = "test";
        GWTUserAccount loggedInUser = clientFactory.getLoggedInUser();
        return loggedInUser.getDisplayName();
        //todo rpc call to get user display name. is this /\ the correct way?
    }

    @Override
    public void goTo(Place place) {
        clientFactory.getPlaceController().goTo(place);
    }
}
