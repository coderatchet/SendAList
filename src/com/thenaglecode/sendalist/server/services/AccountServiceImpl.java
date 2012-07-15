package com.thenaglecode.sendalist.server.services;

import com.google.apps.easyconnect.easyrp.client.basic.data.Account;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountException;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;
import com.thenaglecode.sendalist.server.domain2Objectify.SendAListDAO;
import com.thenaglecode.sendalist.server.domain2Objectify.entities.UserAccount;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

/**
 * Created by IntelliJ IDEA.
 * User: Jared Nagle
 * Date: 4/06/12
 * Time: 7:25 PM
 */
public class AccountServiceImpl implements AccountService {
    private boolean supportAutoCreateAccount = true;

    public boolean isSupportAutoCreateAccount() {
        return supportAutoCreateAccount;
    }

    public void setSupportAutoCreateAccount(boolean supportAutoCreateAccount) {
        this.supportAutoCreateAccount = supportAutoCreateAccount;
    }

    /**
     * Returns the account info for an email, or null if not registered.
     * The email and accountType fields must be set in returned Account object.
     */
    @Override
    @Nullable
    public Account getAccountByEmail(String email) {
        return new SendAListDAO().findUser(email);
    }

    @Override
    public Account createFederatedAccount(org.json.JSONObject assertion) throws AccountException {
        if (!isSupportAutoCreateAccount()) {
            throw new AccountException(AccountException.ACTION_NOT_ALLOWED);
        }
        try {
            UserAccount user = new UserAccount();
            user.setEmail(assertion.getString("email"));
            if (assertion.has("firstName")) {
                user.setFirstName(assertion.getString("firstName"));
            }
            if (assertion.has("lastName")) {
                user.setLastName(assertion.getString("lastName"));
            }
            if (assertion.has("profilePicture")) {
                user.setPhotoUrl(assertion.getString("profilePicture"));
            } else {
                user.setPhotoUrl("http://www.google.com/uds/modules/identitytoolkit/image/nophoto.png");
            }
            user.setIsFederated(true);
            SendAListDAO dao = new SendAListDAO();
            dao.saveUser(user);
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns whether the password is valid for the user.
     */
    @Override
    public boolean checkPassword(String email, String password) {
        return new SendAListDAO().checkPassword(email, password);
    }

    /**
     * Upgrade a legacy email account to federated login.
     * The response code may be OK, ACCOUNT_NOT_FOUND, ACTION_NOT_ALLOWED, UNKNOWN_ERROR.
     */
    @Override
    public void toFederated(String email) throws AccountException {
        SendAListDAO dao = new SendAListDAO();
        UserAccount user = dao.findUser(email);
        if (user == null) {
            throw new AccountException(AccountException.ACCOUNT_NOT_FOUND);
        } else //noinspection ConstantIfStatement
            if (false) /*todo if context is not the user, then...*/ {
                throw new AccountException(AccountException.ACTION_NOT_ALLOWED);
            } else {
                user.setIsFederated(true);
            }
    }
}
