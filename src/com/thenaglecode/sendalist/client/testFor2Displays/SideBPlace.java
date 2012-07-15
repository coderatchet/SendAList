package com.thenaglecode.sendalist.client.testFor2Displays;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 27/06/12
 * Time: 9:13 PM
 */
public class SideBPlace extends Place {

    public static class Tokenizer implements PlaceTokenizer<SideBPlace> {

        @Override
        public SideBPlace getPlace(String token) {
            return null;
        }

        @Override
        public String getToken(SideBPlace place) {
            return null;
        }
    }
}
