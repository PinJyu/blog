package cn.nhmt.blog.bo;

import cn.nhmt.blog.po.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @Description: 重编辑文章时，验证文章是否属于当前用户;
 * @Date: 2020-07-07 15:40
 * @Author: PinJyu
 * @Version: 1.0
 **/
public interface ReEditValid {

    boolean isbelongUser(int articleId, User user);

    default boolean isbelongUser(int articleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                return isbelongUser(articleId, user);
            }
        }
        return false;
    }

}
