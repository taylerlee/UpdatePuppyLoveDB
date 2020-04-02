package data.jdbc;
import java.sql.*;

abstract public class JDBC {
	private String connStr = null;
	private Connection conn = null;
	private String server;
	private int port;
	private String database;
	private String user;
	private String password;
	
	public JDBC(String serverIP,
			int serverPort,
			String user,
			String password) {
		this.server = serverIP;
		this.port = serverPort;
		this.user = user;
		this.password = password;
	}
	
	public JDBC(String server,
			int port,
			String database,
			String user,
			String password) {
		this.server = server;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
	public String getServer() {
		return server;
	}

	public int getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConn() {
		return conn;
	}
	
	protected void setConn(Connection conn) {
		this.conn = conn;
	}

	protected String getConnStr() {
		return connStr;
	}

	protected void setConnStr(String connStr) {
		this.connStr = connStr;
	}
	
	/**
	 * default set auto commit is false
	 * @return
	 */
	abstract public boolean createConnection();
	
	/**
	 * default set auto commit is false
	 * @return
	 */
	abstract public boolean createConnection(String database);
	
	public void connectionClose() throws SQLException {
		this.conn.close();
	}

	/**
	 * Set MSSQL server Database Engine.
	 * @param autoCommit : True is explicit transaction, false is implicit transaction
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.conn.setAutoCommit(autoCommit);
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		this.conn.commit();
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		this.conn.rollback();
	}

	/**
	 * 
	 * @param sqlStr
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sqlStr) throws SQLException {
		return conn.createStatement().executeQuery(sqlStr);
	}

	/**
	 * 
	 * @param sqlStr
	 * @return int
	 * @throws SQLException
	 */
	public int executeUpdate(String sqlStr) throws SQLException {
		return conn.createStatement().executeUpdate(sqlStr);
	}
}