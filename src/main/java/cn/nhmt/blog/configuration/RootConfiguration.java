package cn.nhmt.blog.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.beans.PropertyVetoException;

@Configuration
@ComponentScan("cn.nhmt.blog.service")
@PropertySource("classpath:jdbc.properties")  // 属性文件
@EnableAspectJAutoProxy(exposeProxy = true)  // 暴露代理对象
@EnableTransactionManagement(proxyTargetClass = true) //  允许使用注解事务
public class RootConfiguration {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    // 配置c3p0连接池
    @Bean(value = "dataSource", destroyMethod = "close")
    public ComboPooledDataSource c3p0DataSource() throws PropertyVetoException {
        ComboPooledDataSource cpds= new ComboPooledDataSource();
        cpds.setDriverClass(driver);
        cpds.setJdbcUrl(url);
        cpds.setUser(username);
        cpds.setPassword(password);
        return cpds;
    }

    // 配置mybatis 会话工厂
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws PropertyVetoException {
        SqlSessionFactoryBean ssfb = new SqlSessionFactoryBean();
        ssfb.setDataSource(c3p0DataSource());
        ssfb.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        return ssfb;
    }

    // 配置mapper扫描注册
    @Bean
    public static MapperScannerConfigurer mapperScanner() {
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("cn.nhmt.blog.dao");
        return msc;
    }

    // 配置jdbc事务管理器
    @Bean("transactionManager")
    public DataSourceTransactionManager jdbcTrancactionManager() throws PropertyVetoException {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager();
        dstm.setDataSource(c3p0DataSource());
        return dstm;
    }

    // 数据表初始化, 不好用
    @Bean("dataSourceInitializer")
    public DataSourceInitializer initTable(@Value("${jdbc.inittable}") String enable) throws PropertyVetoException {
        ResourceDatabasePopulator rdp = new ResourceDatabasePopulator();
        rdp.setScripts(new ClassPathResource("init-table.sql"));
        rdp.setSqlScriptEncoding("UTF-8");

        CompositeDatabasePopulator cdpInit = new CompositeDatabasePopulator(rdp);
        CompositeDatabasePopulator cdpDestory = new CompositeDatabasePopulator();

        DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(c3p0DataSource());
        dsi.setEnabled(Boolean.valueOf(enable));
        dsi.setDatabasePopulator(cdpInit);
        dsi.setDatabaseCleaner(cdpDestory);
        return dsi;
    }
}
