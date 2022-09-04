package com.kamo.context.condition;

import com.kamo.context.Aware;

public interface ConditionMatcherAware extends Aware {
    void setConditionMatcher(ConditionMatcher conditionMatcher);

    @Override
    default Class[] getAwareTypes() {
        return new Class[] {ConditionMatcher.class};
    }

    @Override
   default void setAware(Object... awareValue){
        setConditionMatcher((ConditionMatcher) awareValue[0]);
    };
}
