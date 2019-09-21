package com.example.a21__void.afroturf.pkgCommon;

/**
 * Created by ASANDA on 2018/12/04.
 * for Pandaphic
 */
public final class APIConstants {
    public static final String FIELD_USER_ID = "userId"
            , FIELD_SALON_ID = "salonId"
            , FIELD_SALON_OBJ_ID = "salonObjId"
            , FIELD_DATA = "data"
            , FIELD_USER_DATA = "userData"
            , FIELD_UID = "_id"
            , FIELD_NAME = "fname"
            , FIELD_TOKEN = "token"
            , FIELD_INFO = "info"
            , FIELD_TITLE = "title"

            , FIELD_USER_TYPE = "user_type"
            , FIELD_TYPE = "type"

            , FIELD_USERNAME = "username"
            , FIELD_PASSWORD = "password";

    public static final int HTTP_BAD_REQUEST = 400
                            , HTTP_INTERNAL_SERVER_ERROR = 500;
    public static final int NETWORK_ERROR = -1;
    public static final int TIME_OUT = 30 * 1000;

    public static final String MSG_NO_CONNECTION = "We couldn't establish a internet connection. Please make sure your wifi or mobile data is turned on and try again"
                                ,  MSG_COMMUNICATION_ERROR = "There was a problem in communication with our servers, we apologize for the inconvenience. Please try again later,";
    public static final String TITLE_NO_CONNECTION = "No Connection"
                                , TITLE_COMMUNICATION_ERROR = "Communication Error";

}
