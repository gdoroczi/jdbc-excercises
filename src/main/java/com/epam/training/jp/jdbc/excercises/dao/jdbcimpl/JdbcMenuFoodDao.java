package com.epam.training.jp.jdbc.excercises.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.epam.training.jp.jdbc.excercises.dao.MenuFoodDao;
import com.epam.training.jp.jdbc.excercises.domain.Food;

public class JdbcMenuFoodDao extends GenericJdbcDao implements MenuFoodDao {

	public JdbcMenuFoodDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void removeMenuFoods(int menuId) {
		String sql = "DELETE FROM menu_food where menu_id = ?";
		
		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, menuId);
			ps.execute();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
}
