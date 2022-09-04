package env_test.mian;

import com.kamo.context.env.impl.DefaultEnvironment;
import com.kamo.context.env.impl.PropertiesPropertyParser;

public class EnvTest {
    public static void main(String[] args) throws Exception {
//        DefaultEnvironment env = new DefaultEnvironment();
//        String key = "123.";
//        env.setProperty(key, 123);
//        System.out.println(env.containsProperty("123."));
        System.out.println(new PropertiesPropertyParser().propertySourceType());
    }
}
