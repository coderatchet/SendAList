package com.thenaglecode.sendalist.shared.dto;

import com.google.gwt.json.client.*;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 9:08 PM
 */
public class GWTUserAccount implements IsSerializable{
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean isFederated() {
        return isFederated;
    }

    String email;
    String firstName;
    String lastName;
    String displayName;
    String photoUrl;
    boolean isFederated;

    public static GWTUserAccount fromJson(String jsonString) {
        GWTUserAccount account = new GWTUserAccount();
        JSONValue parsed = JSONParser.parseStrict(jsonString);
        JSONObject json = parsed.isObject();
        if(json != null){
            if(json.containsKey("email")) {
                JSONString email = json.get("email").isString();
                if(email != null){
                    account.email = email.stringValue();
                }
                else return null; //needs email
            }
            if(json.containsKey("firstName")) {
                JSONString firstName = json.get("firstName").isString();
                if(firstName != null){
                    account.firstName = firstName.stringValue();
                }
            }
            if(json.containsKey("lastName")) {
                JSONString lastName = json.get("lastName").isString();
                if(lastName != null){
                    account.lastName = lastName.stringValue();
                }
            }
            if(json.containsKey("displayName")) {
                JSONString displayName = json.get("displayName").isString();
                if(displayName != null){
                    account.displayName = displayName.stringValue();
                }
            }
            if(json.containsKey("photoUrl")) {
                JSONString photoUrl = json.get("photoUrl").isString();
                if(photoUrl != null){
                    account.photoUrl = photoUrl.stringValue();
                }
            }
            if(json.containsKey("isFederated")) {
                JSONBoolean isFederated = json.get("isFederated").isBoolean();
                if(isFederated != null){
                    account.isFederated = isFederated.booleanValue();
                }
            }
        }
        return null;
    }
}
