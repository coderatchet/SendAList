package com.thenaglecode.sendalist.client.home;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.thenaglecode.sendalist.client.ClientFactory;
import com.thenaglecode.sendalist.client.MainActivityMapper;
import com.thenaglecode.sendalist.client.MenuActivityMapper;
import com.thenaglecode.sendalist.client.NavigationActivityMapper;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 3:18 PM
 */
public class HomeViewImpl extends Composite implements HomeView {

    @Override
    public void setWidgetVisible(AcceptsOneWidget panel, boolean visible) {
        // dockLayoutPanel.
    }

    @Override
    public AcceptsOneWidget getMenuPanel() {
        return top;
    }

    @Override
    public AcceptsOneWidget getSidePanel() {
        return side;
    }

    @Override
    public AcceptsOneWidget getMainPanel() {
        return main;
    }

    @Override
    public void setWelcomeUserName(String welcomeUserName) {
        welcomeLabel.setText("Welcome, " + welcomeUserName);
    }

    interface HomeViewUiBinder extends UiBinder<HTMLPanel, HomeViewImpl> {
    }

    private static HomeViewUiBinder ourUiBinder = GWT.create(HomeViewUiBinder.class);

    @UiField
    SimplePanel top;
    @UiField
    SimplePanel side;
    @UiField
    SimplePanel main;
    @UiField
    Label welcomeLabel;
    @UiField
    HorizontalPanel head;
    @UiField
    Image logoImage;
    @UiField
    DockLayoutPanel dockLayoutPanel;
    private ClientFactory clientFactory;

    public HomeViewImpl(ClientFactory clientFactory) {
        ourUiBinder.createAndBindUi(this);

        this.clientFactory = clientFactory;
        // side sub-region mapper/manager
        AcceptsOneWidget sideDisplay = new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                Widget real = Widget.asWidgetOrNull(w);
                setWidgetVisible(getSidePanel(), real != null);
                getSidePanel().setWidget(real);
            }
        };
        ActivityMapper navigationActivityMapper = new NavigationActivityMapper(clientFactory);
        ActivityManager sideActivityManager = new ActivityManager(navigationActivityMapper, clientFactory.getEventBus());
        sideDisplay.setWidget(clientFactory.getNavigationView());
        sideActivityManager.setDisplay(sideDisplay);

        // Menu sub-region mapper/manager
        AcceptsOneWidget menuDisplay = new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                Widget real = Widget.asWidgetOrNull(w);
                setWidgetVisible(getMenuPanel(), real != null);
                getMenuPanel().setWidget(real);
            }
        };
        ActivityMapper menuActivityMapper = new MenuActivityMapper(clientFactory);
        ActivityManager menuActivityManager = new ActivityManager(menuActivityMapper, clientFactory.getEventBus());
        menuDisplay.setWidget(clientFactory.getMenuView());
        menuActivityManager.setDisplay(menuDisplay);

        // Main sub-region mapper/manager
        AcceptsOneWidget mainDisplay = new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                Widget real = Widget.asWidgetOrNull(w);
                setWidgetVisible(getMainPanel(), real != null);
                getMainPanel().setWidget(real);
            }
        };
        ActivityMapper mainActivityMapper = new MainActivityMapper(clientFactory);
        ActivityManager mainActivityManager = new ActivityManager(mainActivityMapper, clientFactory.getEventBus());
        mainDisplay.setWidget(clientFactory.getListView());
        mainActivityManager.setDisplay(mainDisplay);
    }
}