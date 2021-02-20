package cn.nhmt.blog.ao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

/**
 * @Description: custom user and userdetail compine
 * @Date: 2020-06-29 19:26
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Getter
@Setter
public class UserAo extends User {

    private int id;
    private String email;
    private Date createGmt;
    private Date lastModifyGmt;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private cn.nhmt.blog.po.User user;

    /**
     * From {@link cn.nhmt.blog.po.User} and {@link cn.nhmt.blog.po.Role} construct {@link User}
     * @param user custom user
     * @param authorities roles
     */
    public UserAo(cn.nhmt.blog.po.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getName(), user.getPassword(), authorities);
        this.id = user.getId();
        this.email = user.getEmail();
        this.createGmt = user.getCreateGmt();
        this.lastModifyGmt = user.getLastModifyGmt();
        this.user = user;
    }

    public cn.nhmt.blog.po.User toUser() {
        return user;
    }

}
