package kamo.cloud;

import java.util.List;
import java.util.Random;

public class LoadBalance {

    public static Invoker random(List<Invoker> list) {
        Random random =new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }
}
