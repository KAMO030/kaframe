package com.kamo.cglib.asm;

import com.kamo.cglib.ProxyClassWriter;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

import java.io.IOException;

public class AsmProxyClassWriter extends ProxyClassWriter implements Opcodes {
    public AsmProxyClassWriter(Class superClass, String proxyClassName) {
        super(superClass);
        this.superName = Type.getInternalName(superClass);
        this.proxyClassName = proxyClassName.replace('.', '/');
    }

    @Override
    public byte[] getProxyClassByteArray()  {
        ClassReader cr = null;
        try {
            cr = new ClassReader(superclass.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ProxyClassVisitor proxyClassVisitor = new ProxyClassVisitor(ASM5,cw,proxyClassName);

        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;

        cr.accept(proxyClassVisitor, parsingOptions);
        return cw.toByteArray();
    }

}
