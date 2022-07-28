package com.kamo.proxy;


import com.kamo.transaction.TransactionManager;




public class TransactionProxy {
	/**
	 * 获得该类的代理类对象
	 * @param target 需要被代理的类
	 * @param transactionManager 代理类的增强类
	 * @return 源对象方法执行后的返回值
	 */
	// 被代理类
	public static Object createProxy(Object target, TransactionManager transactionManager) {
		// 通过JDK创建的动态代理
//		Object proxyObject = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
//				new InvocationHandler() {
//					@Override
//					public Object invoke(Object proxy, Method method,Object[] args) throws Throwable {
//						Object res = null;
//						try {
//							transactionManager.begin();
//							res = method.invoke(target, args);
//							transactionManager.commit();
//						} catch (InvocationTargetException e) {
//							transactionManager.rollback();
//							throw e.getTargetException() ;
//						}
//						return res;
//					}
//				});
		return null;
	}
}
