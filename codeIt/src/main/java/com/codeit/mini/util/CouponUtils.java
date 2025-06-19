package com.codeit.mini.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CouponUtils {

    private static final Pattern COUNT_PATTERN = Pattern.compile("(\\d+)\\s*회");
    private static final Pattern PERCENT_PATTERN = Pattern.compile("(\\d+)%");

    public static int extractCountFromName(String name) {
        if (name == null) return 1;
        
        Matcher matcher = COUNT_PATTERN.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 1;
    }
    
    public static int extractDiscountRate(String name) {
        if (name == null) return 0;
        Matcher matcher = PERCENT_PATTERN.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
