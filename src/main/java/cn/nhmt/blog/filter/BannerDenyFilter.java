package cn.nhmt.blog.filter;

import cn.nhmt.blog.contants.Role;
import cn.nhmt.blog.dao.BannerDao;
import cn.nhmt.blog.po.Banner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @Description: Deny request by address. No use
 * @Date: 2020-07-01 12:54
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class BannerDenyFilter extends OncePerRequestFilter {

    private BannerDao bannerDao;

    public BannerDenyFilter(BannerDao bannerDao) {
        this.bannerDao = bannerDao;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean deny = false;
        if (Objects.nonNull(authentication)) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (Objects.nonNull(authority.getAuthority())
                        && authority.getAuthority().equals(Role.BANNER.getLiteRole())) {
                    deny = true;
                }
            }
        }

        if (!deny) {
            Banner banner = bannerDao.retrieveByAddress(request.getRemoteAddr());
            if (Objects.nonNull(banner)) {
                long createTime = banner.getCreateGmt().getTime();
                long millis = banner.getMinutes() * 60000;
                if (createTime + millis < 0) {
                    millis = Long.MAX_VALUE - createTime;
                    banner.setMinutes((int) (millis / 60000));
                    banner.setLastModifyGmt(new Date());
                    bannerDao.update(banner);
                }
                if (createTime + millis > System.currentTimeMillis()) {
                    deny = true;
                } else {
                    bannerDao.deleteById(banner.getId());
                }
            }
        }

        if (deny) {
            throw new AccessDeniedException("Deny by grant or address");
        }

        filterChain.doFilter(request, response);
    }

}
