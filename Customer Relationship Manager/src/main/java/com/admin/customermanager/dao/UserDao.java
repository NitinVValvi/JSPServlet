package com.admin.customermanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.admin.customermanager.bean.Customer;

public class UserDao {

	private String jdbcURL = "jdbc:mysql://localhost:3306/custom?useSSL=false";
	
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	private String jdbcDriver = "com.mysql.jdbc.Driver";

private static final String INSERT_CUSTOMERS_SQL ="INSERT INTO customers" + " (first_name,last_name,email) VALUES"
+ " (?,?,?); ";

private static final String SELECT_CUSTOMERS_BY_ID = "select id,first_name,last_name,email from customers where id = ?";

private static final String SELECT_ALL_CUSTOMERS = "select * from customers";

private static final String DELETE_CUSTOMERS_SQL = "delete from customers where id = ?;";

private static final String UPDATE_CUSTOMER_SQL = "update customers set first_name = ?,last_name=?,email=? where id = ?;";

public UserDao() {
	}

protected Connection getConnection() {
	
	Connection connection  = null;
	try {
		Class.forName(jdbcDriver);
		connection = DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
	}catch(SQLException e)
	{
		e.printStackTrace();
	}catch(ClassNotFoundException e) {
		e.printStackTrace();
	}
	return connection;
}

//insert user 

public void insertCustomer(Customer customer) throws SQLException{
	
	System.out.println(INSERT_CUSTOMERS_SQL);
	try(Connection connection = getConnection();
			PreparedStatement preparedStatement= connection.prepareStatement(INSERT_CUSTOMERS_SQL)) {
		preparedStatement.setString(1, customer.getFirst_name());
		preparedStatement.setString(2, customer.getLast_name());
		preparedStatement.setString(3, customer.getEmail());

	System.out.println(preparedStatement);
	preparedStatement.executeUpdate();
	
	
	} catch (SQLException e) {
		// TODO: handle exception
	printSQLException(e);
	}
}

//select customer by id
public Customer selectCustomer(int id) {
	
	Customer customer = null;
	
	try(Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMERS_BY_ID);) {
		preparedStatement.setInt(1, id);
		
		System.out.println(preparedStatement);
		
		
		ResultSet rs = preparedStatement.executeQuery();
		
		
		while(rs.next()) {
			String first_name =rs.getString("first_name");
			String last_name =rs.getString("last_name");
			String email =rs.getString("email");
			
			customer = new Customer(id,first_name,last_name,email);

		}
	} catch (SQLException e) {
		// TODO: handle exception
		printSQLException(e);
	}
	return customer;
}




//select all customer

public List<Customer> selectAllCustomer(){
	
	List<Customer> customers = new ArrayList<>();
	
	try(Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS);) {
		
		
		System.out.println(preparedStatement);
		
		
		ResultSet rs = preparedStatement.executeQuery();
		
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String first_name =rs.getString("first_name");
			String last_name =rs.getString("last_name");
			String email =rs.getString("email");
			
			customers.add(new Customer(id,first_name,last_name,email));

		}
	} catch (SQLException e) {
		// TODO: handle exception
		printSQLException(e);
	}
	return customers;
}

//update customer


public boolean updateCustomer(Customer customer)throws SQLException{
	
	
	boolean rowUpdated;
	try(Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUSTOMER_SQL);) {
		System.out.println("updated user: "+preparedStatement);
		
		preparedStatement.setString(1, customer.getFirst_name());
		preparedStatement.setString(2, customer.getLast_name());
		preparedStatement.setString(3, customer.getEmail());
		
		
		preparedStatement.setInt(4,customer.getId());
		
		rowUpdated = preparedStatement.executeUpdate()>0;
		
	}
	return rowUpdated;
		
		
}
//delete customer

public boolean deleteCustomer(int id) throws SQLException{

	boolean rowDeleted;
	try(Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUSTOMERS_SQL);) {
		
		preparedStatement.setInt(1, id);
		
		rowDeleted = preparedStatement.executeUpdate()>0;
	}
	return rowDeleted;
	
}


private void printSQLException(SQLException ex) {

	for(Throwable e : ex) {
		if (e instanceof SQLException) {
			
			e.printStackTrace(System.err);
			System.err.println("SQLState: " +((SQLException) e).getSQLState());
			System.err.println("Error code: " +((SQLException) e).getErrorCode());
			System.err.println("Message: " +e.getMessage());

			Throwable t = ex.getCause();
			while(t!= null) {
				System.out.println("cause " +t );
				t= t.getCause();
			}
		
		
		
		}
	}
	
}



}
