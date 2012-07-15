package com.thenaglecode.sendalist.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.thenaglecode.sendalist.client.header.MenuActivity;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 13/06/12
 * Time: 12:15 AM
 */
public class MenuActivityMapper implements ActivityMapper {

    ClientFactory clientFactory;

    public MenuActivityMapper(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        return new MenuActivity(clientFactory);
    }
}
