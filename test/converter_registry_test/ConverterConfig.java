package converter_registry_test;

import com.kamo.bean.annotation.Arg;
import com.kamo.bean.annotation.Bean;
import com.kamo.bean.annotation.Configuration;
import test.pojo.Ctype;

@Configuration
public class ConverterConfig {
    @Bean
    public String getString(@Arg(name = "ctype",value = "1,饮料") Ctype ctype){
        return ctype.toString();
    }

}
