package com.kamo.context.listener;

import com.kamo.context.listener.impl.DefaultEventMulticaster;
import com.kamo.util.AnnotationUtils;
import com.kamo.util.ReflectUtil;

import java.lang.reflect.Type;
import java.util.EventListener;

@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    void onApplicationEvent(E event);
    default String[] getMulticasterNames(){
        return new String[] {DefaultEventMulticaster.DEFAULT_EVENT_MULTICASTER_NAME};
    }

    default Class getEventType() {
        try {
            for (Type genericInterface : this.getClass().getGenericInterfaces()) {
                if (genericInterface.getTypeName().startsWith(ApplicationListener.class.getName())) {
                    return ReflectUtil.getActualTypeArgument(genericInterface);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApplicationEvent.class;
        }
        return null;
    }
}
