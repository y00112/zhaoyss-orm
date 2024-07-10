package com.zhaoyss.orm;

import java.util.ArrayList;
import java.util.List;

/**
 * select ... from ... WHERE ...
 *
 * @param <T>
 */
public class Where<T> extends CriteriaQuery<T> {
    Where(Criteria<T> criteria, String clause, Object... params) {
        super(criteria);
        this.criteria.where = clause;
        this.criteria.whereParams = new ArrayList<>();
        for (Object param : params) {
            this.criteria.whereParams.add(param);
        }
    }

    /**
     * 添加 limit 子句
     *
     * @param maxResults
     * @return
     */
    public Limit<T> limit(int maxResults) {
        return limit(0, maxResults);
    }

    public Limit<T> limit(int offset, int maxResults) {
        return new Limit<>(this.criteria, offset, maxResults);
    }

    /**
     * 添加 orderBy 子句
     *
     * @param orderBy
     * @return
     */
    public OrderBy<T> orderBy(String orderBy) {
        return new OrderBy<>(this.criteria, orderBy);
    }

    /**
     * 查询列表
     *
     * @return
     */
    public List<T> list() {
        return this.criteria.list();
    }

    /**
     * 查询一个
     *
     * @return
     */
    public T one() {
        return this.criteria.one();
    }

    /**
     * 获取唯一查询结果，如果没有找到或者有多个值，则引发异常
     *
     * @return T demoInstance
     * @return jakarta.persistence.NoResultException 如果result为空
     * @return jakarta.persistence.NonUniqueResultException 如果result有多个值
     */
    public T unique() {
        return this.criteria.unique();
    }

}
