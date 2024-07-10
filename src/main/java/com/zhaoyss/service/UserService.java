package com.zhaoyss.service;

import com.zhaoyss.entity.User;
import com.zhaoyss.orm.ZhaoyssdbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserService {

    @Autowired
    ZhaoyssdbTemplate zhaoyssdbTemplate;

    public User register(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        zhaoyssdbTemplate.insert(user);
        return user;
    }

    public void deleteUser(Long id){
        zhaoyssdbTemplate.delete(User.class,id);
    }

    public void updateUser(Long id,String name){
        User user = getUserById(id);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        zhaoyssdbTemplate.update(user);
    }

    public User getUserById(long id){
        return zhaoyssdbTemplate.get(User.class,id);
    }

    public User selectUserByEmail(String email){
        return zhaoyssdbTemplate.from(User.class).where("email = ?",email).one();
    }

    public User selectUserUnique(String email){
        return zhaoyssdbTemplate.from(User.class).where("email = ?",email).unique();
    }

    public String getNameByEmail(String email){
        User user = zhaoyssdbTemplate.select("name").from(User.class).where("email = ?", email).unique();
        return user.getName();
    }

    public List<User> getUsers(int pageIndex){
        int pageSize = 5;
        return zhaoyssdbTemplate.from(User.class).orderBy("id desc").limit((pageIndex -1 ) * pageSize,pageSize).list();
    }
}
