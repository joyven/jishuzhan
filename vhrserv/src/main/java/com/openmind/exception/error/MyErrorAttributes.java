package com.openmind.exception.error;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * MyErrorAttributes
 *
 * @author zhoujunwen
 * @date 2020-01-07
 * @time 11:11
 * @desc
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.put("custommsg", "出错啦！");
        errorAttributes.remove("error");
        return errorAttributes;
    }
}
