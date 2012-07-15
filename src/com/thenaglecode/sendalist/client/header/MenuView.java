package com.thenaglecode.sendalist.client.header;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.thenaglecode.sendalist.client.Presenter;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:18 PM
 */
public interface MenuView extends IsWidget{
    HasClickHandlers getHomeButton();
    HasClickHandlers getSupportButton();
    HasClickHandlers getProfileButton();
    HasClickHandlers getContactsButton();
    void setPresenter(Presenter presenter);
}
