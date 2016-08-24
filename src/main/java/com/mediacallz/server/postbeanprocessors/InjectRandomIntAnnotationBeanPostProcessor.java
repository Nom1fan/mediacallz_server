package com.mediacallz.server.postbeanprocessors;

import com.mediacallz.server.annotations.InjectRandInt;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Created by Evegeny on 17/07/2016.
 */
@Component
public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectRandInt.class)) {
                Random random = new Random();
                InjectRandInt annotation = field.getAnnotation(InjectRandInt.class);
                int min = annotation.min();
                int max = annotation.max();
                int value = min + random.nextInt(max - min);
                field.setAccessible(true);
                ReflectionUtils.setField(field,bean,value);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}





