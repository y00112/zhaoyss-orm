package com.zhaoyss.orm;

import java.util.ArrayList;
import java.util.List;

/**
 * select ... from ... ORDER BY ...
 */
public class OrderBy<T> extends CriteriaQuery<T> {

    OrderBy(Criteria<T> criteria, String orderBy) {
        super(criteria);
        orderBy(orderBy);
    }

    /**
     * 按照字段名称排序
     *
     * @param orderBy like "name desc"
     * @return
     */
    private OrderBy<T> orderBy(String orderBy) {
        if (criteria.orderBy == null){
            criteria.orderBy = new ArrayList<>();
        }
        orderBy = checkProperty(orderBy);
        criteria.orderBy.add(orderBy);
        return this;
    }

    /**
     * 检查 orderBy 是属性
     *
     * @param orderBy
     * @return
     */
    private String checkProperty(String orderBy) {
        String prop = null;
        String upper = orderBy.toUpperCase();
        if (upper.endsWith(" DESC")){
            prop = orderBy.substring(0,orderBy.length() - 5).trim();
            return propertyToField(prop) + " DESC";
        }
        if (upper.endsWith(" ASC")){
            prop = orderBy.substring(0, orderBy.length() - 4).trim();
            return propertyToField(prop) + " ASC";
        }else {
            prop = orderBy.trim();
            return propertyToField(prop);
        }
    }

    private String propertyToField(String prop) {
        BeanProperty bp = this.criteria.mapper.allPropertiesMap.get(prop.toUpperCase());
        if (bp == null){
            throw new IllegalArgumentException("Invalid property when use order by: " + prop);
        }
        return bp.columnName;
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
     * @return T demoInstance
     * @return jakarta.persistence.NoResultException 如果result为空
     * @return jakarta.persistence.NonUniqueResultException 如果result有多个值
     */
    public T unique() {
        return this.criteria.unique();
    }
}
