package com.thenaglecode.sendalist.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.thenaglecode.sendalist.client.navigation.NavigationActivity;
import com.thenaglecode.sendalist.client.navigation.NavigationPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 13/06/12
 * Time: 7:49 PM
 */
public class NavigationActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public NavigationActivityMapper(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        return new NavigationActivity((NavigationPlace) place, clientFactory);
    }
}
