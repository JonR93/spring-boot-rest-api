package com.springboot.rest.api.server.utils;

public class AppConstants {

    private AppConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String NO_REPLY_EMAIL_ADDRESS = "springbootapinoreplyemail@something.com";

    public static final String DEFAULT_PAGE_NUMBER = "0";
    public  static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    public static final int DEFAULT_PASSWORD_RESET_TOKEN_EXPIRATION_MINUTES = 15;
}
