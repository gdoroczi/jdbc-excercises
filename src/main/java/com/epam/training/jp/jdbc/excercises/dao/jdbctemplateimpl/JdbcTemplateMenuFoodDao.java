package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.MenuFoodDao;

public class JdbcTemplateMenuFoodDao extends JdbcDaoSupport implements MenuFoodDao {

	public JdbcTemplateMenuFoodDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void removeMenuFoods(int id) {
		String sql = "delete from menu_food where menu_id = ?";
		getJdbcTemplate().update(sql, id);
	}
}
