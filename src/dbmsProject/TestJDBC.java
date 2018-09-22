package dbmsProject;

/***********
 * 
 *  MariaDB [(none)]> GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY 'root' WITH GRANT OPTION;
 * 
 * download and place on your java classpath:
 * www.cise.ufl.edu/~pjd/courses/4301/notes/mariadb-java-client-2.2.3.jar
 * 
 * Statements:
 * (1) Parse SQL Query
 * (2) Compile SQL Query
 * (3) Plan/optimize data acquisition path
 * (4) Execute query and return result set
 * 
 * Prepared Statements:
 * (1) - (3) combined
 * (4) follows
 * 
************/

import java.sql.*;

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
		this.db = "festive";
		this.username = "root";+
		this.password = "root";
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

class TestJDBC {
	public static void main(String[] args) {
		DBConnection dbc = new DBConnection();
		String sql, list[];
		ResultSet rs;

		dbc.openConnection();

		dbc.update("DELETE FROM Beers;");

		rs = dbc.query("SELECT * FROM Beers;", null);
		try {
			while (rs.next()) {
				System.out.println("( " + rs.getString("name") + ", " + rs.getInt("Calories") + " ) ");
			}
			System.out.println("That's all the Beers folks!");
			System.out.println();
		} catch (SQLException sqle) {
		}

		sql = "INSERT INTO Beers ( name, brewer, calories ) " + "VALUES " + "(          'Bud', 'Anheuser', 180 ), "
				+ "(    'Bud Light', 'Anheuser', 125 ), " + "(        'Busch', 'Anheuser', 200 ), "
				+ "(       'Corona',    'Crown', 175 ), " + "(       'Modela',    'Crown', 225 ), "
				+ "(          'MGD',   'Miller', 220 ), " + "( 'Miller Light',   'Miller', 110 ), "
				+ "(   'Wicked Ale',  'Pete\\'s', 200 );";

		dbc.update(sql);

		rs = dbc.query("SELECT * FROM Beers;", null);
		try {
			while (rs.next()) {
				System.out.println("( " + rs.getString("name") + ", " + rs.getInt("Calories") + " ) ");
			}
			System.out.println("That's all the Beers folks!");
			System.out.println();
		} catch (SQLException sqle) {
		}

		sql = "SELECT * FROM Beers WHERE brewer = ?";
		list = new String[1];
		list[0] = "Miller";

		rs = dbc.query(sql, list);
		try {
			while (rs.next()) {
				System.out.println("( " + rs.getString("name") + ", " + rs.getInt("Calories") + " ) ");
			}
			System.out.println("That's all the Beers folks!");
			System.out.println();
		} catch (SQLException sqle) {
		}

		dbc.closeConnection();
	}
}
