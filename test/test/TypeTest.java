package test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class TypeTest<T> {
    private int i;
    private Integer it;
    private int[] iarray;
    private List list;
    private List<String> slist;
    private List<T> tlist;
    private T t;
    private T[] tarray;
    public static void main(String[] args) throws NoSuchFieldException {

        test(TypeTest.class.getDeclaredField("i"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("it"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("iarray"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("list"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("slist"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("tlist"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("t"));
        System.out.println("=======");
        test(TypeTest.class.getDeclaredField("tarray"));

    }


    public static void test(Field field) {

        if (field.getType().isPrimitive()) {
            System.out.println(field.getName() + "是基本数据类型");
        } else {
            System.out.println(field.getName() + "不是基本数据类型");
        }

        if (field.getGenericType() instanceof ParameterizedType) {
            System.out.println(field.getName() + "是泛型类型");
        } else {
            System.out.println(field.getName() + "不是泛型类型");
        }

        if (field.getType().isArray()) {
            System.out.println(field.getName() + "是普通数组");
        } else {
            System.out.println(field.getName() + "不是普通数组");
        }

        if (field.getGenericType() instanceof GenericArrayType) {
            System.out.println(field.getName() + "是泛型数组");
        } else {
            System.out.println(field.getName() + "不是泛型数组");
        }

        if (field.getGenericType() instanceof TypeVariable) {
            System.out.println(field.getName() + "是泛型变量");
        } else {
            System.out.println(field.getName() + "不是泛型变量");
        }

    }

}
