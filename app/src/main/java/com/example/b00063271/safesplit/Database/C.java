package com.example.b00063271.safesplit.Database;

public class C {

    public static final double round(double value){ return Math.round(value * 100.0) / 100.0; }

    public static final String COLLECTION_TRANSACTION = "transaction";
    public static final String TRANSACTION_FROM_ID = "fromID";
    public static final String TRANSACTION_FROM = "from";
    public static final String TRANSACTION_TO_ID = "toID";
    public static final String TRANSACTION_TO = "to";
    public static final String TRANSACTION_AMOUNT = "amount";

    public static final String COLLECTION_USERS = "users";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_MOBILE = "mobile";
    public static final String USERS_NAME = "name";
    public static final String USERS_TRANSACTIONS = "transactionIds";

    public static final String COLLECTION_USERS_HISTORY = "history";
    public static final String USERS_HISTORY_ACTIVITY = "activityString";
    public static final String USERS_HISTORY_AMOUNT = "amount";
    public static final String USERS_HISTORY_TYPE = "activityType";
    public static final String USERS_HISTORY_TIMESTAMP = "timeStamp";
    public static final String USERS_HISTORY_TOS = "to";

    public static final String LOCAL_FILE_NAME = "userStuff";
    public static final String NO_INTERNET_BROADCAST = "noInternet";
    public static final String INTERNET_BROADCAST = "internet";

    public static final String SETTLE_UP = "Settle Up";

    public static final int ACTIVITY_TYPE_SETTLE_UP = 1030;
    public static final int ACTIVITY_TYPE_UPDATE_PROFILE = 1031;

    public static final int CALLBACK_GET_TRANSACTIONS = 35;
    public static final int CALLBACK_GET_USER_EMAIL = 36;
    public static final int CALLBACK_SET_USER_EMAIL = 37;
    public static final int CALLBACK_SET_USER_PASSWORD = 38;
    public static final int CALLBACK_ADD_USER = 39;
    public static final int CALLBACK_CREATE_TRANSACTION = 40;
    public static final int CALLBACK_CHANGED_CONNECTION = 42;
    public static final int CALLBACK_DELETE_TRANSACTION = 41;
}
