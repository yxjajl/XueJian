package com.dh.sqlexe;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

@Component
public class JdbcDAO {
	@Resource 
	private JdbcTemplate jdbcTemplate;
//	@Resource
//	private DataSourceTransactionManager dataSourceTransactionManager;
	
	public void execute(String sql) {
		jdbcTemplate.execute(sql);
	}
	
	public void batchSql(String[] sql) {
		jdbcTemplate.batchUpdate(sql);
	}
}
