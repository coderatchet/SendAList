package com.thenaglecode.sendalist.client.contacts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 10:34 PM
 */
public class ContactsViewImpl extends Composite implements ContactsView{
    interface ContactsViewImplUiBinder extends UiBinder<HTMLPanel, ContactsViewImpl> {
    }

    private static ContactsViewImplUiBinder ourUiBinder = GWT.create(ContactsViewImplUiBinder.class);

    public ContactsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}