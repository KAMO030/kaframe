package proxy_test;

import com.kamo.context.annotation.ComponentScan;
import com.kamo.context.annotation.Configuration;
import com.kamo.proxy.annotation.EnableProxy;

@Configuration
@ComponentScan
@EnableProxy(path = "proxy_test")
public class Config {

    public void test(){
        System.out.println(11);
    }
}