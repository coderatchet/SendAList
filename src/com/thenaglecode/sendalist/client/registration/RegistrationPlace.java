package com.thenaglecode.sendalist.client.registration;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.thenaglecode.sendalist.client.placeGroups.MainPlace;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 12/06/12
 * Time: 10:56 AM
 */
public class RegistrationPlace extends MainPlace {
    String name;

    public void setName(String name){
        this.name = name;
    }

    public static class Tokenizer implements PlaceTokenizer<RegistrationPlace> {

        @Override
        public RegistrationPlace getPlace(String token) {
            RegistrationPlace place = new RegistrationPlace();
            place.setName(token);
            return place;
        }

        @Override
        public String getToken(RegistrationPlace place) {
            return place.getName();
        }
    }

    private String getName() {
        return name;
    }
}
