# Zhaoyssdb-orm

一个轻量级的 ORM 框架，底层基于 JdbcTemplate 进行了封装。

- 无需 xml 配置
- 简化单表操作
- 灵活构建查询语法
- 支持h2,hsqldb,mysql,sqlite等多种数据源切换

# 使用说明
需要在项目的 resources 文件下创建一个 jdbc.properties 配置文件。
```properties
# zhaoyssdb配置
# 数据源类型：sqlite,h2,hsqldb,mysql
zhaoyssdb.datasource=h2
# 标有 @Entity 注解的实体包路径
zhaoyssdb.entityPackagePath=com.zhaoyss.entity

# hsqldb 数据源配置
#zhaoyssdb.driverClassName:org.hsqldb.jdbcDriver
#zhaoyssdb.url=jdbc:hsqldb:file:testdb
#zhaoyssdb.username=sa
#zhaoyssdb.password=

# h2 数据源配置
zhaoyssdb.url=jdbc:h2:mem:testdb
zhaoyssdb.driverClassName=org.h2.Driver
zhaoyssdb.username=sa
zhaoyssdb.password=

# Mysql 数据源配置
#zhaoyssdb.url= jdbc:mysql://192.168.10.91:3306/zhaoyssdb?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
#zhaoyssdb.driverClassName=com.mysql.cj.jdbc.Driver
#zhaoyssdb.username=zhaoyssdb
#zhaoyssdb.password=jELiaMFCpBB5PnNa

## SQLite 数据源配置
#zhaoyssdb.url= jdbc:sqlite:D:/workspaces/springboot/zhaoyss-orm/sqlitedb_user.db
#zhaoyssdb.driverClassName=org.sqlite.JDBC

```
## 快速开始
可以参考测试用例 [`src/test/java/com/zhaoyss/ZhaoyssdbTemplateUserTest.java`](src/test/java/com/zhaoyss/ZhaoyssdbTemplateUserTest.java)

## 操作指引
```java
    // save
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);
    user.setCreatedAt(System.currentTimeMillis());
    zhaoyssdbTemplate.insert(user);

    // 根据id查询
    User getUserById(long id) {
    return zhaoyssdbTemplate.get(User.class, id);

    // 根据条件查询所有字段
    // select * from users where email = "zhaoyss@qq.com" 
    zhaoyssdbTemplate.from(User.class).where("email = ?", email).one();

    
    // 根据条件查询指定的字段
    // select name from users where emial = "zhaoyss@qq.com" 
    zhaoyssdbTemplate.select("name").from(User.class).where("email = ?", email).unique();

    // 复杂查询
    // select * form usres  order by id desc limit 0,5
    int pageSize = 5;
    zhaoyssdbTemplate.from(User.class).orderBy("id desc").limit((pageIndex - 1) * pageSize, pageSize).list();
```
## 参考
- [设计ORM](https://www.liaoxuefeng.com/wiki/1252599548343744/1282383340896289)
- [MyBatis](https://mybatis.net.cn/)
- [JdbcTempalte](https://spring.io/projects/spring-data-jdbc)
- [JPA](https://springdoc.cn/spring-data-jpa/)

