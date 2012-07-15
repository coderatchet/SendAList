package com.thenaglecode.sendalist.client.home;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.thenaglecode.sendalist.client.Presenter;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 3:19 PM
 */
public interface HomeView extends IsWidget{
    void setWidgetVisible(AcceptsOneWidget panel, boolean visible);
    AcceptsOneWidget getMenuPanel();
    AcceptsOneWidget getSidePanel();
    AcceptsOneWidget getMainPanel();

    void setWelcomeUserName(String welcomeUserName);
    
    public interface HomePresenter extends Presenter {
        String getUserDisplayName();
    }
}
