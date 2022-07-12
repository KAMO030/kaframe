package kamo.proxy;


import kamo.transaction.TransManager;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;




public class TransactionProxy {
	/**
	 * 获得该类的代理类对象
	 * @param target 需要被代理的类
	 * @param transManager 代理类的增强类
	 * @return 源对象方法执行后的返回值
	 */
	// 被代理类
	public static Object createProxy(Object target,TransManager transManager) {
		// 通过JDK创建的动态代理
		Object proxyObject = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method,Object[] args) throws Throwable {
						Object res = null;
						try {
							transManager.beginTrans();
							res = method.invoke(target, args);
							transManager.commitTrans();
						} catch (InvocationTargetException e) {
							transManager.rollbackTrans();
							throw e.getTargetException() ;
						}
						return res;
					}
				});
		return proxyObject;
	}
}
