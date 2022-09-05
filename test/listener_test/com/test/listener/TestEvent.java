package listener_test.com.test.listener;

import com.kamo.context.listener.ApplicationEvent;

public class TestEvent extends ApplicationEvent {
    String test1 = "test1";
    String test = "test";

    public TestEvent(Object source) {
        super(source);
    }
}