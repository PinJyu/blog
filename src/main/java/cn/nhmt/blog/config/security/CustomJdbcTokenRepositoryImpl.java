package cn.nhmt.blog.config.security;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * @Description: dynamic init table
 * @Date: 2020-07-05 13:31
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class CustomJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

    public CustomJdbcTokenRepositoryImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public void initDao() {
        if (getJdbcTemplate() != null) {
            getJdbcTemplate().execute("create table if not exists persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
                    + "token varchar(64) not null, last_used timestamp not null)");
        }
    }

    @Deprecated
    @Override
    public void setCreateTableOnStartup(boolean createTableOnStartup) {
        throw new UnsupportedOperationException("Create table operation in \"initDao()\" "
                + "with \"create table if not exists, don't use this method.\"");
    }

}
