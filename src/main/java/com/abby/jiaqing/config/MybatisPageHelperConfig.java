package com.abby.jiaqing.config;

import com.github.pagehelper.PageInterceptor;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPageHelperConfig {
    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactoryBean sessionFactoryBean(){
        SqlSessionFactoryBean sessionFactoryBean=new SqlSessionFactoryBean();
        sessionFactoryBean.setPlugins(new PageInterceptor());
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean;
    }
}
