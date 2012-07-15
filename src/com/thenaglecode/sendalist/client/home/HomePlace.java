package com.thenaglecode.sendalist.client.home;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 4:06 PM
 */
public class HomePlace extends Place{

    public HomePlace() {}

    private String welcomeUserName;

    public String getWelcomeUserName() {
        return welcomeUserName;
    }

    public static class Tokenizer implements PlaceTokenizer<HomePlace> {
        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }

        @Override
        public String getToken(HomePlace place) {
            return "home";
        }
    }
}
