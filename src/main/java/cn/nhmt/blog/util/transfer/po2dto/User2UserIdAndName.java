package cn.nhmt.blog.dto.user.util;

import cn.nhmt.blog.dto.Po2Dto;
import cn.nhmt.blog.dto.user.UserIdAndName;
import cn.nhmt.blog.po.User;

/**
 * @Description: TODO
 * @Date: 2020-04-22 15:11
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class User2UserIdAndName implements Po2Dto<User, UserIdAndName> {
    @Override
    public UserIdAndName transfer(User po) {
        return null;
    }
}
