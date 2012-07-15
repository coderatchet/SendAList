package com.thenaglecode.sendalist.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.web.bindery.event.shared.EventBus;
import com.thenaglecode.sendalist.client.home.HomePlace;
import com.thenaglecode.sendalist.client.testFor2Displays.SideAActivityMapper;
import com.thenaglecode.sendalist.client.testFor2Displays.SideAPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 2/06/12
 * Time: 8:38 PM
 */
public class SendAList implements EntryPoint {
    private Place defaultMainPlace = new HomePlace();
    private SimplePanel appWidget = new SimplePanel();
    private SimplePanel mainWidget; //defaults view is list
    private SimplePanel sideWidget; //default view is navigation
    private SimplePanel menuWidget; //default view is menu

    public void onModuleLoad() {
        testTwoDisplays();
    }

    final HorizontalPanel horizontalPanel = new HorizontalPanel();

    private void testTwoDisplays() {
        final SimplePanel sideA = new SimplePanel();
        //final SimplePanel sideB = new SimplePanel();
        sideA.setStyleName("side");

        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        EventBus eventBus = clientFactory.getEventBus();
        PlaceController placeController = clientFactory.getPlaceController();

        SideAActivityMapper sideAActivityMapper = new SideAActivityMapper();
        ActivityManager sideAActivityManager = new ActivityManager(sideAActivityMapper, eventBus);
        sideAActivityManager.setDisplay(sideA);

        //SideBActivityMapper sideBActivityMapper = new SideBActivityMapper();
        //ActivityManager sideBActivitiyManager = new ActivityManager(sideBActivityMapper, eventBus);
        //sideBActivitiyManager.setDisplay(sideB);

        AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
        PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
        placeHistoryHandler.register(placeController, eventBus, new SideAPlace());

        sideA.setSize("30em", "30em");
        //sideB.setSize("30em", "30em");
        horizontalPanel.add(sideA);
        //horizontalPanel.add(sideB);

        horizontalPanel.setStyleName("mainmain");
        RootPanel.get("main").add(horizontalPanel);
        placeHistoryHandler.handleCurrentHistory();
    }

    private void initDisplayRegions() {
        ClientFactory clientFactory = GWT.create(ClientFactory.class);
        EventBus eventBus = clientFactory.getEventBus();
        PlaceController placeController = clientFactory.getPlaceController();
        //http://tbroyer.posterous.com/gwt-21-activities-nesting-yagni
        //RootPanel.get().add(new Button("test button"));

        ActivityMapper appActivityMapper = new AppActivityMapper(clientFactory);
        ActivityManager activityManager = new ActivityManager(appActivityMapper, eventBus);
        activityManager.setDisplay(appWidget);

        AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
        PlaceHistoryHandler placeHistoryHandler = new PlaceHistoryHandler(historyMapper);
        placeHistoryHandler.register(placeController, eventBus, defaultMainPlace);

        RootPanel.get().add(appWidget);
        placeHistoryHandler.handleCurrentHistory();
    }
}
