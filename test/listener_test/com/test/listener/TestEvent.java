package listener_test.com.test.listener;

import com.kamo.context.listener.ApplicationEvent;

public class TestEvent extends ApplicationEvent {
    public TestEvent(Object source) {
        super(source);
    }
}