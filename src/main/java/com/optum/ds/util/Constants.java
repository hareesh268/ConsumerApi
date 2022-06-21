package com.optum.ds.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Application Constants
 */
public class Constants {
    public static final String STR_SEPERATOR=":";
    public static final String TOKEN_GRANT_TYPE = "grant_type";
    public static final String TOKEN_CLIENT_CREDENTIALS = "client_credentials";
    public static final String TOKEN_CLIENT_ID = "client_id";
    public static final String TOKEN_CLIENT_SECRET = "client_secret";
    public static final String TOKEN_CLIENT_SCOPE = "scope";

    public static final String FUNCTIONALITY_SPLUNK_LOGGING = "DS-TINVALIDATE-CONSUMER|";
    public static final String PASS_WRAPPER = "******";
    public static final String ERROR_TIME_FORMAT = "yyyy.MM.dd@hh:mm:ss.SSS";
    private static SimpleDateFormat errorDateFormat = new SimpleDateFormat ( ERROR_TIME_FORMAT );

    public static final String TECH_ERROR_DESCRIPTION = "Something went wrong. Please try again later.";

    public static final String APP_ID = "2";
    public static final String KEY_PENDING = "P";
    public static final String MAIL_KEY_TYPE = "R";

    public static final String ACTIVE = "Active";

    //Token Service
    public static final String TOKEN_SERVICE_FAILED_FAULT_CODE = "DS:E:EXT:TOKF";

    public static String errorTimeStamp () {
        Date dNow = new Date();
        return errorDateFormat.format ( dNow );
    }

    public static final List<String> STATUS_SUCCESS = Collections.unmodifiableList(new ArrayList<String>(){{
        add("0");
        add("6");
        add("7");
        add("8");
    }});

}
