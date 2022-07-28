package com.kamo.transaction;


import java.sql.SQLException;

/**
 * 事务管理接口
 *
 */
public interface TransactionManager {
	/**
	 * 开启事务
	 */
	void begin(TransactionDefinition definition) throws SQLException;



	void cleanupAfterCompletion();
	default void resume() throws SQLException {
	}
}
