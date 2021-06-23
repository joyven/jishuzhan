package com.openmind.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AviatorScriptTest
 *
 * @author zhoujunwen
 * @date 2021-06-10 14:55
 * @desc
 */
public class AviatorScriptTest {
    public static void main(String[] args) throws IOException {
        String file = AviatorScriptTest.class.getResource("/hello.av").getPath();
        System.out.println(file);
        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Map<String, Object> env = new HashMap<String, Object>();
            final Date date = new Date();
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS").format(date);
            env.put("date", date);
            env.put("dateStr", dateStr);

            Boolean result = (Boolean) AviatorEvaluator.execute("date==dateStr", env);
//            System.out.println(result);

            result = (Boolean) AviatorEvaluator.execute("date > '2009-12-20 00:00:00:00' ", env);
//            System.out.println(result);

            result = (Boolean) AviatorEvaluator.execute("date < '2200-12-20 00:00:00:00' ", env);
//            System.out.println(result);

            result = (Boolean) AviatorEvaluator.execute("date == date ", env);
//            System.out.println(result);
        }
        System.out.println(String.format("aviator script:%d ms", System.currentTimeMillis() - s));
    }
}
