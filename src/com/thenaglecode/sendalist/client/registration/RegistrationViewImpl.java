package com.thenaglecode.sendalist.client.registration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 12/06/12
 * Time: 10:57 AM
 */
public class RegistrationViewImpl implements RegistrationView {
    interface RegistrationViewImplUiBinder extends UiBinder<HTMLPanel, RegistrationViewImpl> {
    }

    private static RegistrationViewImplUiBinder ourUiBinder = GWT.create(RegistrationViewImplUiBinder.class);

    public RegistrationViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
    }
}