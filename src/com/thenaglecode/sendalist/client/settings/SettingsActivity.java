package com.thenaglecode.sendalist.client.settings;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/10/12
 * Time: 10:12 PM
 */
public class SettingsActivity extends AbstractActivity {

    private SettingsPlace place;
    private ClientFactory clientFactory;
    private SettingsView settingsView;

    public SettingsActivity(SettingsPlace place, ClientFactory clientFactory) {
        this.place = place;
        this.clientFactory = clientFactory;
        settingsView = clientFactory.getSettingsView();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(settingsView);
    }
}
