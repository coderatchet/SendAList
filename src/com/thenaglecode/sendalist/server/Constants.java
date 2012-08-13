package com.thenaglecode.sendalist.server;

/**
 * Created by IntelliJ IDEA.
 * User: jarednagle
 * Date: 12/08/12
 * Time: 7:08 PM
 */
public class Constants {

    public static final String PROTECTED_RESOURCES_URL = "/secured";

    /* Constants for Javascript Google Identity kit */
    public static final String URL_HOME = PROTECTED_RESOURCES_URL + "/SendAList.jsp";
    public static final String URL_LOGIN = "/login.jsp";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_SIGN_UP = "/signup.jsp";
    public static final String URL_USER_STATUS = "/status";
    public static final String URL_CALLBACK = "http://thenaglecode.broke-it.net:8080/callback";
    public static final String URL_REALM = "";
    public static final String DEV_KEY = "AIzaSyCk5yd7Qt3vQGAuXaUpeLdD2yKgx5enmis";
    public static final String COMPANY_NAME = "The Nagle Code";
    public static final String[] IDPS = new String[]{"Gmail", "GoogleApps", "Yahoo", "Hotmail"};
    public static final boolean TRY_FEDERATED_FIRST = true;
    public static final boolean USER_CACHED_USER_STATUS = false;
    public static final boolean USE_CONTEXT_PARAM = false;

    /* used for ContextLoader */
    public static final String GOOGLE_API_KEY = "AIzaSyCk5yd7Qt3vQGAuXaUpeLdD2yKgx5enmis";
    public static final String SESSION_USER_KEY = "sal_login_acc";
    public static final String SESSION_IDP_ASSERT = "sal_idp_assert";
    public static final String HOME_URL = "/secured/SendAList.jsp";
    public static final String SIGNUP_URL = "/signup.jsp";

    /* Api Scopes */
    public static final String SCOPE_READ_ONLY = "/read";
    public static final String SCOPE_READ_WRITE = "/readwrite";

    /* servlet names */
    public static final String SERVLET_URL_ACCOUNT_CHOOSER_JAVASCRIPT = "/acjs";
    public static final String SERVLET_URL_TRANSACTION_API = PROTECTED_RESOURCES_URL + SCOPE_READ_WRITE + "/api";
    public static final String SERVLET_URL_INFO_API = PROTECTED_RESOURCES_URL + SCOPE_READ_ONLY + "/info/*";
}
