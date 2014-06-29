package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.RestaurantDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;
import com.epam.training.jp.jdbc.excercises.domain.Food;
import com.epam.training.jp.jdbc.excercises.domain.Restaurant;
import com.epam.training.jp.jdbc.excercises.dto.RestaurantWithAddress;

public class JdbcTemplateRestaurantDao extends JdbcDaoSupport implements RestaurantDao {

	public JdbcTemplateRestaurantDao(DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public List<Food> getFoodsAvailable(Date date, String restaurantName) {
		String sql = "SELECT FOOD.ID, FOOD.CALORIES, FOOD.ISVEGAN, FOOD.NAME, FOOD.PRICE "
				+ "FROM FOOD "
				+ "JOIN MENU_FOOD ON FOOD.ID = MENU_FOOD.FOOD_ID "
				+ "JOIN MENU ON MENU.ID = MENU_FOOD.MENU_ID "
				+ "JOIN RESTAURANT ON RESTAURANT.ID = MENU.RESTAURANT_ID "
				+ "WHERE RESTAURANT.NAME = ? AND ? BETWEEN FROMDATE AND TODATE";
		
		return getJdbcTemplate().query(sql,
				new Object[] {date, restaurantName},
				new FoodMapper()
				);
	}
	
	private static final class FoodMapper implements RowMapper<Food> {

		@Override
		public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
			Food food = new Food();
			
			food.setId(rs.getInt(1));
			food.setCalories(rs.getInt(2));
			food.setVegan(rs.getBoolean(3));
			food.setName(rs.getString(4));
			food.setPrice(rs.getInt(5));
			
			return food;
		}
		
	}

	@Override
	public List<RestaurantWithAddress> getAllRestaurantsWithAddress() {
		String sql = "SELECT RESTAURANT.ID, RESTAURANT.NAME, "
				+ "	ADDRESS.ID, ADDRESS.CITY, ADDRESS.COUNTRY, ADDRESS.STREET, ADDRESS.ZIPCODE "
				+ "FROM RESTAURANT "
				+ "JOIN ADDRESS ON ADDRESS.ID = RESTAURANT.ADDRESS_ID ";
		
		return getJdbcTemplate().query(sql,
				new Object[] {},
				new RestaurantWithAddressMapper()
				);
		
	}
	
	private static final class RestaurantWithAddressMapper implements RowMapper<RestaurantWithAddress> {

		@Override
		public RestaurantWithAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
			Restaurant restaurant = new Restaurant();
			restaurant.setId(rs.getInt("RESTAURANT.ID"));
			restaurant.setName(rs.getString("RESTAURANT.NAME"));
			Address address = new Address();
			address.setCity(rs.getString("ADDRESS.CITY"));
			address.setCountry(rs.getString("ADDRESS.COUNTRY"));
			address.setStreet(rs.getString("ADDRESS.STREET"));
			address.setZipCode(rs.getString("ADDRESS.ZIPCODE"));
			
			return new RestaurantWithAddress(restaurant, address);
		}
	
	}
	
}