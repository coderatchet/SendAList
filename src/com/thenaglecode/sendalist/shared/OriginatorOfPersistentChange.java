package com.thenaglecode.sendalist.shared;

import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is used to determine the origin of the persistent change transaction, whether the request is from a user
 * in our system, what ip, etc... common usage is to determine whether a change is valid or forbidden for a given
 * session id.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 14/07/12
 * Time: 6:05 PM
 */
public abstract class OriginatorOfPersistentChange {
    String ip;
    UserAccount user;


    OriginatorOfPersistentChange(HttpServletRequest req) {
        ip = req.getRemoteAddr();
        user = getUser(req);
    }

    private UserAccount getUser(HttpServletRequest req) {
        return null; //todo implement
    }
}
