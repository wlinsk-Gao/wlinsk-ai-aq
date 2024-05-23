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
    SYS_URL_UNAUTHORIZED("9990", "权限不足"),
    DATA_NOT_FOUND("9989", "数据不存在"),

    PARAMETER_ERROR("9000", "Parameter validation error"),
    USER_ACCOUNT_ALREADY_EXIST("10000", "账号已存在"),
    USER_ACCOUNT_NOT_EXIST("10001", "账号不存在"),
    USER_ACCOUNT_PASSWORD_ERROR("10002", "账号或密码有误，请重新输入"),
    USER_ACCOUNT_PASSWORD_ERROR_TIMES_EXCEED("10003", "Account or password error times exceed"),
    USER_ACCOUNT_PASSWORD_ERROR_TIMES_EXCEED_LOCK("10004", "Account or passworderror times exceed, account locked"),
    USER_REGISTER_ERROR("10005","用户注册失败，请稍后重试"),
    USER_DISABLED("10006","当前用户已被禁用，请联系管理员处理"),
    APP_REVIEW_STATUS_HAS_CHANGED("11000","应用状态已修改"),

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
