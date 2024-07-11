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

## Database Init
```java
@Component
public class DatabaseInitializer {
    @Autowired
    DataSource dataSource;

    /**
     * h2 hsqldb 初始化代码
     * @throws SQLException
     */
    @PostConstruct
    public void init() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }


    /**
     * mysql 初始化代码
     * @throws SQLException
     */
    @PostConstruct
    public void init() throws SQLException {
        try (var conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, " //
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }


    /**
     * SQLite
     *
     * @throws SQLException
     */
    @PostConstruct
    public void init() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // 使用 AUTOINCREMENT 关键字
                        + "email VARCHAR(100) NOT NULL, " //
                        + "password VARCHAR(100) NOT NULL, " //
                        + "name VARCHAR(100) NOT NULL, " //
                        + "createdAt BIGINT NOT NULL, " //
                        + "UNIQUE (email))");
            }
        }
    }
}
```

## 参考
- [设计ORM](https://www.liaoxuefeng.com/wiki/1252599548343744/1282383340896289)
- [MyBatis](https://mybatis.net.cn/)
- [JdbcTempalte](https://spring.io/projects/spring-data-jdbc)

