package com.thenaglecode.sendalist.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.thenaglecode.sendalist.client.contacts.ContactsPlace;
import com.thenaglecode.sendalist.client.home.HomePlace;
import com.thenaglecode.sendalist.client.testFor2Displays.SideAPlace;
import com.thenaglecode.sendalist.client.testFor2Displays.SideBPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:54 PM
 */
@WithTokenizers({
        HomePlace.Tokenizer.class,
        ContactsPlace.Tokenizer.class,
        SideAPlace.Tokenizer.class,
        SideBPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
