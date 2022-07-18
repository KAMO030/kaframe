package com.kamo.transaction.impl;

import com.kamo.transaction.TransManager;
import com.kamo.idal.SqlSession;

public class SqlSessionTrans implements TransManager {
    private SqlSession sqlSession;

    public SqlSessionTrans(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public void beginTrans() {
        sqlSession.setAutoCommit(false);

    }

    @Override
    public void commitTrans() {
        sqlSession.commit();
        sqlSession.setAutoCommit(true);
        sqlSession.close();
    }

    @Override
    public void rollbackTrans() {
        sqlSession.rollBack();
        sqlSession.setAutoCommit(true);
        sqlSession.close();
    }
}
