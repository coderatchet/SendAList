package com.thenaglecode.sendalist.server;

import com.google.apps.easyconnect.easyrp.client.basic.Context;
import com.google.apps.easyconnect.easyrp.client.basic.data.AccountService;
import com.google.apps.easyconnect.easyrp.client.basic.session.RpConfig;
import com.google.apps.easyconnect.easyrp.client.basic.session.SessionBasedSessionManager;
import com.google.apps.easyconnect.easyrp.client.basic.session.SessionManager;
import com.thenaglecode.sendalist.server.services.AccountServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by IntelliJ IDEA.
 * User: JELLYBEANS
 * Date: 6/7/12
 * Time: 4:31 PM
 */
public class ContextLoader implements ServletContextListener {
    public static final String GOOGLE_API_KEY = "AIzaSyCk5yd7Qt3vQGAuXaUpeLdD2yKgx5enmis";

    @Override
    public void contextDestroyed(ServletContextEvent evt) {
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        initEasyRpContext();
    }

    // Set config parameters.
    private void initEasyRpContext() {
        RpConfig config = new RpConfig.Builder()
                .sessionUserKey("sal_login_acc")
                .sessionIdpAssertionKey("sal_idp_assert")
                .homeUrl("/#home")
                .signupUrl("/#signup")
                .build();

        AccountService accountService = new AccountServiceImpl();

        // SessionBasedSessionManager fully supports server side session state.
        SessionManager sessionManager = new SessionBasedSessionManager(config);

        Context.setConfig(config);
        Context.setAccountService(accountService);
        Context.setSessionManager(sessionManager);
        Context.setGoogleApisDeveloperKey(GOOGLE_API_KEY);
    }
}
