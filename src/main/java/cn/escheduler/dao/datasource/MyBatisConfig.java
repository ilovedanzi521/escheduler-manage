/****************************************************
 * 创建人：  @author xiep
 * 创建时间: 2017-11-7/14:30:36
 * 项目名称: ycmp-api
 * 文件名称: MyBatisConfig.java
 * 文件描述: @Description
 * 公司名称: 深圳市赢和信息技术有限公司
 *
 * All rights Reserved, Designed By 深圳市赢和信息技术有限公司
 * @Copyright:2016-2017
 *
 ********************************************************/

package cn.escheduler.dao.datasource;

import cn.escheduler.common.Constants;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static cn.escheduler.dao.utils.PropertyUtils.*;


/**
 * @Description: Mybatis基础配置
 */
@Configuration
@PropertySource({"classpath:dao/data_source.properties"})
@MapperScan(basePackages = "cn.escheduler.dao.mapper", sqlSessionFactoryRef = "defaultSqlSessionFactory")

public class MyBatisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfig.class);

    /**
     * env.
     */
    @Autowired
    private Environment env;

    /**
     * @Title: defaultDataSource
     * @Description: 创建数据源
     * @return DataSource
     * @throws SQLException
     */
    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource defaultDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setDriverClassName(getString(Constants.SPRING_DATASOURCE_DRIVER_CLASS_NAME));
        druidDataSource.setUrl(getString(Constants.SPRING_DATASOURCE_URL));
        druidDataSource.setUsername(getString(Constants.SPRING_DATASOURCE_USERNAME));
        druidDataSource.setPassword(getString(Constants.SPRING_DATASOURCE_PASSWORD));
        druidDataSource.setValidationQuery(getString(Constants.SPRING_DATASOURCE_VALIDATION_QUERY));

        druidDataSource.setPoolPreparedStatements(getBoolean(Constants.SPRING_DATASOURCE_POOL_PREPARED_STATEMENTS));
        druidDataSource.setTestWhileIdle(getBoolean(Constants.SPRING_DATASOURCE_TEST_WHILE_IDLE));
        druidDataSource.setTestOnBorrow(getBoolean(Constants.SPRING_DATASOURCE_TEST_ON_BORROW));
        druidDataSource.setTestOnReturn(getBoolean(Constants.SPRING_DATASOURCE_TEST_ON_RETURN));
        druidDataSource.setKeepAlive(getBoolean(Constants.SPRING_DATASOURCE_KEEP_ALIVE));

        druidDataSource.setMinIdle(getInt(Constants.SPRING_DATASOURCE_MIN_IDLE));
        druidDataSource.setMaxActive(getInt(Constants.SPRING_DATASOURCE_MAX_ACTIVE));
        druidDataSource.setMaxWait(getInt(Constants.SPRING_DATASOURCE_MAX_WAIT));
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(getInt(Constants.SPRING_DATASOURCE_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE));
        druidDataSource.setInitialSize(getInt(Constants.SPRING_DATASOURCE_INITIAL_SIZE));
        druidDataSource.setTimeBetweenEvictionRunsMillis(getLong(Constants.SPRING_DATASOURCE_TIME_BETWEEN_EVICTION_RUNS_MILLIS));
        druidDataSource.setTimeBetweenConnectErrorMillis(getLong(Constants.SPRING_DATASOURCE_TIME_BETWEEN_CONNECT_ERROR_MILLIS));
        druidDataSource.setMinEvictableIdleTimeMillis(getLong(Constants.SPRING_DATASOURCE_MIN_EVICTABLE_IDLE_TIME_MILLIS));
        druidDataSource.setValidationQueryTimeout(getInt(Constants.SPRING_DATASOURCE_VALIDATION_QUERY_TIMEOUT));
        //auto commit
        druidDataSource.setDefaultAutoCommit(getBoolean(Constants.SPRING_DATASOURCE_DEFAULT_AUTO_COMMIT));

        return druidDataSource;
    }

    /**
     * @Title: sqlSessionFactoryBean
     * @Description: 获取session工厂
     * @param defaultDataSource 默认的数据源
     * @return SqlSessionFactory
     */
    @Primary
    @Bean(name = "defaultSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("defaultDataSource") DataSource defaultDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        /**
         * 关于session的全局缓存，否则将导致同一微服务部署多台，缓存不同步问题，
         * 需要设置 CacheEnabled=false ，LocalCacheScope=Statement
         */
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        bean.setConfiguration(configuration);

        bean.setDataSource(defaultDataSource);
        bean.setTypeAliasesPackage("cn.escheduler.dao.model");

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            return bean.getObject();
        } catch (Exception e) {
        	LOGGER.error("mybatis初始化sqlSessionFactoryBean失败", e);
        }
        return null;
    }

    /**
     * @Title: sqlSessionTemplate
     * @Description: sqlSessionTemplate实例
     * @param sqlSessionFactory session工厂
     * @return SqlSessionTemplate
     */
    @Primary
    @Bean("defaultSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * @Title: transactionManager
     * @Description: 事物管理
     * @param dataSource 数据源
     * @return DataSourceTransactionManager
     */
    @Primary
    @Bean(name = "TransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("defaultDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
