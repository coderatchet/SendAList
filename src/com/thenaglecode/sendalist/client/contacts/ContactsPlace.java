package com.thenaglecode.sendalist.client.contacts;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.thenaglecode.sendalist.client.placeGroups.MainPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:32 PM
 */
public class ContactsPlace extends MainPlace {
    public static class Tokenizer implements PlaceTokenizer<ContactsPlace> {
        @Override
        public ContactsPlace getPlace(String token) {
            return new ContactsPlace();
        }

        @Override
        public String getToken(ContactsPlace place) {
            return "contacts";
        }
    }
}
