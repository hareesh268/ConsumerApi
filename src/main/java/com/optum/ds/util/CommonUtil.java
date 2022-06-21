package com.optum.ds.util;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.security.SecureRandom;

public class CommonUtil {

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class.getCanonicalName());

    public static String generateUhcProviderID(String mpin) {
        int number = randomNumber(800) + 100;
        return mpin  + number;
    }

    public static String generateSecurityKey() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public static String setOrgName(String firstName, String lastName){
        StringBuilder orgName = new StringBuilder();
        orgName.append(lastName);
        if(StringUtils.isNotBlank(firstName)){
            orgName.append(", ");
            orgName.append(firstName);
        }
        return orgName.toString();
    }

    private static int randomNumber(int number)
    {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(number);
    }



}
