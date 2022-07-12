package kamo.transaction.impl;

import java.sql.Connection;
import java.sql.SQLException;


import kamo.transaction.TransManager;

import javax.sql.DataSource;


/**
 * 事务管理的实现类
 *
 */
public class TransManagerImpl implements TransManager {
	private DataSource dataSource;

	public TransManagerImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void beginTrans(){
		try {
			Connection con = dataSource.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void commitTrans(){
		Connection con;
		try {
			con = dataSource.getConnection();
			con.commit();
			con.setAutoCommit(true);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rollbackTrans() {
		Connection con;
		try {
			con = dataSource.getConnection();
			con.rollback();
			con.setAutoCommit(true);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
