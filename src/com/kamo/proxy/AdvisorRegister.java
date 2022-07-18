package com.kamo.proxy;


import java.util.ArrayList;
import java.util.List;

public  final class AdvisorRegister {
    private AdvisorRegister() {

    }
    private static final List<Advisor> ADVISOR_LIST = new ArrayList<>();
    public static void registerAdvisor(Advisor advisor){
        ADVISOR_LIST.add(advisor);
    };

    public static Advisor getAdvisor(int index){
        return ADVISOR_LIST.get(index);
    }

    public static int advisorSize() {
        return ADVISOR_LIST.size();
    }

    public static List<Advisor> getAdvisors() {
        return new ArrayList<>(ADVISOR_LIST);

    }
  public static boolean classFilter(Class aspClass){
      for (Advisor advisor : ADVISOR_LIST) {
          if (advisor.classFilter(aspClass)) {
              return true;
          }
      }
      return false;
  }
}
