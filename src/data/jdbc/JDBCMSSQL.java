package data.jdbc;

import java.sql.*;

public class JDBCMSSQL extends JDBC {
	
	public JDBCMSSQL(
			String jdbcDriver,
			String server,
			int port,
			String user,
			String password) throws ClassNotFoundException, SQLException {
		super(server, port, user, password);		
		Class.forName(jdbcDriver);
	}

	@Override
	public boolean createConnection() {
		super.setConnStr("jdbc:sqlserver://%s:%d;");
		try {
			super.setConn(DriverManager.getConnection(String.format(super.getConnStr(), super.getServer(), super.getPort()), super.getUser(), super.getPassword()));
			setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public boolean createConnection(String database) {
		super.setConnStr("jdbc:sqlserver://%s:%d;database=%s");
		try {
			super.setConn(DriverManager.getConnection(String.format(super.getConnStr(), super.getServer(), super.getPort(), database), super.getUser(), super.getPassword()));
			setAutoCommit(false);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
}
