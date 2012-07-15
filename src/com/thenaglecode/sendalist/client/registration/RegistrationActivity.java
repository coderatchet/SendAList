package com.thenaglecode.sendalist.client.registration;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 12/06/12
 * Time: 10:56 AM
 */
public class RegistrationActivity extends AbstractActivity{

    private ClientFactory clientFactory;
    private RegistrationPlace registrationPlace;
    private RegistrationView registraitonView;

    public RegistrationActivity(RegistrationPlace place, ClientFactory clientFactory){
        this.registrationPlace = place;
        this.clientFactory = clientFactory;
        this.registraitonView = clientFactory.getRegistrationView();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        //todo implement
    }
}
