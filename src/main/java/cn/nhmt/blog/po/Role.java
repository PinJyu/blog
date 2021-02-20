package cn.nhmt.blog.po;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: role 表对象
 * @Date: 2020-06-29 14:36
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Data
@NoArgsConstructor
public class Role implements Serializable {

    private int userId;
    private String grant;
    private Date createGmt;
    private Date lastModifyGmt;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Collection<GrantedAuthority> grantCache;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String prefix;

    {
        prefix = cn.nhmt.blog.contants.Role.getPrefix();
    }

    public Collection<GrantedAuthority> getGrantCollection() {
        if (Objects.isNull(grantCache)) {
            if (!StringUtils.hasText(grant)) {
                grantCache = new HashSet<>();
            } else {
                String[] grants = this.grant.split("\\s+,\\s+");
                Set<GrantedAuthority> grantSet = new HashSet<>(grants.length);
                for (String grant : grants) {
                    if (StringUtils.hasText(grant)) {
                        if (!grant.startsWith(prefix)) {
                            grant = prefix + grant;
                        }
                        grantSet.add(new SimpleGrantedAuthority(grant));
                    }
                }
                grantCache = grantSet;
            }
        }

        return grantCache;
    }

    public boolean addGrants(String... roles) {
        return grantAddOrRemove(true, roles);
    }

    public boolean removeGrants(String... roles) {
        return grantAddOrRemove(false, roles);
    }

    private boolean grantAddOrRemove(boolean isAdd, String... roles) {
        if (Objects.isNull(grantCache)) {
            getGrantCollection();
        }

        boolean success = true;
        for (String role : roles) {
            if (!role.startsWith(prefix)) {
                role = prefix + role;
            }
            SimpleGrantedAuthority temp = new SimpleGrantedAuthority(role);
            if (isAdd) {
                success &= grantCache.add(temp);
            } else {
                success &= grantCache.remove(temp);
            }
        }
        grant = grantCache.stream().map(r -> {
            String authority = r.getAuthority();
            if (authority.startsWith(prefix)) {
                authority = authority.substring(prefix.length());
            }
            return authority;
        }).collect(Collectors.joining(","));
        return success;
    }

    public void clearGrant() {
        this.grant = null;
        if (!ObjectUtils.isEmpty(grantCache)) {
            grantCache.clear();
        }
    }

}
