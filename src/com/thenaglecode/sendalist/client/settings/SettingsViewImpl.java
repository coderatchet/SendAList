package com.thenaglecode.sendalist.client.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/10/12
 * Time: 10:14 PM
 */
public class SettingsViewImpl extends Composite implements SettingsView{

    interface SettingsViewImplUiBinder extends UiBinder<HTMLPanel, SettingsViewImpl> {
    }

    private static SettingsViewImplUiBinder ourUiBinder = GWT.create(SettingsViewImplUiBinder.class);

    public SettingsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
    }
}