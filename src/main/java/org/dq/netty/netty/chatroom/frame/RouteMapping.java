package org.dq.netty.netty.chatroom.frame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 路由到mapping
 */
public class RouteMapping {
    private static Logger log = LoggerFactory.getLogger(RouteMapping.class);
    private static Map<String, Method> routeMap;//实际的应用，value应该存储该method的相关信息，而不是method对象

    public static void makeRoute(String msg) throws Exception {
        try {
            //{'path':'chat/broadcastMsg','content':'test'}
            JSONObject jsonObject = JSON.parseObject(msg);
            if (jsonObject.containsKey("path") && jsonObject.containsKey("content")) {
                var path = (String) jsonObject.get("path");
                Method method = routeMap.get(path);
                if (method == null) {
                    log.error("makeRoute has no given path : {}", path);
                    throw new Exception("makeRoute has no given path");
                }
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();

                Object[] parameter = new Object[parameterTypes.length];
                parameter[0] = jsonObject.get("content");
                method.invoke(method.getDeclaringClass().getConstructor().newInstance(), parameter);//反射调用
            } else {
                log.error("makeRoute  msg cannot convert to json. content: " + msg);
                throw new Exception("makeRoute  msg cannot convert to json");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void initRouteMap() {
        log.info("init mapping");
        routeMap = new HashMap<>();
        /**
         * MethodAnnotationsScanner指定扫描方法
         * TypeAnnotationsScanner指定扫描类
         * SubTypesScanner指定扫描子类
         * 使用该方法的时候，不要在类上面应用lomhok注解，会导致扫描不到方法
         */
        Reflections reflections = new Reflections("org.dq.netty.netty", new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());//扫描org.dq.netty包下面的类,MethodAnnotationsScanner指定了扫描方法，将会无法扫描到类，默认是扫描类
//        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(WSMapping.class);//获取所有WSMapping注解标注的类
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(WSMapping.class);//获取所有注解标记的方法
        methodSet.forEach(method -> {
            String controllerName = method.getDeclaringClass().getAnnotation(WSMapping.class).value();
            String methodName = method.getAnnotation(WSMapping.class).value();
            routeMap.put(controllerName + "/" + methodName, method);
        });
    }
}
