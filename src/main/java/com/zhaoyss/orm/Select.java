package com.zhaoyss.orm;

import java.util.Arrays;

/**
 * SELECT ... from ...
 */
public class Select extends CriteriaQuery {
    @SuppressWarnings("unchecked")
    Select(Criteria criteria,String... selectFields) {
        super(criteria);
        if (selectFields.length > 0){
            this.criteria.select = Arrays.asList(selectFields);
        }
    }

    /**
     * 添加 FROM 子句
     *
     * @param entityClass
     * @return
     * @param <T>
     */

    public <T> From<T> from(Class<T> entityClass){
        return new From<T>(this.criteria,this.criteria.zhaoyssdbTemplate.getMapper(entityClass));
    }

}
