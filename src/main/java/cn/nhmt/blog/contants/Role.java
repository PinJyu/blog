package cn.nhmt.blog.contants;

/**
 * @Description: spring security web role
 * @Date: 2020-06-26 16:55
 * @Author: PinJyu
 * @Version: 1.0
 **/
public enum Role {

    ADMIN,

    AUTHOR,

    VISITOR,

    BANNER;

    public static String DEFAULT_PREFIX = "ROLE_";

    private static String prefix = DEFAULT_PREFIX;

    public static void setPrefix(String prefix) {
        Role.prefix = prefix;
    }

    public static String getPrefix() {
        return prefix;
    }

    public String getLiteRole() {
        return this.name().toUpperCase();
    }

    public String getFullRole() {
        return prefix + this.getLiteRole();
    }

}
