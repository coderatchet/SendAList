package com.thenaglecode.sendalist.client.testFor2Displays;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 26/06/12
 * Time: 10:22 PM
 */
public class SideBActivityMapper implements ActivityMapper {

    @Override
    public Activity getActivity(Place place) {
        return new SideBActivity();
    }

    private class SideBActivity extends AbstractActivity {
        @Override
        public void start(AcceptsOneWidget panel, EventBus eventBus) {
            panel.setWidget(new Button("SideB"));
        }
    }
}
