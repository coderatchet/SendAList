package com.thenaglecode.sendalist.client.navigation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:18 PM
 */
public class NavigationViewImpl extends Composite implements NavigationView {
    interface NavigationViewImplUiBinder extends UiBinder<HTMLPanel, NavigationViewImpl> {
    }

    private static NavigationViewImplUiBinder ourUiBinder = GWT.create(NavigationViewImplUiBinder.class);

    public NavigationViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}