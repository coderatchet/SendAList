package com.thenaglecode.sendalist.client.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.thenaglecode.sendalist.client.Presenter;
import com.thenaglecode.sendalist.client.contacts.ContactsPlace;
import com.thenaglecode.sendalist.client.home.HomePlace;
import com.thenaglecode.sendalist.client.support.SupportPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:18 PM
 */
public class MenuViewImpl extends Composite implements MenuView {
    private Presenter presenter;

    @Override
    public HasClickHandlers getHomeButton() {
        return homeButton;
    }

    @Override
    public HasClickHandlers getSupportButton() {
        return supportButton;
    }

    @Override
    public HasClickHandlers getProfileButton() {
        return settingsButton;
    }

    @Override
    public HasClickHandlers getContactsButton() {
        return contactsButton;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    interface HeaderViewImplUiBinder extends UiBinder<HTMLPanel, MenuViewImpl> {}

    private static HeaderViewImplUiBinder ourUiBinder = GWT.create(HeaderViewImplUiBinder.class);

    @UiField
    Button homeButton;
    @UiField
    Button contactsButton;
    @UiField
    Button supportButton;
    @UiField
    Button settingsButton;

    public MenuViewImpl() {
        ourUiBinder.createAndBindUi(this);
    }

    @UiHandler("homeButton")
    public void onClickHome(ClickEvent event){
        presenter.goTo(new HomePlace());
    }

    @UiHandler("contactsButton")
    public void onClickContacts(ClickEvent event){
        presenter.goTo(new ContactsPlace());
    }

    @UiHandler("supportButton")
    public void onClickSupport(ClickEvent event){
        presenter.goTo(new SupportPlace());
    }
}