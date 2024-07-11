package com.zhaoyss;

import com.zhaoyss.entity.User;
import com.zhaoyss.orm.ZhaoyssdbTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DbConfig.class)
public class ZhaoyssdbTemplateUserTest {

    @Autowired
    ZhaoyssdbTemplate zhaoyssdbTemplate;

    @Test
    public void testRegister() {
        User register = register("zhaoyss123@gmial", "password", "zhaoyss");
        System.out.println("register() ok: " + register.getId());
    }

    /**
     * 增
     */
    User register(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        zhaoyssdbTemplate.insert(user);
        return user;
    }

    /**
     * 删
     */
    void deleteUser(Long id) {
        zhaoyssdbTemplate.delete(User.class, id);
    }

    /**
     * 改
     */
    void updateUser(Long id, String name) {
        User user = getUserById(id);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        zhaoyssdbTemplate.update(user);
    }

    /**
     * 条件查询
     */
    User getUserById(long id) {
        return zhaoyssdbTemplate.get(User.class, id);
    }

    /**
     * 条件查询
     */
    User selectUserByEmail(String email) {
        return zhaoyssdbTemplate.from(User.class).where("email = ?", email).one();
    }

    /**
     * 条件查询
     */
    User selectUserUnique(String email) {
        return zhaoyssdbTemplate.from(User.class).where("email = ?", email).unique();
    }

    /**
     * 条件查询
     */
    String getNameByEmail(String email) {
        User user = zhaoyssdbTemplate.select("name").from(User.class).where("email = ?", email).unique();
        return user.getName();
    }

    /**
     * 复杂查询
     */
    List<User> getUsers(int pageIndex) {
        int pageSize = 5;
        return zhaoyssdbTemplate.from(User.class).orderBy("id desc").limit((pageIndex - 1) * pageSize, pageSize).list();
    }

}
