package com.epam.training.jp.jdbc.excercises.dao.jdbctemplateimpl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.epam.training.jp.jdbc.excercises.dao.AddressDao;
import com.epam.training.jp.jdbc.excercises.domain.Address;

public class JdbcTemplateAddressDao extends JdbcDaoSupport implements AddressDao {

	public JdbcTemplateAddressDao(DataSource dataSource) {		
		setDataSource(dataSource);
	}

	@Override
	public void save(Address address) {
		getJdbcTemplate().update(
				"INSERT INTO address (city, country, street, zipcode) VALUES (?, ?, ?, ?) ",
				address.getCity(), address.getCountry(), address.getStreet(), address.getZipCode());
	}

}
