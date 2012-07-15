package com.thenaglecode.sendalist.client.navigation;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/14/12
 * Time: 8:32 AM
 */
public class NavigationActivity extends AbstractActivity {
    private ClientFactory clientFactory;
    private NavigationPlace navigationPlace;
    private NavigationView navigationView;

    private NavigationActivity() {
    }

    //todo implement
    public NavigationActivity(NavigationPlace navigationPlace, ClientFactory clientFactory){
        this.navigationPlace = navigationPlace;
        this.clientFactory = clientFactory;
        this.navigationView = clientFactory.getNavigationView();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        //todo implement
    }
}
