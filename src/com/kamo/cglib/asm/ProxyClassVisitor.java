package com.kamo.cglib.asm;

import jdk.internal.org.objectweb.asm.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class ProxyClassVisitor extends ClassVisitor implements Opcodes {

    private String superName;
    private String proxyClassName;
    private Map<String,Type> methodFieldMapping;
    private String handlerName;

    public ProxyClassVisitor(int api, ClassVisitor classVisitor, String proxyClassName) {
        super(api, classVisitor);
        this.proxyClassName = proxyClassName;
        this.methodFieldMapping = new HashMap<>();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return null;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.superName = name;

        this.handlerName = "handler$Proxy$" +Math.abs(this.superName.hashCode()) ;
        super.visit(version, access, proxyClassName, null, name, null);
        FieldVisitor handlerField = super.visitField(ACC_PRIVATE, handlerName, "Ljava/lang/reflect/InvocationHandler;", null, null);
        handlerField.visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodName, String methodDesc, String signature, String[] exceptions) {
        if (filterMethod(access, methodName, methodDesc)) {
            return null;
        }
        if (AsmUtil.isInitMethod(methodName)) {
            return creatInitMethod(access, methodName, methodDesc, signature, exceptions);
        }
        return creatProxyMethod(access, methodName, methodDesc, signature, exceptions);
    }

    private boolean filterMethod(int access, String methodName, String methodDesc) {
        return AsmUtil.isStaticBlock(methodName) ||
                (access & ACC_STATIC) != 0
                ;
    }

    private MethodVisitor creatProxyMethod(int access, String methodName, String methodDesc, String signature, String[] exceptions) {

        MethodVisitor proxyVisitor = super.visitMethod(access, methodName, methodDesc, signature, exceptions);

        doCreatProxyMethod(proxyVisitor, methodName, methodDesc);

        MethodVisitor superVisitor = super.visitMethod(access, methodName + "$Proxy", methodDesc, signature, exceptions);
        doCreatSuperMethod(superVisitor, methodName, methodDesc);

        return null;
    }

    private void doCreatSuperMethod(MethodVisitor superVisitor, String methodName, String methodDesc) {
        Type returnType = Type.getMethodType(methodDesc).getReturnType();
        superVisitor.visitCode();
        AsmUtil.loadInsn(superVisitor, methodDesc, true);
        superVisitor.visitMethodInsn(INVOKESPECIAL, superName, methodName, methodDesc, false);
        if (returnType.equals(Type.VOID_TYPE)) {
            superVisitor.visitInsn(RETURN);
        }else {
            superVisitor.visitInsn(ARETURN);
        }

        superVisitor.visitMaxs(4, 4);
        superVisitor.visitEnd();
    }
    private void doCreatProxyMethod(MethodVisitor proxyVisitor, String methodName, String methodDesc) {
        Type methodType = Type.getMethodType(methodDesc);
        Type[] argumentTypes = methodType.getArgumentTypes();
        int parameterCount = argumentTypes.length;
        String methodFieldName = methodName+"$Proxy$"+Math.abs(methodDesc.hashCode());
        Type returnType = methodType.getReturnType();

        this.methodFieldMapping.put(methodFieldName,methodType);

        Label tryLabel = new Label();
        Label tryEndLabel = new Label();
        Label catchLabel = new Label();



        proxyVisitor.visitCode();

        proxyVisitor.visitTryCatchBlock(tryLabel, tryEndLabel, catchLabel, "java/lang/NoSuchMethodException");
        proxyVisitor.visitLabel(tryLabel);

        proxyVisitor.visitVarInsn(ALOAD, 0);
        proxyVisitor.visitFieldInsn(GETFIELD, proxyClassName, handlerName, "Ljava/lang/reflect/InvocationHandler;");

        proxyVisitor.visitVarInsn(ALOAD, 0);
        proxyVisitor.visitVarInsn(ALOAD, 0);
        proxyVisitor.visitFieldInsn(GETFIELD, proxyClassName, methodFieldName, "Ljava/lang/reflect/Method;");

        proxyVisitor.visitIntInsn(BIPUSH, parameterCount);
        proxyVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");

        int loadInsn = 1;
        for (int ii = 0; ii < argumentTypes.length; ii++, loadInsn++) {
            proxyVisitor.visitInsn(DUP);
            proxyVisitor.visitIntInsn(BIPUSH, ii);
            if (argumentTypes[ii].equals(Long.TYPE)) {
                proxyVisitor.visitVarInsn(LLOAD, loadInsn);
                proxyVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
                loadInsn++;
            } else if (argumentTypes[ii].equals(Double.TYPE)) {
                proxyVisitor.visitVarInsn(DLOAD, loadInsn);
                proxyVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
                loadInsn++;
            } else if (AsmUtil.isPrimitive(argumentTypes[ii])) {
                AsmUtil.primitive2Wrapper(proxyVisitor, argumentTypes[ii]);
            } else {
                proxyVisitor.visitVarInsn(ALOAD, loadInsn);
            }
            proxyVisitor.visitInsn(AASTORE);
        }
        proxyVisitor.visitMethodInsn(INVOKEINTERFACE, "java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);

        if (returnType.equals(Type.VOID_TYPE)) {
            proxyVisitor.visitInsn(POP);
            proxyVisitor.visitLabel(tryEndLabel);
            proxyVisitor.visitInsn(RETURN);
        } else if (AsmUtil.isPrimitive(returnType)) {
            //把Object转换为原始类型
            AsmUtil.wrapper2Primitive(proxyVisitor, returnType);
            proxyVisitor.visitLabel(tryEndLabel);
            proxyVisitor.visitInsn(IRETURN);
        } else {
            proxyVisitor.visitTypeInsn(CHECKCAST, returnType.getInternalName());
            proxyVisitor.visitLabel(tryEndLabel);
            proxyVisitor.visitInsn(ARETURN);
        }
        proxyVisitor.visitLabel(catchLabel);
        proxyVisitor.visitVarInsn(ASTORE, loadInsn);
        proxyVisitor.visitTypeInsn(NEW, "java/lang/RuntimeException");
        proxyVisitor.visitInsn(DUP);
        proxyVisitor.visitVarInsn(ALOAD, loadInsn);
        proxyVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
        proxyVisitor.visitInsn(ATHROW);


        proxyVisitor.visitMaxs(8, 5);
        proxyVisitor.visitEnd();

    }

    private MethodVisitor creatInitMethod(int access, String methodName, String methodDesc, String signature, String[] exceptions) {
        StringBuffer descBuffer = new StringBuffer(methodDesc);
        descBuffer.insert(descBuffer.indexOf(")"), "Ljava/lang/reflect/InvocationHandler;");
        String proxyIntDesc = descBuffer.toString();
        MethodVisitor initVisitor = super.visitMethod(access, methodName, proxyIntDesc, signature, exceptions);
        int size = AsmUtil.getMethodArgSize(proxyIntDesc);
        initVisitor.visitCode();
        AsmUtil.loadInsn(initVisitor, methodDesc, true);
        initVisitor.visitMethodInsn(INVOKESPECIAL, superName, methodName, methodDesc, false);

        initVisitor.visitVarInsn(ALOAD, 0);
        initVisitor.visitVarInsn(ALOAD, size);
        initVisitor.visitFieldInsn(PUTFIELD, proxyClassName, handlerName, "Ljava/lang/reflect/InvocationHandler;");

        initVisitor.visitVarInsn(ALOAD, 0);
        initVisitor.visitMethodInsn(INVOKESPECIAL, proxyClassName, "initProxyMethod", "()V", false);

        initVisitor.visitInsn(RETURN);
        initVisitor.visitMaxs(2, size + 1);
        initVisitor.visitEnd();

        return null;
    }

    @Override
    public void visitEnd() {
        creatInitProxyMethod();
        super.visitEnd();
    }

    private void creatInitProxyMethod() {
        MethodVisitor initProxyMethod = super.visitMethod(ACC_PRIVATE, "initProxyMethod", "()V", null, null);
        if (methodFieldMapping.isEmpty()) {
            initProxyMethod.visitCode();
            initProxyMethod.visitInsn(RETURN);
            initProxyMethod.visitMaxs(1, 1);
            initProxyMethod.visitEnd();
            return;
        }
        Label tryLabel = new Label();
        Label tryEndLabel = new Label();
        Label catchLabel = new Label();
        Label returnLabel = new Label();

        initProxyMethod.visitCode();
        initProxyMethod.visitTryCatchBlock(tryLabel, tryEndLabel, catchLabel, "java/lang/NoSuchMethodException");

        initProxyMethod.visitLabel(tryLabel);

        for (String methodFieldName : this.methodFieldMapping.keySet()) {
            super.visitField(ACC_PRIVATE, methodFieldName, "Ljava/lang/reflect/Method;", null, null).visitEnd();
            String methodName = methodFieldName.substring(0,methodFieldName.indexOf("$Proxy"));
            doCreatInitProxyMethod(initProxyMethod, this.methodFieldMapping.get(methodFieldName), methodName, methodFieldName);
        }
        initProxyMethod.visitLabel(tryEndLabel);

        initProxyMethod.visitJumpInsn(GOTO, returnLabel);
        initProxyMethod.visitLabel(catchLabel);
        initProxyMethod.visitVarInsn(ASTORE, 1);
        initProxyMethod.visitTypeInsn(NEW, "java/lang/RuntimeException");
        initProxyMethod.visitInsn(DUP);
        initProxyMethod.visitVarInsn(ALOAD, 1);
        initProxyMethod.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/Throwable;)V", false);
        initProxyMethod.visitInsn(ATHROW);


        initProxyMethod.visitLabel(returnLabel);
        initProxyMethod.visitInsn(RETURN);
        initProxyMethod.visitMaxs(7, 2);
        initProxyMethod.visitEnd();
    }
    private void doCreatInitProxyMethod(MethodVisitor initProxyMethod, Type methodType, String methodName, String methodFieldName) {


        Type[] argumentTypes = methodType.getArgumentTypes();

        initProxyMethod.visitVarInsn(ALOAD, 0);
        initProxyMethod.visitVarInsn(ALOAD, 0);
        initProxyMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
        initProxyMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getSuperclass", "()Ljava/lang/Class;", false);
        initProxyMethod.visitLdcInsn(methodName);


        initProxyMethod.visitIntInsn(BIPUSH, argumentTypes.length);
        initProxyMethod.visitTypeInsn(ANEWARRAY, "java/lang/Class");

        for (int ii = 0; ii < argumentTypes.length; ii++) {
            initProxyMethod.visitInsn(DUP);
            initProxyMethod.visitIntInsn(BIPUSH, ii);

            if (AsmUtil.isPrimitive(argumentTypes[ii])) {
                Type wrapperType = AsmUtil.getWrapperType(argumentTypes[ii]);
                initProxyMethod.visitFieldInsn(GETSTATIC, wrapperType.getInternalName(), "TYPE", "Ljava/lang/Class;");
            } else {
                initProxyMethod.visitLdcInsn(argumentTypes[ii]);
            }
            initProxyMethod.visitInsn(AASTORE);
        }
        initProxyMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
        initProxyMethod.visitFieldInsn(PUTFIELD, proxyClassName, methodFieldName, "Ljava/lang/reflect/Method;");

    }
}
