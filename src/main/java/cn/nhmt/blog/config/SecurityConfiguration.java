package cn.nhmt.blog.config;

import cn.nhmt.blog.ao.UserAo;
import cn.nhmt.blog.config.security.*;
import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.contants.Role;
import cn.nhmt.blog.dao.RoleDao;
import cn.nhmt.blog.dao.UserDao;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.po.User;
import cn.nhmt.blog.util.WebUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;

/**
 * @Description: spring security config
 * @Date: 2020-06-21 14:51
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, proxyTargetClass = true, order = 1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired(required = false)
    private GrantedAuthorityDefaults grantedAuthorityDefaults;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .anonymous().authorities(Role.VISITOR.getFullRole())
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login/process")
                .permitAll().failureHandler(new CustomAuthenticationFailureHandlerImpl())
                .successHandler(new CustomSavedRequestAwareAuthenticationSuccessHandlerImpl(http))
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
                .and()
                .sessionManagement().maximumSessions(1).expiredUrl("/login").and()
                .sessionAuthenticationErrorUrl("/login").invalidSessionUrl("/login")
                .sessionFixation().changeSessionId().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandlerImpl("/error"))
                .and()
                .rememberMe().alwaysRemember(true).tokenValiditySeconds(60 * 60 * 48)
                .userDetailsService(userDetailsService()).tokenRepository(persistentTokenRepository())
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .servletApi().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/images/**", "/files/**", "/editormd/**");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsServiceImpl(userDao, roleDao);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new CustomJdbcTokenRepositoryImpl(dataSource);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl rhi = new RoleHierarchyImpl();
        if (Objects.nonNull(grantedAuthorityDefaults)) {
            Role.setPrefix(grantedAuthorityDefaults.getRolePrefix());
        }
        String gt = " > ";
        rhi.setHierarchy(
                Role.ADMIN.getFullRole() + gt
                        + Role.AUTHOR.getFullRole() + gt
                        + Role.VISITOR.getFullRole() + gt
                        + Role.BANNER.getFullRole()
        );
        return rhi;
    }

}
