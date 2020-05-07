package com.bo.mapper;

import com.bo.pojo.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MiaoshaUserMapper {

    List<MiaoshaUser> queryUserList();

    MiaoshaUser queryUserById(int id);

    int addUser(MiaoshaUser user);

    MiaoshaUser queryUserByEmail(String email);
}
