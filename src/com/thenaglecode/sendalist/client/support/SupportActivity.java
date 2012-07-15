package com.thenaglecode.sendalist.client.support;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 9:54 PM
 */
public class SupportActivity extends AbstractActivity {
    private SupportPlace supportPlace;
    private ClientFactory clientFactory;

    public SupportActivity(SupportPlace supportPlace, ClientFactory clientFactory) {
        this.supportPlace = supportPlace;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        SupportView supportView = clientFactory.getSupportView();
        panel.setWidget(supportView);
        //todo finish
    }
}
