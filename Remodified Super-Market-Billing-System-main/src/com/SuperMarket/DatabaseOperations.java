package com.techvidvan;

import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
	private static Connection conn;

	public static void dbInit() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "manoj@123");

			Statement statement = conn.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS customers (\n"
					+ "  id INT AUTO_INCREMENT PRIMARY KEY,\n"
					+ "  name VARCHAR(255) NOT NULL,\n"
					+ "  phone VARCHAR(20) NOT NULL,\n"
					+ "  email VARCHAR(255),\n"
					+ "  address VARCHAR(255)\n"
					+ ");");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS products (\n"
					+ "  id INT AUTO_INCREMENT PRIMARY KEY,\n"
					+ "  name VARCHAR(255) NOT NULL,\n"
					+ "  price DECIMAL(10,2) NOT NULL,\n"
					+ "  stock INT NOT NULL\n"
					+ ");");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS orders (\n"
					+ "  id INT AUTO_INCREMENT PRIMARY KEY,\n"
					+ "  customer_id INT NOT NULL,\n"
					+ "  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
					+ "  FOREIGN KEY (customer_id) REFERENCES customers(id)\n"
					+ ");");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS order_items (\n"
					+ "  id INT AUTO_INCREMENT PRIMARY KEY,\n"
					+ "  order_id INT NOT NULL,\n"
					+ "  product_id INT NOT NULL,\n"
					+ "  quantity INT NOT NULL,\n"
					+ "  price DECIMAL(10,2) NOT NULL,\n"
					+ "  timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
					+ "  FOREIGN KEY (order_id) REFERENCES orders(id),\n"
					+ "  FOREIGN KEY (product_id) REFERENCES products(id)\n"
					+ ");");

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static int createNewOrder(int custID, String timestamp) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (customer_id, order_date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1, custID);
		ps.setTimestamp(2, Timestamp.valueOf(timestamp));
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		int oid = rs.getInt(1);
		rs.close();
		ps.close();
		return oid;
	}

	public static void addOrderItem(int orderID, int prodID, int quantity, Float price, String timestamp) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("INSERT INTO order_items (order_id, product_id, quantity, price, timestamp) VALUES (?, ?, ?, ?, ?);");
		ps.setInt(1, orderID);
		ps.setInt(2, prodID);
		ps.setInt(3, quantity);
		ps.setFloat(4, price);
		ps.setTimestamp(5, Timestamp.valueOf(timestamp));
		ps.executeUpdate();
		ps.close();
	}

	public static void discardOrder(int orderID) throws SQLException {
		String sql = "DELETE FROM order_items WHERE order_id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, orderID);
		ps.executeUpdate();
		ps = conn.prepareStatement("DELETE FROM orders WHERE id = ?");
		ps.setInt(1, orderID);
		ps.executeUpdate();
		ps.close();
	}

	public static void deleteOrderItem(int orderID) throws SQLException {
		String sql = "DELETE FROM order_items WHERE id = (SELECT MAX(id) FROM order_items WHERE order_id = ?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, orderID);
		ps.executeUpdate();
		ps.close();
	}

	public static float getTotalPrice(int orderID) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT SUM(price) as total_price FROM order_items WHERE order_id = ?");
		ps.setInt(1, orderID);
		ResultSet rs = ps.executeQuery();
		float price = 0;
		if (rs.next()) {
			price = rs.getFloat("total_price");
		}
		rs.close();
		ps.close();
		return price;
	}

	public static boolean addProduct(String name, float price, int stock) throws SQLException {
		// Check if product already exists
		String query = "SELECT id, stock FROM products WHERE name = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			// Product exists, update stock
			int id = rs.getInt("id");
			int currentStock = rs.getInt("stock");
			query = "UPDATE products SET stock = ? WHERE id = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, currentStock + stock);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			return false;
		}

		// Insert new product
		query = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
		stmt = conn.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setFloat(2, price);
		stmt.setInt(3, stock);
		stmt.executeUpdate();
		return true;
	}

	public static List<String> getCustomers() throws SQLException {
		List<String> customers = new ArrayList<>();
		String sql = "SELECT id, name FROM customers";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			customers.add(rs.getInt("id") + "-|-" + rs.getString("name"));
		}
		rs.close();
		ps.close();
		return customers;
	}

	public static List<String> getProducts() throws SQLException {
		List<String> products = new ArrayList<>();
		String sql = "SELECT id, name FROM products";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			products.add(rs.getInt("id") + "-|-" + rs.getString("name"));
		}
		rs.close();
		ps.close();
		return products;
	}

	public static String[] getProd(int id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		String[] product = null;
		if (rs.next()) {
			product = new String[]{rs.getString("id"), rs.getString("name"), rs.getString("price"), rs.getString("stock")};
		}
		rs.close();
		ps.close();
		return product;
	}

	public static boolean addCustomer(String name, String phone, String email, String address) throws SQLException {
		// Check if customer already exists
		String query = "SELECT COUNT(*) FROM customers WHERE name = ? AND phone = ? AND email = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setString(2, phone);
		stmt.setString(3, email);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		if (rs.getInt(1) > 0) {
			return false;
		}

		// Insert new customer
		query = "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)";
		stmt = conn.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setString(2, phone);
		stmt.setString(3, email);
		stmt.setString(4, address);
		stmt.executeUpdate();
		return true;
	}

	public static void delete(int id, String table) throws SQLException {
		String query = "DELETE FROM " + table + " WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.executeUpdate();

		// Reorganize IDs after deletion
		query = "SET @count = 0";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		query = "UPDATE " + table + " SET id = @count:= @count + 1";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
	}

	public static void updateCombox(String table, JComboBox<String> cbx) throws SQLException {
		cbx.removeAllItems();
		String sql = "SELECT * FROM " + table;
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			cbx.addItem(rs.getString("id") + "-|-" + rs.getString("name"));
		}
		rs.close();
		ps.close();
	}

	public static void loadData(DefaultTableModel model, String table) throws SQLException {
		// Clear the existing rows from the table model
		model.setRowCount(0);

		// Query to select all data from the specified table
		String sql = "SELECT * FROM " + table;

		// Prepare the SQL statement
		PreparedStatement ps = conn.prepareStatement(sql);

		// Execute the query
		ResultSet rs = ps.executeQuery();

		// Define an array to hold each row's data
		Object[] row = new Object[model.getColumnCount()];

		// Iterate through the result set and add each row to the table model
		while (rs.next()) {
			for (int i = 0; i < row.length; i++) {
				row[i] = rs.getObject(i + 1);
			}
			model.addRow(row);
		}

		// Close the ResultSet and PreparedStatement to release resources
		rs.close();
		ps.close();
	}

	public static void updateStock(int prodID, int newStock) throws SQLException {
		String query = "UPDATE products SET stock = ? WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, newStock);
		stmt.setInt(2, prodID);
		stmt.executeUpdate();
		stmt.close();
	}


	public static int getStock(int prodID) throws SQLException {
		String query = "SELECT stock FROM products WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, prodID);
		ResultSet rs = stmt.executeQuery();
		int stock = 0;
		if (rs.next()) {
			stock = rs.getInt("stock");
		}
		rs.close();
		stmt.close();
		return stock;
	}

	public static List<String[]> getDailyReport(String date) throws SQLException {
		String sql = "SELECT orders.id, customers.name, products.name, order_items.quantity, order_items.price " +
				"FROM orders " +
				"JOIN order_items ON orders.id = order_items.order_id " +
				"JOIN customers ON orders.customer_id = customers.id " +
				"JOIN products ON order_items.product_id = products.id " +
				"WHERE DATE(orders.order_date) = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setDate(1, Date.valueOf(date));
		ResultSet rs = ps.executeQuery();
		List<String[]> reportList = new ArrayList<>();
		while (rs.next()) {
			String[] row = new String[5];
			row[0] = rs.getString(1);
			row[1] = rs.getString(2);
			row[2] = rs.getString(3);
			row[3] = rs.getString(4);
			row[4] = rs.getString(5);
			reportList.add(row);
		}
		rs.close();
		ps.close();
		return reportList;
	}

	public static List<String[]> getMonthlyReport(String month) throws SQLException {
		String sql = "SELECT orders.id, customers.name, products.name, order_items.quantity, order_items.price " +
				"FROM orders " +
				"JOIN order_items ON orders.id = order_items.order_id " +
				"JOIN customers ON orders.customer_id = customers.id " +
				"JOIN products ON order_items.product_id = products.id " +
				"WHERE DATE_FORMAT(orders.order_date, '%Y-%m') = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, month);
		ResultSet rs = ps.executeQuery();
		List<String[]> reportList = new ArrayList<>();
		while (rs.next()) {
			String[] row = new String[5];
			row[0] = rs.getString(1);
			row[1] = rs.getString(2);
			row[2] = rs.getString(3);
			row[3] = rs.getString(4);
			row[4] = rs.getString(5);
			reportList.add(row);
		}
		rs.close();
		ps.close();
		return reportList;
	}
}