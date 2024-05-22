package com.wlinsk.basic.exception;



public enum SysCode implements ReturnCode {
    success("00000", "success"),

    SYSTEM_ERROE("9999", "system error"),
    ENUM_ERROR("9998", "enum typeHandler error"),
    DATABASE_DELETE_ERROR("9997", "database delete result no 1"),
    DATABASE_INSERT_ERROR("9996", "database insert result no 1"),
    DATABASE_UPDATE_ERROR("9995", "database update result no 1"),
    REDIS_EXPIRED_TIME_ERROR("9994", "redis expired time error"),
    TRANSACTION_EXCEPTION("9993", "TransactionException"),
    HTTP_CLINT_ERROR("9992", "http clint error"),
    SYS_TOKEN_EXPIRE("9991", "Invalid token"),// token失效
    SYS_URL_UNAUTH("9990", "unauthorized"),// 权限不足
    DATA_NOT_FOUND("9989", "Data does not exist"),// 数据不存在

    PARAMETER_ERROR("9000", "Parameter validation error"),
    SYS_10000("10000", "Upcoming. Please contact 0968525555"),
    MQ_SYNC_SEND_ERROR("20001","mq sync send error"),
    MQ_ASYNC_SEND_ERROR("20002","mq async send error"),
    MQ_SEND_ONE_WAY_ERROR("20003","ma send one way error"),
    MQ_ASYNC_SEND_DELAY_ERROR("20004","mq async send delay error"),

    USER_ACCOUNT_ALREADY_EXIST("10001", "Account already exists"),
    USER_ACCOUNT_NOT_EXIST("10002", "Account does not exist"),
    USER_ACCOUNT_PASSWORD_ERROR("10003", "Account or password error"),
    USER_ACCOUNT_PASSWORD_ERROR_TIMES_EXCEED("10004", "Account or password error times exceed"),
    USER_ACCOUNT_PASSWORD_ERROR_TIMES_EXCEED_LOCK("10005", "Account or passworderror times exceed, account locked"),

    /**
     * 业务错误码
     */

    ;


    private final String code;

    private final String message;

    SysCode(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }
}
