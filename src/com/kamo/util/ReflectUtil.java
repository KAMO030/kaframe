package com.kamo.util;

import com.kamo.context.env.PropertyParser;
import com.kamo.util.exception.ReflectException;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ReflectUtil {
    private ReflectUtil() {
    }

    public static Type[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            return pt.getActualTypeArguments();
        }
        return new Type[]{type};
    }

    public static Class getActualTypeArgument(Type type) {
        return (Class) getActualTypeArguments(type)[0];
    }

    public static Type[] getActualTypesOnInterface(Class targetType, String interfaceName) throws IllegalArgumentException {
        Type[] genericInterfaces = targetType.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType && genericInterface.getTypeName().startsWith(PropertyParser.class.getName())) {
                return ReflectUtil.getActualTypeArguments(genericInterface);
            }
        }
        throw new IllegalArgumentException(targetType + " 没有实现：" + interfaceName);
    }

    public static Class getActualTypeOnInterface(Class targetType, String interfaceName) throws IllegalArgumentException {
        return (Class) getActualTypesOnInterface(targetType, interfaceName)[0];
    }

    public static <T> Class getWrapperClass(Class<T> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        String typeName = type.getName();
        if (typeName.equals("byte"))
            return Byte.class;
        if (typeName.equals("short"))
            return Short.class;
        if (typeName.equals("int"))
            return Integer.class;
        if (typeName.equals("long"))
            return Long.class;
        if (typeName.equals("char"))
            return Character.class;
        if (typeName.equals("float"))
            return Float.class;
        if (typeName.equals("double"))
            return Double.class;
        if (typeName.equals("boolean"))
            return Boolean.class;
        if (typeName.equals("void"))
            return Void.class;
        throw new IllegalArgumentException("Not primitive type :" + typeName);
    }

    public static <T> T parseString(Class<T> type, String value) {
        if (!isPrimitive(type)) {
            throw new IllegalArgumentException("[" + type + "]类型不是原始类型或其包装类");
        }
        Object parsed = null;
        if (type == String.class) {
            parsed = value;
        } else if (type == int.class || type == Integer.class) {
            parsed = Integer.valueOf(value);
        } else if (type == float.class || type == Float.class) {
            parsed = Float.valueOf(value);
        } else if (type == long.class || type == Long.class) {
            parsed = Long.valueOf(value);
        } else if (type == double.class || type == Double.class) {
            parsed = Double.valueOf(value);
        } else if (type == boolean.class || type == Boolean.class) {
            parsed = Boolean.valueOf(value);
        } else if (type == char.class || type == Character.class) {
            parsed = value.charAt(0);
        } else if (type == byte.class || type == Byte.class) {
            parsed = Byte.valueOf(value);
        }
        return (T) parsed;
    }

    public static boolean isPrimitive(Class type) {
        Objects.requireNonNull(type);
        if (type.isPrimitive() || String.class.equals(type)) {
            return true;
        }
        try {
            type.getDeclaredField("TYPE");
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static Object invokeMethod(Method method, Object instance, Object... args) {
        try {
            Objects.requireNonNull(method);
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (Exception e) {
            throw new ReflectException("执行 " + instance + " 的 " + method + " 方法时发生异常,实参为: " + Arrays.toString(args), e);
        }
    }

    public static Object invokeMethod(Object instance, String methodeName, Object... args) {
        Class[] parameterTypes = new Class[args.length];
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        Method method = getMethod(instance.getClass(), methodeName, parameterTypes);
        return invokeMethod(method, instance, args);


    }

    public static Object getFieldValue(Field field, Object instance) {
        try {
            Objects.requireNonNull(field);
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    public static void setFieldValue(Field field, Object instance, Object value) {
        try {
            Objects.requireNonNull(field);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectException(e);
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            Objects.requireNonNull(constructor);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }


    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public static Method getMethod(Class type, String methodName, boolean isSearchSuper, Class... parameterTypes) {
        try {
            return type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class superclass = type.getSuperclass();
            if (!isSearchSuper || superclass == null || superclass.equals(Object.class)) {
                throw new ReflectException(e);
            }
            AtomicReference<Method> methodAtomic = new AtomicReference<>();
            forEachSuperclass(superclass, t -> {
                try {
                    methodAtomic.set(t.getDeclaredMethod(methodName, parameterTypes));
                    return true;
                } catch (NoSuchMethodException ex) {
                    return false;
                }
            });
            Method method = methodAtomic.get();
            if (method == null) {
                throw new ReflectException(e);
            }
            return method;
        }
    }

    public static Method getMethod(Class type, String methodName, Class... parameterTypes) {
        return getMethod(type, methodName, false, parameterTypes);
    }

    public static Field getField(Class type, String fieldName, boolean isSearchSuper) {
        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superclass = type.getSuperclass();
            if (!isSearchSuper || superclass == null || superclass.equals(Object.class)) {
                throw new ReflectException(type.getName(), e);
            }
            AtomicReference<Field> fieldAtomic = new AtomicReference<>();
            forEachSuperclass(superclass, t -> {
                try {
                    fieldAtomic.set(t.getDeclaredField(fieldName));
                    return true;
                } catch (NoSuchFieldException ex) {
                    return false;
                }
            });
            Field field = fieldAtomic.get();
            if (field == null) {
                throw new ReflectException(type.getName(), e);
            }
            return field;
        }
    }

    public static Field getField(Class type, String fieldName) {
        return getField(type, fieldName, false);
    }

    public static void forEachField(Class type, Function<Field, Boolean> function) {
        forEachSuperclass(type, c -> {
            Field[] fields = c.getDeclaredFields();
            boolean isBreak = false;
            for (Field field : fields) {
                isBreak = function.apply(field);
                if (isBreak) {
                    break;
                }
            }
            return isBreak;
        });
    }

    public static void forEachMethod(Class type, Function<Method, Boolean> function) {
        forEachSuperclass(type, c -> {
            Method[] methods = c.getDeclaredMethods();
            boolean isBreak = false;
            for (Method method : methods) {
                isBreak = function.apply(method);
                if (isBreak) {
                    break;
                }
            }
            return isBreak;
        });
    }

    public static void forEachSuperclass(Class type, Function<Class, Boolean> function) {
        Objects.requireNonNull(type);
        do {
            if (function.apply(type)) {
                break;
            }
        } while ((type = type.getSuperclass()) != Object.class && type != null);
    }

    public static Constructor getConstructor(Class targetClass, Class... parameterTypes) {
        try {
            return targetClass.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    public static Constructor getConstructor(Class targetClass) {
        return getConstructor(targetClass, new Class[0]);
    }

    public static void forEachArray(Object array, Consumer consumer) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException(array + " 不是数组类型");
        }
        try {
            for (int i = 0; true; i++) {
                consumer.accept(Array.get(array, i));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public static Object collection2Array(Collection collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(collection + " collection为空");
        }
        return array2Array(collection.toArray());
    }

    public static Object[] array2Array(Object[] fromArray) {
        if (fromArray.length < 1) {
            throw new IllegalArgumentException(fromArray + " 数组为空");
        }
        Class componentType = fromArray[0].getClass();
        Object toArray = Array.newInstance(componentType, fromArray.length);
        array2Array(toArray, fromArray);
        return (Object[]) toArray;
    }

    public static void collection2Array(Object array, Collection collection) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException(array + " 不是数组类型");
        }
        array2Array(array, collection.toArray());
    }

    public static void array2Array(Object toArray, Object[] fromArray) {
        if (!toArray.getClass().isArray()) {
            throw new IllegalArgumentException(toArray + " 不是数组类型");
        }
        System.arraycopy(fromArray, 0, toArray, 0, fromArray.length);
    }

    public static boolean isCollection(Class type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class type) {
        return Map.class.isAssignableFrom(type);
    }

    public static Class loadClass(ClassLoader classLoader, String beanType) {
        try {
            return classLoader.loadClass(beanType);
        } catch (ClassNotFoundException e) {
            throw new ReflectException(e);
        }
    }


}
