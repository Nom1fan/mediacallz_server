package com.mediacallz.server.utils;

/**
 * Created by Mor on 03/06/2017.
 */
public interface PhoneNumberUtils {

    String toValidLocalPhoneNumber(String str);

    String toValidInternationalPhoneNumber(String str, Country country);

    String toNumeric(String str);

    boolean isNumeric(String str);

    boolean isValidPhoneNumber(String destPhone);

    enum Country {
        IL,
    }
}
