package com.kamo.cglib.asm;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.io.*;

public class AsmUtil implements Opcodes {
    public static boolean isInitMethod(String methodName) {
        return "<init>".equals(methodName);
    }

    public static boolean isStaticBlock(String methodName) {
        return "<clinit>".equals(methodName);
    }

    public static int getMethodArgSize(Type[] argumentTypes) {

        int size = 0;
        for (int i = 0; i < argumentTypes.length; i++, size++) {
            if (isJorD(argumentTypes[i])) {
                size++;
            }
        }
        return size;
    }
    public static int getMethodArgSize(String methodDesc) {
        Type[] argumentTypes = Type.getMethodType(methodDesc).getArgumentTypes();

        return getMethodArgSize(argumentTypes);
    }

    public static boolean isJorD(Type argumentType) {
        return argumentType.equals(Type.LONG_TYPE) || argumentType.equals(Type.DOUBLE_TYPE);
    }
    public static void loadInsn(MethodVisitor methodVisitor, String methodDesc, boolean isIncludeThis) {
        Type[] argumentTypes = Type.getMethodType(methodDesc).getArgumentTypes();

        int loadIndex = 0;
        if (isIncludeThis) {
            methodVisitor.visitVarInsn(ALOAD, loadIndex++);
        }
        for (int i = 0; i < argumentTypes.length ; i++, loadIndex++) {
            String classDesc = argumentTypes[i].getDescriptor();
            if (classDesc.equals("J")) {
                methodVisitor.visitVarInsn(LLOAD, loadIndex);
                loadIndex++;
            } else if (classDesc.equals("D")) {
                methodVisitor.visitVarInsn(DLOAD, loadIndex);
                loadIndex++;
            } else if (isPrimitive(classDesc)) {
                methodVisitor.visitVarInsn(ILOAD, loadIndex);
            } else {
                methodVisitor.visitVarInsn(ALOAD, loadIndex);
            }
        }
    }

    public static boolean isPrimitive(Type type){
        return type.getDescriptor().length() == 1;
    }
    public static boolean isPrimitive(String classDesc){
        return classDesc.length() == 1;
    }

    public static void writeBytes(String filepath, byte[] bytes) {
        File file = new File(filepath);
        File dirFile = file.getParentFile();
        mkdirs(dirFile);

        try (OutputStream out = new FileOutputStream(filepath);
             BufferedOutputStream buff = new BufferedOutputStream(out)) {
            buff.write(bytes);
            buff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void mkdirs(File dirFile) {
        boolean file_exists = dirFile.exists();

        if (file_exists && dirFile.isDirectory()) {
            return;
        }

        if (file_exists && dirFile.isFile()) {
            throw new RuntimeException("Not A Directory: " + dirFile);
        }

        if (!file_exists) {
            boolean flag = dirFile.mkdirs();
        }
    }
    public static void primitive2Wrapper(MethodVisitor methodVisitor, Type primitiveType) {
//        Type wrapperClass = AsmUtil.getWrapperClass(classType);
        String primitiveName = primitiveType.getClassName();
        Type wrapperType = AsmUtil.getWrapperType(primitiveType);
        String internalName = wrapperType.getInternalName();
        methodVisitor.visitMethodInsn(INVOKESTATIC, internalName, "valueOf", "(" + primitiveName + ")" + wrapperType.getDescriptor(), false);
    }

    public static Type getPrimitiveType(Type wrapperType) {
        String internalName = wrapperType.getInternalName();

        if (isPrimitive(wrapperType)&&internalName.startsWith("java/lang/")) {
            throw new IllegalArgumentException(wrapperType+" :不是包装类型");
        }
        String className = internalName.substring(10);
        if (className.equals("Byte"))
            return Type.BYTE_TYPE;
        if (className.equals("Short"))
            return Type.SHORT_TYPE;
        if (className.equals("Integer"))
            return Type.INT_TYPE;
        if (className.equals("Long"))
            return Type.LONG_TYPE;
        if (className.equals("Character"))
            return Type.CHAR_TYPE;
        if (className.equals("Float"))
            return Type.FLOAT_TYPE;
        if (className.equals("Double"))
            return Type.DOUBLE_TYPE;
        if (className.equals("Boolean"))
            return Type.BOOLEAN_TYPE;
        if (className.equals("Void"))
            return Type.VOID_TYPE;
        throw new IllegalArgumentException("Not primitive type :" + className);
    }
    public static Type getWrapperType(Type primitiveType) {
        if (!isPrimitive(primitiveType)) {
            throw new IllegalArgumentException(primitiveType+" :不是原始型");
        }
        String className = primitiveType.getClassName();
        if (className.equals("byte"))
            return Type.getType(Byte.class);
        if (className.equals("short"))
            return Type.getType(Short.class);
        if (className.equals("int"))
            return Type.getType(Integer.class);
        if (className.equals("long"))
            return Type.getType(Long.class);
        if (className.equals("char"))
            return Type.getType(Character.class);
        if (className.equals("float"))
            return Type.getType(Float.class);
        if (className.equals("double"))
            return Type.getType(Double.class);
        if (className.equals("boolean"))
            return Type.getType(Boolean.class);
        if (className.equals("void"))
            return Type.getType(Void.class);
        throw new IllegalArgumentException("Not wrapper type :" + className);
    }
    public static void wrapper2Primitive(MethodVisitor methodVisitor, Type wrapperType) {
        String primitiveName = getPrimitiveType(wrapperType).getDescriptor();
        String internalName = wrapperType.getInternalName();
        String typeName = wrapperType.getInternalName().substring(10 );
        methodVisitor.visitTypeInsn(CHECKCAST, internalName);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, internalName, typeName+"Value", "()"+primitiveName, false);
    }
}
