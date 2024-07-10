package com.zhaoyss.orm;

import java.util.List;

/**
 * select ... FROM ...
 *
 * @param <T>
 */
public class From<T> extends CriteriaQuery<T>{
    From(Criteria<T> criteria,Mapper<T> mapper) {
        super(criteria);
        this.criteria.mapper = mapper;
        this.criteria.clazz = mapper.entityClass;
        this.criteria.table = mapper.tableName;
    }

    /**
     * 添加 where 子句
     *
     * @param clause: like "name = ? and age = ?"
     * @param args: ? 相匹配的参数
     * @return
     */
    public Where<T> where(String clause,Object... args){
        return new Where<>(this.criteria,clause,args);
    }

    /**
     * 添加 orderBy 子句
     *
     * @param orderBy like "name desc"
     * @return
     */
    public OrderBy<T> orderBy(String orderBy){
        return new OrderBy<>(this.criteria,orderBy);
    }

    /**
     * 添加 limit 子句
     *
     * @param maxResults limit 最大的结果
     * @return
     */
    public Limit<T> limit(int maxResults){
        return limit(0,maxResults);
    }

    public Limit<T> limit(int offset, int maxResults) {
        return new Limit<>(this.criteria, offset, maxResults);
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
     * @return T
     * @return jakarta.persistence.NoResultException 如果result为空
     * @return jakarta.persistence.NonUniqueResultException 如果result有多个值
     */
    public T unique() {
        return this.criteria.unique();
    }

}
