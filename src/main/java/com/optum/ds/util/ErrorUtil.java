package com.optum.ds.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorUtil {

    //Letter generation
    public static final String ERROR_CODE_LETTER_GENERATION_MULTIPLE_UHC_PROVIDERS_FOUND = "DSC:E:PLG:MUH";

    public static final String ERROR_MESSAGE_LETTER_GENERATION_MULTIPLE_UHC_PROVIDERS_FOUND = "Multiple uhcProviders found.";

    //Common for all functions.
    public static final String ERROR_TIME_FORMAT = "yyyy.MM.dd@hh:mm:ss.SSS";

    public static String errorTimeStamp () {
        SimpleDateFormat errorDateFormat = new SimpleDateFormat ( ERROR_TIME_FORMAT );
        Date dNow = new Date();
        return errorDateFormat.format ( dNow );
    }

}
