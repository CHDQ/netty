package org.dq.netty.netty.chatroom.frame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 路由到mapping
 */
@Component
public class RouteMapping implements ApplicationContextAware {
    private Logger log = LoggerFactory.getLogger(RouteMapping.class);
    private ApplicationContext applicationContext;

    public void pushDataToRouteMap(String key, Method value) {
        this.routeMap.put(key, value);
    }

    private Map<String, Method> routeMap = new HashMap<>();//实际的应用，value应该存储该method的相关信息，而不是method对象

    public void makeRoute(String msg) throws Exception {
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
                /**
                 * 参数处理没有做过多的验证比较，只是简单的使用字符串传递，如果需要完善的话，需要比较验证参数类型,通过is开头的函数进行比较
                 */
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] parameter = new Object[parameterTypes.length];
                parameter[0] = jsonObject.get("content");
                method.invoke(applicationContext.getBean(method.getDeclaringClass()), parameter);//反射调用
            } else {
                log.error("makeRoute  msg cannot convert to json. content: " + msg);
                throw new Exception("makeRoute  msg cannot convert to json");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void initRouteMap() {
        synchronized (RouteMapping.class) {

//            log.info("init mapping");
//            routeMap = new HashMap<>();
            /**
             * 不稳定
             * MethodAnnotationsScanner指定扫描方法
             * TypeAnnotationsScanner指定扫描类
             * SubTypesScanner指定扫描子类
             */
//            Reflections reflections = new Reflections("org.dq.netty.netty", new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());//扫描org.dq.netty包下面的类,MethodAnnotationsScanner指定了扫描方法，将会无法扫描到类，默认是扫描类
//            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(WSMapping.class);//获取所有WSMapping注解标注的类
//            Set<Method> methodSet = reflections.getMethodsAnnotatedWith(WSMapping.class);//获取所有注解标记的方法
//            methodSet.forEach(method -> {
//                String controllerName = method.getDeclaringClass().getAnnotation(WSMapping.class).value();
//                String methodName = method.getAnnotation(WSMapping.class).value();
//                routeMap.put(controllerName + "/" + methodName, method);
//            });
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
