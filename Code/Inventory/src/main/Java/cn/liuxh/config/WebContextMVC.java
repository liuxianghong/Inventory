package cn.liuxh.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
 * Created by hxx on 11/16/16.
 */
@Configuration
@ComponentScan(basePackages = "cn.liuxh", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
})
@MapperScan("cn.liuxh.mapper")
public class WebContextMVC {

    private static ApplicationContext appContext;

    public static ApplicationContext getAppContext() {
        return appContext;
    }

    @Autowired
    public void setAppContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }

    @Bean
    @ConfigurationProperties(locations = "classpath:application.properties",prefix="spring.datasource")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/Inventory?characterEncoding=UTF-8&amp;useUnicode=true");
        dataSource.setUsername("root");
        dataSource.setPassword("210749Qq");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return  dataSource;
        //return new org.apache.tomcat.jdbc.pool.DataSource();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());



        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:/mybatis/*.xml"));

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }


}

