package com.zhaoyss.orm;

public class CriteriaQuery <T>{

    protected final Criteria<T> criteria;

    CriteriaQuery(Criteria<T> criteria){
        this.criteria = criteria;
    }

    String sql(){
        return criteria.sql();
    }
}
