package com.zhaoyss.orm;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * bean 的属性
 */
public class BeanProperty {
    // Method
    final Method getter;
    final Method setter;

    // Java type
    final Class<?> propertyType;
    // Java bean property name
    final String propertyName;
    // Table column name
    final String columnName;

    boolean isId() {
        return this.getter.isAnnotationPresent(Id.class);
    }

    boolean isIdentityId() {
        if (!isId()) {
            return false;
        }
        GeneratedValue gv = this.getter.getAnnotation(GeneratedValue.class);
        if (gv == null) {
            return false;
        }
        GenerationType strategy = gv.strategy();
        return strategy == GenerationType.IDENTITY;
    }

    boolean isInsertable() {
        if (isIdentityId()) {
            return false;
        }
        Column col = this.getter.getAnnotation(Column.class);
        return col == null || col.updatable();
    }

    boolean isUpdatable(){
        if (isId()){
            return false;
        }
        Column col = this.getter.getAnnotation(Column.class);
        return col == null || col.updatable();
    }

    public BeanProperty(PropertyDescriptor pd) {
        this.getter = pd.getReadMethod();
        this.setter = pd.getWriteMethod();
        this.propertyType = pd.getReadMethod().getReturnType();
        this.propertyName = pd.getName();
        this.columnName = getColumnName(pd.getReadMethod(), propertyName);
    }

    private static String getColumnName(Method method, String defaultName) {
        Column col = method.getAnnotation(Column.class);
        if (col == null || col.name().isEmpty()) {
            return defaultName;
        }
        return col.name();
    }
}
