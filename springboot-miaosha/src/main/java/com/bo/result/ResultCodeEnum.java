package com.bo.result;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(true,20000,"成功"),

    //通用错误
    UNKNOWN_ERROR(false,20001,"未知错误"),
    PARAM_ERROR(false,20002,"参数错误"),
    NULL_POINT(false,20003,"空指针异常"),
    HTTP_CLIENT_ERROR(false,20004,"客户端异常"),
    BIND_ERROR(false, 20005, "绑定异常:%s"),
    ACCESS_COUNT_ERROR(false, 20005, "访问太频繁"),

    //用户异常
    USER_EXISTS(false,30001,"该用户已注册"),
    USER_NOT_EXISTS(false, 30002, "该邮箱尚未注册"),
    USER_PASSWORD_ERROR(false, 30002, "用户密码错误"),
    USER_NO_LOGIN(false,30003,"请先登录"),
    CHECKCODE_ERROR(false,30004,"验证码错误"),

    //商品异常
    GOODS_NO_STOCK(false,40001,"库存不足"),
    GOODS_REPEAT_KILL(false,40002,"不可重复秒杀"),
    GOODS_NOT_EXISTS(false, 40003, "商品不存在"),

    //秒杀异常
    MIAOSHA_NOT_START(false, 50001, "秒杀尚未开始"),
    MIAOSHA_REPEAT_REQUEST(false, 50002, "秒杀二次请求"),
    MIAOSHA_IS_OVER(false, 50003, "秒杀已结束");

    private Boolean success;//响应是否成功
    private Integer code;   //响应状态码
    private String message; //响应信息

    ResultCodeEnum(boolean success,Integer code,String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
