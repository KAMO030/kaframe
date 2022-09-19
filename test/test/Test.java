package test;

import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class Test {


    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {

        AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(A.class);
        annotationConfigApplicationContext.getBean(MyClassLoader.class).t();
//        System.out.println(bean.test("1"));
//        Service bean = (Service) annotationConfigApplicationContext.getBean(Service.class);
//        System.out.println(bean);
//
//        B myClassLoader = (B) annotationConfigApplicationContext.getBean("myClassLoader");
//        System.out.println(Arrays.toString(A.class.getDeclaredAnnotations()));
//        B d = (B) annotationConfigApplicationContext.getBean("d");
//        myClassLoader.t();
//        d.t();
//
//        System.out.println(d);
//        System.out.println(myClassLoader);
//        System.out.println(annotationConfigApplicationContext.getBean("provider"));
//        System.out.println(B.class.isAssignableFrom(D.class));
//        System.out.println(annotationConfigApplicationContext.getBean("d"));
//       test.t();
//        Supplier v = new Supplier() {
//
//            @Override
//            public Object get() {
//                return null;
//            }
//        };
//        String sql = "select * from b";
//        Consumer consumer = o -> System.out.println(o.toString());
//        consumer.andThen(o -> System.out.println(o.toString()));
    }
}
