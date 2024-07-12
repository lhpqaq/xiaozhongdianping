package com.xzdp.mapper;

import com.xzdp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * BaseMapper 提供了一系列的通用 CRUD 方法，例如 insert、delete、update、selectById 等。
	•	MyBatis-Plus 会根据 User 实体类和数据库表的映射关系，将这些通用方法映射到具体的 SQL 语句。
	•	例如，调用 UserMapper.insert(User user) 方法时，MyBatis-Plus 会生成并执行一条 INSERT INTO SQL 语句，将 user 对象的数据插入到数据库中的 User 表。
 * MyBatis 会为 UserMapper 接口生成一个代理对象，这个代理对象实现了接口中的所有方法，并将这些方法映射到相应的 SQL 语句。
 */
public interface UserMapper extends BaseMapper<User> {
    // 这里可以实现一些自定义的映射，需要修改xml文件和注解
}
