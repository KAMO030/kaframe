package env_test.mian;

import com.kamo.context.env.impl.DefaultEnvironment;
import com.kamo.context.env.impl.PropertiesPropertyParser;

public class EnvTest {
    public static void main(String[] args) throws Exception {
        DefaultEnvironment env = new DefaultEnvironment();
        String key = "123.55";
        env.setProperty(key, 123);
        System.out.println(env.getProperty("123.66", 312));
//        System.out.println(new PropertiesPropertyParser().propertySourceType());
    }
}
