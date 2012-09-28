package org.motechproject.whp.integration;

import org.motechproject.event.ServerEventRelay;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class Replacer implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if (bean instanceof ServerEventRelay) {
            System.out.println("replaced with Stub Event relay");
            return new StubEventRelay((ServerEventRelay) bean);
        } else
            return bean;
    }
}
