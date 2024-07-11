package com.zhaoyss.orm;

import com.zhaoyss.util.LoggerUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class ZhaoyssdbTemplate {

    final JdbcTemplate jdbcTemplate;

    final Map<Class<?>, Mapper<?>> classMapping;


    public ZhaoyssdbTemplate(JdbcTemplate jdbcTemplate, String basePackage) {
        this.jdbcTemplate = jdbcTemplate;
        List<Class<?>> classes = scanEntities(basePackage);
        Map<Class<?>, Mapper<?>> classMapping = new HashMap<>();
        try {
            for (Class<?> clazz : classes) {
                LoggerUtil.logInfo("Found class: "+ clazz.getName());
                Mapper<?> mapper = new Mapper<>(clazz);
                classMapping.put(clazz, mapper);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.classMapping = classMapping;
    }

    /**
     * 根据包扫描 Entity.class 的实例
     *
     * @param basePackage
     * @return
     */
    private List<Class<?>> scanEntities(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        List<Class<?>> classes = new ArrayList<>();
        Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
        for (BeanDefinition bean : beans) {
            try {
                classes.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }



    /**
     * 映射
     * 通过class获取Mapper
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Mapper<T> getMapper(Class<T> clazz) {
        Mapper<T> mapper = (Mapper<T>) this.classMapping.get(clazz);
        if (mapper == null) {
            throw new RuntimeException("Target class is not a registered entity: " + clazz.getName());
        }
        return mapper;
    }

    /**
     * 增
     *
     * @param bean
     * @param <T>
     */
    public <T> void insert(T bean) {
        try {
            int rows;
            final Mapper<?> mapper = getMapper(bean.getClass());
            Object[] args = new Object[mapper.insertableProperties.size()];
            int n = 0;
            for (BeanProperty prop : mapper.insertableProperties) {
                args[n] = prop.getter.invoke(bean);
                n++;
            }
            LoggerUtil.logInfo("SQL: " + mapper.insertSQL);
            // 使用 identityId
            if (mapper.id.isIdentityId()) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                rows = jdbcTemplate.update(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(mapper.insertSQL, Statement.RETURN_GENERATED_KEYS);
                        for (int i = 0; i < args.length; i++) {
                            ps.setObject(i + 1,args[i]);
                        }
                        return ps;
                    }
                },keyHolder);
                if (rows == 1){
                    mapper.id.setter.invoke(bean,keyHolder.getKey().longValue());
                }
            }else {
                // id是指定的
                rows = jdbcTemplate.update(mapper.insertSQL,args);
            }

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删
     *
     * @param bean
     * @param <T>
     */
    public <T> void delete(T bean) {
        try {
            Mapper<?> mapper = getMapper(bean.getClass());
            delete(bean.getClass(), mapper.getIdValue(bean));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void delete(Class<T> clazz, Object id) {
        Mapper<?> mapper = getMapper(clazz);
        LoggerUtil.logInfo("SQL: " + mapper.deleteSQL);
        int update = jdbcTemplate.update(mapper.deleteSQL, id);
        System.out.println(update);
    }

    /**
     * 改
     *
     * @param bean
     * @param <T>
     */
    public <T> void update(T bean) {
        try {
            Mapper<?> mapper = getMapper(bean.getClass());
            Object[] args = new Object[mapper.updatableProperties.size() + 1];
            int n = 0;
            for (BeanProperty prop : mapper.updatableProperties) {
                args[n] = prop.getter.invoke(bean);
                n++;
            }
            args[n] = mapper.id.getter.invoke(bean);
            LoggerUtil.logInfo("SQL: " + mapper.updateSQL);
            jdbcTemplate.update(mapper.updateSQL, args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 查 - 通过 id 查询
     *
     * @param clazz
     * @param id
     * @return
     * @param <T>
     */
    public <T> T get(Class<T> clazz, Object id) {
        T t = fetch(clazz, id);
        if (t == null) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }
        return t;
    }

    private <T> T fetch(Class<T> clazz, Object id) {
        Mapper<T> mapper = getMapper(clazz);
        LoggerUtil.logInfo("SQL: " + mapper.selectSQL);
        List<T> list = (List<T>) jdbcTemplate.query(mapper.selectSQL, mapper.rowMapper, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);

    }


    /**
     * 链式调用
     * Select ... from ...
     *
     * @param selectFields
     * @return
     */
    public Select select(String... selectFields){
        return new Select(new Criteria(this),selectFields);
    }

    /**
     * 链式调用
     * select ... FROM ...
     *
     * @param entityClass
     * @return
     * @param <T>
     */
    public <T> From<T> from(Class<T> entityClass){
        Mapper<T> mapper = getMapper(entityClass);
        return new From<>(new Criteria<>(this),mapper);
    }

}
