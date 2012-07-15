package com.thenaglecode.sendalist.client.support;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 5:20 PM
 */
public class SupportViewImpl extends Composite implements SupportView{
    interface SupportViewImplUiBinder extends UiBinder<HTMLPanel, SupportViewImpl> {
    }

    private static SupportViewImplUiBinder ourUiBinder = GWT.create(SupportViewImplUiBinder.class);

    public SupportViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}