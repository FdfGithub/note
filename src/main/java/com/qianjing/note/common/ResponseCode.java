package com.qianjing.note.common;

/**
 * Created by geely
 */
public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    CODE_INVALID(100,"CODE_INVALID"),
    CODE_FAIL(101,"CODE_FAIL"),
    PASSWORD_FAIL(102,"PASSWORD_FAIL"),
    AUTH_TIMEOUT(103,"AUTH_TIMEOUT"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),

    ;

    private final int code;
    private final String desc;


    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}
