package com.mediacallz.server.utils;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * Created by Mor on 18/02/2016.
 */
@Component
public class PhoneNumberUtilsImpl implements PhoneNumberUtils {

    @Override
    public String toValidLocalPhoneNumber(String str) {

        if(str!=null) {
            str = toNumeric(str);

            // TODO deal with other countries
            if (str.startsWith("9720")) {
                str = str.replaceFirst("9720", "0");
            }
            if (str.startsWith("972")) {
                str = str.replaceFirst("972", "0");
            }

            return str;
        } else
            return "";
    }

    @Override
    public String toValidInternationalPhoneNumber(String str, Country country) {

        if(str!=null) {

            str = toNumeric(str);

            switch (country) {

                case IL:
                    if (str.startsWith("0"))
                        str = str.replaceFirst("0", "972");
                    break;

                default:
                    System.out.print("WARNING: No such country:" + country);
            }
        }

        return str;
    }

    @Override
    public String toNumeric(String str) {

        if (str != null)
            str = str.replaceAll("[^0-9]", "");
        return str;
    }

    @Override
    public boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    @Override
    public boolean isValidPhoneNumber(String destPhone) {

        if (destPhone != null) { // if it's null it's a Private Number (incoming calls)
            boolean lengthOK = 10 == destPhone.length();
            boolean isNumeric = isNumeric(destPhone);
            boolean isValidPrefix = destPhone.startsWith("0"); //TODO deal with other countries

            return lengthOK && isNumeric && isValidPrefix;
        } else
            return false;
    }
}
