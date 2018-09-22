package dbmsProject;

/*Charan Kotha
 * HW 11*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

class DBConnection {

	private static final String JDBC_DRIVER = "org.mysql.jdbc.Driver";
	private static final String LOCAL_HOST = "jdbc:mariadb://localhost:3306/";

	private String db;
	private String username;
	private String password;
	// private Statement statement;
	private PreparedStatement preparedStatement;
	private Connection connection;

	public DBConnection() {
		this.db = "mall";
		this.username = "root";
		this.password = "1006";
	}

	public DBConnection(String db, String username, String password) {
		this.db = db;
		this.username = username;
		this.password = password;
	}

	public boolean openConnection() {
		boolean result = false;

		try {
			connection = DriverManager.getConnection(LOCAL_HOST + db, username, password);
			System.out.println(db + " connected.");
			System.out.println();

			// statement = connection.createStatement();

			result = true;
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return result;
	}

	public ResultSet query(String sql, String args[]) {
		ResultSet rs = null;
		int i;

		try {
			// rs = statement.executeQuery( sql );

			preparedStatement = connection.prepareStatement(sql);

			if (args != null) {
				for (i = 0; i < args.length; i++) {
					preparedStatement.setString((i + 1), args[i]);
				}
			}
			rs = preparedStatement.executeQuery();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return rs;
	}

	public int update(String sql) {
		int result = 0;

		try {
			// result = statement.executeUpdate( sql );

			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return result;
	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}

public class HW10 {
	@SuppressWarnings({ "null", "resource" })
	public static void main(String[] args) throws SQLException {
		DBConnection dbc = new DBConnection();
		String sql, list[];
		ResultSet rs = null;
		Scanner reader = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean c = true;

		Connection conn = null;

		dbc.openConnection();

		System.out.println("---------------------Welcome!---------------------");
		System.out.println("\n");
		while (c) {
			boolean b = true;
			boolean d = true;
			boolean f = true;
			System.out.println("Please choose from the following list of options:");
			System.out.println("1. Enter a complete SQL Command");
			System.out.println("2. Select a table from the list of relations");
			System.out.println("3. Update the table");
			System.out.println("4. Exit");

			System.out.println("Enter your number now:");

			int a = reader.nextInt();
			if (a == 1 || a == 2 || a == 3 || a == 4) {
				if (a == 1) {
					while (b) {
						String s = "";
						System.out.println("Enter your SQL query");
						try {
							s = br.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}

						rs = dbc.query(s, null);
						ResultSetMetaData rsmd = rs.getMetaData();
						String columns = "";
						int columnsNumber = rsmd.getColumnCount();
						for (int i = 1; i <= columnsNumber; i++) {
							columns += rsmd.getColumnName(i) + "  ";
						}
						// Iterate through the data in the result set and display it.
						System.out.println(columns);
						while (rs.next()) {
							// Print one row
							for (int i = 1; i <= columnsNumber; i++) {
								System.out.print(rs.getString(i) + " "); // Print one element of a row
							}
							System.out.println();// Move to the next line to print the next row.
						}
						System.out.println("\n");
						b = false;
					}
				} else if (a == 2) {
					while (d) {
						String s = "";
						String columns = "";
						System.out.println("Enter the name of the table that you want to select:");
						s = reader.next();
						if (!s.equals(null)) {
							rs = dbc.query("SELECT * FROM " + s, null);
							System.out.println("Here is the table!");
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnsNumber = rsmd.getColumnCount();
							for (int i = 1; i <= columnsNumber; i++) {
								columns += rsmd.getColumnName(i) + "  ";
							}

							System.out.println(columns);
							// Iterate through the data in the result set and display it.
							while (rs.next()) {
								// Print one row
								for (int i = 1; i <= columnsNumber; i++) {
									System.out.print(rs.getString(i) + " "); // Print one element of a row
								}
								System.out.println();// Move to the next line to print the next row.
							}
							System.out.println("\n");

							System.out.println(
									"Would you like to select columns from " + s + "? Type 1 for yes and 2 for no");
							int x = reader.nextInt();
							if (x == 1) {
								String s2 = "";
								System.out.println("What columns would you like to select from the table?");
								s2 = reader.next();
								rs = dbc.query("SELECT " + s2 + " FROM " + s, null);
								rsmd = rs.getMetaData();
								columnsNumber = rsmd.getColumnCount();
								for (int i = 1; i <= columnsNumber; i++) {
									columns += rsmd.getColumnName(i) + "  ";
								}

								System.out.println(columns);
								// Iterate through the data in the result set and display it.
								while (rs.next()) {
									// Print one row
									for (int i = 1; i <= columnsNumber; i++) {
										System.out.print(rs.getString(i) + " "); // Print one element of a row
									}
									System.out.println();// Move to the next line to print the next row.
								}
								System.out.println("\n");

								System.out.println(
										"Would you like to search the column for a range of values or a specific value? Type 1 for Yes and 2 for No");
								int y = reader.nextInt();
								if (y == 1) {
									System.out.println(
											"Are you looking for a numerical value or a text value? Enter 1 for numerical and 2 for text");
									int z = reader.nextInt();
									if (z == 1) {
										String s3 = "";
										System.out.println("Enter the operation you want to do i.e <,<=, >, >= or =");
										s3 = reader.next();
										System.out.println("Enter the value");
										int z2 = reader.nextInt();
										rs = dbc.query("SELECT " + s2 + " FROM " + s + " " + "WHERE" + " " + s2 + " "
												+ s3 + " " + z2, null);
										rsmd = rs.getMetaData();
										columnsNumber = rsmd.getColumnCount();
										for (int i = 1; i <= columnsNumber; i++) {
											columns += rsmd.getColumnName(i) + "  ";
										}

										System.out.println(columns);
										// Iterate through the data in the result set and display it.
										while (rs.next()) {
											// Print one row
											for (int i = 1; i <= columnsNumber; i++) {
												System.out.print(rs.getString(i) + " "); // Print one element of a row
											}
											System.out.println();// Move to the next line to print the next row.
										}
										System.out.println("\n");
										d = false;
									} else if (z == 2) {
										String s4 = "";
										System.out.println("Enter the value you want to find");
										s4 = reader.next();
										rs = dbc.query("SELECT " + s2 + " FROM " + s + " " + "WHERE" + " " + s2 + " "
												+ "=" + " " + "'" + s4 + "'", null);
										rsmd = rs.getMetaData();
										columnsNumber = rsmd.getColumnCount();
										for (int i = 1; i <= columnsNumber; i++) {
											columns += rsmd.getColumnName(i) + "  ";
										}

										System.out.println(columns);
										// Iterate through the data in the result set and display it.
										while (rs.next()) {
											// Print one row
											for (int i = 1; i <= columnsNumber; i++) {
												System.out.print(rs.getString(i) + " "); // Print one element of a row
											}
											System.out.println();// Move to the next line to print the next row.
										}
										System.out.println("\n");
									}
								}
							}
							d = false;
						}
					}
				} else if (a == 3) {
					String s4 = "";
					System.out.println(
							"Would you like to enter query to update or select from tables and columns? Enter 1 for a query and 2 for tables");
					int v = reader.nextInt();
					if (v == 1) {
						while (f) {
							String s = "";
							System.out.println("Enter your SQL update");
							try {
								s = br.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							}

							rs = dbc.query(s, null);
							int x = dbc.update(s);
							ResultSetMetaData rsmd = rs.getMetaData();
							String columns = "";

							String columns2 = rsmd.getColumnName(2);
							String columns3 = rsmd.getColumnName(3);
							String columns4 = rsmd.getColumnName(4);
							String columns5 = rsmd.getColumnName(5);

							int columnsNumber = rsmd.getColumnCount();
							for (int i = 1; i <= columnsNumber; i++) {
								columns += rsmd.getColumnName(i) + "  ";
							}
							// Iterate through the data in the result set and display it.
							System.out.println(columns);
							while (rs.next()) {
								// Print one row
								for (int i = 1; i <= columnsNumber; i++) {
									System.out.print(rs.getString(i) + " "); // Print one element of a row
								}
								System.out.println();// Move to the next line to print the next row.
							}
							System.out.println("\n");
						}
						f = false;
					} else if (v == 2) {
						String s = "";
						String columns = "";
						System.out.println("Enter the name of the table that you want to select:");
						s = reader.next();
						if (!s.equals(null)) {
							rs = dbc.query("SELECT * FROM " + s, null);
							int y = dbc.update("SELECT * FROM " + s);
							System.out.println("Here is the table!");
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnsNumber = rsmd.getColumnCount();
							for (int i = 1; i <= columnsNumber; i++) {
								columns += rsmd.getColumnName(i) + "  ";
							}

							System.out.println(columns);
							// Iterate through the data in the result set and display it.
							while (rs.next()) {
								// Print one row
								for (int i = 1; i <= columnsNumber; i++) {
									System.out.print(rs.getString(i) + " "); // Print one element of a row
								}
								System.out.println();// Move to the next line to print the next row.
							}
							System.out.println("\n");

							System.out.println(
									"Would you like to select columns from " + s + "? Type 1 for yes and 2 for no");
							int x = reader.nextInt();
							if (x == 1) {
								String s2 = "";
								System.out.println("What columns would you like to select from the table?");
								s2 = reader.next();
								rs = dbc.query("SELECT " + s2 + " FROM " + s, null);
								int u = dbc.update("SELECT " + s2 + " FROM " + s);
								rsmd = rs.getMetaData();

								columnsNumber = rsmd.getColumnCount();
								for (int i = 1; i <= columnsNumber; i++) {
									columns += rsmd.getColumnName(i) + "  ";
								}

								System.out.println(columns);
								// Iterate through the data in the result set and display it.
								while (rs.next()) {
									// Print one row
									for (int i = 1; i <= columnsNumber; i++) {
										System.out.print(rs.getString(i) + " "); // Print one element of a row
									}
									System.out.println();// Move to the next line to print the next row.
								}
								System.out.println("\n");

								System.out.println(
										"Would you like to search the column for a range of values or a specific value? Type 1 for Yes and 2 for No");
								int h = reader.nextInt();
								if (h == 1) {
									System.out.println(
											"Are you looking for a numerical value or a text value? Enter 1 for numerical and 2 for text");
									int z = reader.nextInt();
									if (z == 1) {
										String s3 = "";
										System.out.println("Enter the operation you want to do i.e <,<=, >, >= or =");
										s3 = reader.next();
										System.out.println("Enter the value");
										int z2 = reader.nextInt();
										rs = dbc.query("SELECT " + s2 + " FROM " + s + " " + "WHERE" + " " + s2 + " "
												+ s3 + " " + z2, null);
										rsmd = rs.getMetaData();
										columnsNumber = rsmd.getColumnCount();
										for (int i = 1; i <= columnsNumber; i++) {
											columns += rsmd.getColumnName(i) + "  ";
										}

										System.out.println(columns);
										// Iterate through the data in the result set and display it.
										while (rs.next()) {
											// Print one row
											for (int i = 1; i <= columnsNumber; i++) {
												System.out.print(rs.getString(i) + " "); // Print one element of a row
											}
											System.out.println();// Move to the next line to print the next row.
										}
										System.out.println("\n");
										d = false;
									} else if (z == 2) {
										String s5 = "";
										System.out.println("Enter the value you want to find");
										s4 = reader.next();
										rs = dbc.query("SELECT " + s2 + " FROM " + s + " " + "WHERE" + " " + s2 + " "
												+ "=" + " " + "'" + s5 + "'", null);
										rsmd = rs.getMetaData();
										columnsNumber = rsmd.getColumnCount();
										for (int i = 1; i <= columnsNumber; i++) {
											columns += rsmd.getColumnName(i) + "  ";
										}

										System.out.println(columns);
										// Iterate through the data in the result set and display it.
										while (rs.next()) {
											// Print one row
											for (int i = 1; i <= columnsNumber; i++) {
												System.out.print(rs.getString(i) + " "); // Print one element of a row
											}
											System.out.println();// Move to the next line to print the next row.
										}
										System.out.println("\n");
									}
								}
							}
							d = false;
						}

					}

				} else if (a == 4) {
					System.out.println("Thank you for using the Mall database!");
					c = false;
				}
			} else {
				System.out.println("Please enter a valid number");
				System.exit(0);
			}
		}

		dbc.closeConnection();
	}
}
