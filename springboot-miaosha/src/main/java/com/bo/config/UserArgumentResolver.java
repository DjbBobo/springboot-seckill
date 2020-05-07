package com.bo.config;

import com.bo.exception.CMSException;
import com.bo.pojo.MiaoshaUser;
import com.bo.result.ResultCodeEnum;
import com.bo.service.MiaoshaUserService;
import com.bo.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *自定义参数解析，检验用户是否登陆
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private CookieUtil cookieUtil;

    /**
     * 用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    /**
     * 真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        //获取浏览器的Cookie:token
        String cookieToken = cookieUtil.getCookieToken(request);
        if (StringUtils.isEmpty(cookieToken)){
            throw new CMSException(ResultCodeEnum.USER_NO_LOGIN);
        }

        return miaoshaUserService.getUserByToken(response,cookieToken);
    }


}
