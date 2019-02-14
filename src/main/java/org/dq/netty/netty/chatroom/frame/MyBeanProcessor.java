package org.dq.netty.netty.chatroom.frame;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * 对象实例化的时候会调用
 */
@Component
public class MyBeanProcessor implements BeanPostProcessor {
    @Autowired
    private RouteMapping routeMapping;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        var annotation = AnnotationUtils.findAnnotation(bean.getClass(), WSMapping.class);
        if (annotation == null) {
            return bean;
        }
        var controllerName = annotation.value();
        //common-lang也有一样的处理method的方法
        ReflectionUtils.doWithMethods(bean.getClass(), method -> {
            if (method.getAnnotation(WSMapping.class) != null) {
                routeMapping.pushDataToRouteMap(controllerName + "/" + method.getName(), method);
            }
        });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
