package com.thenaglecode.sendalist.client.testFor2Displays;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 26/06/12
 * Time: 10:05 PM
 */
public class SideAActivityMapper implements ActivityMapper {

    @Override
    public Activity getActivity(Place place) {
        return new SideAActivity();
    }

    private class SideAActivity extends AbstractActivity {
        @Override
        public void start(AcceptsOneWidget panel, EventBus eventBus) {
            FlowPanel flowPanel = new FlowPanel();
            flowPanel.add(new Label("Side A"));
            panel.setWidget(flowPanel);
        }
    }
}
