package converter_registry_test;

import com.kamo.bean.annotation.*;
import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.context.converter.Converter;
import test.pojo.Ctype;

import java.util.Date;

@Configuration
//@Import(Test.StringCtypeConverter.class)
@Service
public class Test {
@Autowired("2021-1-2")
private static Date getString;
    public static void main(String[] args)  {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class,ConverterConfig.class);
        String getString1 = context.getBean("getString1");
        System.out.println(getString);
        System.out.println(getString1);

    }
    @Bean
    public String getString1(@Arg(name = "ctype",value = "2,饮料") Ctype ctype){
        return ctype.toString();
    }
    public static class StringCtypeConverter implements Converter<String,Ctype>{

        @Override
        public Ctype convert(String value) {
            Ctype ctype = new Ctype();
            String[] split = value.split(",");
            ctype.settId(split[0]);
            ctype.settName(split[1]);
            return ctype;
        }
    }

}
