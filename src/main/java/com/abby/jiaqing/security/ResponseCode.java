package com.abby.jiaqing.security;

public class ResponseCode {
    //用户验证返回码
    public static final int BAD_CREDENTIALS=10000;

    public static final int USER_NOT_LOGGED_IN=10001;

    //用户在线状态返回码
    public static final int USER_IS_ONLINE=20001;

    public static final int USER_IS_OFFLINE=20002;

    //
    public static final int UPLOAD_IMAGE_SUCCESS=30000;

    public static final int IMAGE_NULL=30001;

    public static final int IMAGE_NAME_NULL=30002;

    public static final int IMAGE_DAMAGED=30003;

    public static final int UPLOAD_RESULT_WRITE_FAILED=30004;

    public static final int UPLOAD_IMAGE_FAILED=30005;
}
