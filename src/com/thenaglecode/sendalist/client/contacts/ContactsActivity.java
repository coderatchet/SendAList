package com.thenaglecode.sendalist.client.contacts;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.thenaglecode.sendalist.client.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 8:58 PM
 */
public class ContactsActivity extends AbstractActivity implements ContactsView.ContactsPresenter {

    private ClientFactory clientFactory;
    private ContactsPlace contactsPlace;
    private ContactsView contactsView;

    public ContactsActivity(ContactsPlace contactsPlace, ClientFactory clientFactory){
        this.contactsPlace = contactsPlace;
        this.clientFactory = clientFactory;
        this.contactsView = clientFactory.getContactsView();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        ContactsView contactsView = clientFactory.getContactsView();
    }
}
