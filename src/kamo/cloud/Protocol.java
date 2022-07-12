package kamo.cloud;
import kamo.cloud.URL;
public interface Protocol {

    void export(URL url);
    Invoker refer(URL url);
}
