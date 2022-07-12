package kamo.context;

import kamo.context.exception.NoSuchBeanDefinitionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanMatcher {
    private final Map<String,Object> sources;

    public BeanMatcher(Map<String, Object> sources) {
        this.sources = sources;
    }

    public boolean match(Class type){
        String[] beanNames = getBeanNamesByType(type);
        return beanNames.length>0;
    }
    public <T> T getMatchAndRemove(String name, Class<T> type){
        String beanName = null;
        String[] matchNames = getBeanNamesByType(type);
        if (matchNames.length == 0) {
            throw new NoSuchBeanDefinitionException("找不到类型为: " + type.getName() + " 的 Bean");
        }
        if (matchNames.length == 1) {
            beanName = matchNames[0];
        } else {
            for (String matchName : matchNames) {
                if (name.equals(matchName)) {
                    beanName = name;
                    break;
                }
            }
        }
        if (beanName == null) {
            throw new NoSuchBeanDefinitionException("找不到ID为: " + name + " 的 Bean");
        }
        return (T) sources.remove(beanName);
    }
    public String[] getBeanNamesByType(Class type){
        String[] beanDefinitionNames = sources.keySet().toArray(new String[0]);
        List<String> matchNames = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (isTypeMatch(beanDefinitionName, type)) {
                matchNames.add(beanDefinitionName);
            }

        }
        return matchNames.toArray(new String[0]);
    }

    private boolean isTypeMatch(String name, Class typeToMatch) {
        if (!sources.containsKey(name)) {

            return false;
        }
        return typeToMatch
                .isAssignableFrom(sources.get(name)
                        .getClass());
    }
}
