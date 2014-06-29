package com.epam.training.jp.jdbc.excercises.dao.jdbcimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.epam.training.jp.jdbc.excercises.dao.RestaurantDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;
import com.epam.training.jp.jdbc.excercises.domain.Food;
import com.epam.training.jp.jdbc.excercises.domain.Restaurant;
import com.epam.training.jp.jdbc.excercises.dto.RestaurantWithAddress;

public class JdbcRestaurantDao extends GenericJdbcDao implements RestaurantDao {

	public JdbcRestaurantDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Food> getFoodsAvailable(Date date, String restaurantName) {
		String sql = "SELECT f.id, f.name, price, calories, isvegan FROM menu m "
				+ "JOIN menu_food mf ON m.id = mf.menu_id "
				+ "JOIN food f ON mf.food_id = f.id "
				+ "JOIN restaurant r ON m.id = m.Restaurant_ID "
				+ "WHERE ? BETWEEN fromdate AND todate "
				+ "AND r.name = ?";
						
		try(Connection connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql)) {
			
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ps.setString(2, restaurantName);
			
			List<Food> foods = new ArrayList<Food>();
			try(ResultSet resultSet = ps.executeQuery()) {
				Food food;
				while(resultSet.next()) {
					food = new Food();
					food.setId(resultSet.getInt(1));
					food.setName(resultSet.getString(2));
					food.setPrice(resultSet.getInt(3));
					food.setCalories(resultSet.getInt(4));
					food.setVegan(resultSet.getBoolean(5));
					foods.add(food);
				}
			}
			return foods;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<RestaurantWithAddress> getAllRestaurantsWithAddress() {
		String sql = "SELECT r.id, r.name, "
				+ "a.id, a.city, a.country, a.street, a.zipcode "
				+ "FROM restaurant r "
				+ "JOIN address a ON a.id = r.address_id ";
		
		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {			
			List<RestaurantWithAddress> restaurantWithAddresses = new ArrayList<RestaurantWithAddress>();
			try (ResultSet rs = ps.executeQuery()) {
				Restaurant restaurant;
				Address address;
				while (rs.next()) {
					restaurant = new Restaurant();
					restaurant.setId(rs.getInt("r.id"));
					restaurant.setName(rs.getString("r.name"));
					address = new Address();
					address.setCity(rs.getString("a.city"));
					address.setCountry(rs.getString("a.country"));
					address.setStreet(rs.getString("a.street"));
					address.setZipCode(rs.getString("a.zipcode"));
					restaurantWithAddresses.add(new RestaurantWithAddress(restaurant, address));
				}
			}
			return restaurantWithAddresses;
		} 
		catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	



}
