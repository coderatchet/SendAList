package com.thenaglecode.sendalist.client.testFor2Displays;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 26/06/12
 * Time: 10:37 PM
 */
public class SideAPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<SideAPlace> {

        @Override
        public SideAPlace getPlace(String token) {
            return null;
        }

        @Override
        public String getToken(SideAPlace place) {
            return null;
        }
    }
}
