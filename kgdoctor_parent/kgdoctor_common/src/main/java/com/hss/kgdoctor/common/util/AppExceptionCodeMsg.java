package com.hss.kgdoctor.common.util;

//这个枚举类中定义的都是跟业务有关的异常
public enum AppExceptionCodeMsg {
    UNLOGIN(40001,"用户未登录"),
    INVALID_CODE(10000,"验证码无效"),
    USERNAME_NOT_EXISTS(10001,"用户名不存在"),
    ADD_HOSPITAL_FAIL(20001,"新增医院失败"),
    UPDATE_HOSPITAL_FAIL(20002,"修改医院失败"),
    BIND_HOSPITAL_DEPARTMENT_FAIL(20003,"为医院绑定科室失败"),
    ADD_DOCTOR_FAIL(20004,"新增医生失败"),
    BIND_USER_FAIL(20006,"绑定用户失败"),
    ACCOUNT_NOT_EXIT(20005,"账户不存在"),
    DCOTOR_NOT_EXIT(20007,"医生不存在"),
    INQUIRY_CHANGE_FAIL(20008,"状态转换失败，请重试"),
    REPEAT_CREATE_REGISTER(20009,"重复创建错误"),
    UPLOAD_TIME_INVALID(20010,"失败:不符合时间！"),
    REPEAT_UPLOAD_EXCEPTION(20011,"失败:请勿重复上传！"),
    PRODUCT_NOT_EXITS(30001,"挂号不存在！"),
    REPEAT_DO_SECKILL(30002,"请勿重复挂号"),
    STOCK_OUT_OF_COUNT(30003,"库存不足"),
    SECKILL_TIME_EXCEPTION(30002,"未到抢购时间"),
    USER_CREDIT_NOT_ENOUTH(10002,"用户积分不足");
    ;

    private int code ;
    private String msg ;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    AppExceptionCodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}