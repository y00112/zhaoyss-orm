package com.zhaoyss.orm;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @param <T>
 */
public class Criteria<T> {
    ZhaoyssdbTemplate zhaoyssdbTemplate;
    Mapper<T> mapper;
    Class<T> clazz;
    List<String> select = null;
    boolean distinct = false;
    String table = null;
    String where = null;
    List<Object> whereParams = null;
    List<String> orderBy = null;
    int offset = 0;
    int maxResults = 0;

    Criteria(ZhaoyssdbTemplate zhaoyssdbTemplate) {
        this.zhaoyssdbTemplate = zhaoyssdbTemplate;
    }

    String sql() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("SELECT ");
        sb.append(select == null ? "*" : String.join(", ", select));
        sb.append(" FROM ").append(mapper.tableName);
        if (where != null) {
            sb.append(" WHERE ").append(String.join(", ", where));
        }
        if (orderBy != null) {
            sb.append(" ORDER BY ").append(String.join(", ", orderBy));
        }
        if (offset > 0 && maxResults > 0) {
            sb.append(" LIMIT ? , ?");
        }
        String sql = sb.toString();
        return sql;
    }

    Object[] params() {
        List<Object> params = new ArrayList<>();
        if (where != null) {
            for (Object obj : whereParams) {
                if (obj == null) {
                    params.add(null);
                } else {
                    params.add(obj);
                }
            }
        }
        if (offset > 0 && maxResults > 0) {
            params.add(offset);
            params.add(maxResults);
        }
        return params.toArray();
    }

    List<T> list() {
        String selectSql = sql();
        Object[] selectParams = params();
        return zhaoyssdbTemplate.jdbcTemplate.query(selectSql, mapper.rowMapper, selectParams);
    }

    T one() {
        this.offset = 0;
        this.maxResults = 1;
        String selectSql = sql();
        Object[] selectParams = params();
        List<T> list = zhaoyssdbTemplate.jdbcTemplate.query(selectSql, mapper.rowMapper, selectParams);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    T unique(){
        this.offset = 0;
        this.maxResults = 2;
        String selectSql = sql();
        Object[] selectParams = params();
        List<T> list = zhaoyssdbTemplate.jdbcTemplate.query(selectSql, mapper.rowMapper, selectParams);
        if (list.isEmpty()) {
            throw new NoResultException("Expected unique row but nothing found.");
        }
        if (list.size() > 1) {
            throw new NonUniqueResultException("Expected unique row but more than 1 rows found.");
        }
        return list.get(0);
    }

}
