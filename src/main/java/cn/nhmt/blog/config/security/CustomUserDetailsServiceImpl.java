package cn.nhmt.blog.config.security;

import cn.nhmt.blog.ao.UserAo;
import cn.nhmt.blog.dao.RoleDao;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

/**
 * @Description: load user
 * @Date: 2020-07-05 13:33
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.retrieveByName(username, null);
        if (Objects.isNull(user)) {
            UsernameNotFoundException unfe = new UsernameNotFoundException("username:" + username + " not found.");
            AuthenticationExceptionHolder.set(unfe);
            throw unfe;
        }
        cn.nhmt.blog.po.Role role = roleDao.retrieveByUserId(user.getId());
        return new UserAo(user, role.getGrantCollection());
    }

    public static abstract class AuthenticationExceptionHolder {

        private static final ThreadLocal<AuthenticationException> threadLocal = new ThreadLocal<>();

        public static AuthenticationException get() {
            return threadLocal.get();
        }

        public static void set(AuthenticationException ex) {
            threadLocal.set(ex);
        }

        public static void clear() {
            threadLocal.remove();
        }
    }

}
