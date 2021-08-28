package office.sirion.util;

//import com.sirionlabs.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static office.sirion.base.TestBase.CONFIG;

/**
 * Created by Naveen Kumar Gupta on 08/06/18.
 */


public class PostgreSQLJDBC {

	private final static Logger logger = LoggerFactory.getLogger(PostgreSQLJDBC.class);


	String dbName = "Automation_Gladiator_Development_13042018";
	String hostAddress = "192.168.2.152";
	String portName = "5432";
	String userName = "postgres";
	String password = "postgres";
	String driverclass = "org.postgresql.Driver";


	/**
	 * Connection object to create db connection
	 */
	private Connection conn = null;

	/**
	 * Constructor which will make db connect based on the environment
	 */
	public PostgreSQLJDBC() {
		try {
			Class.forName(driverclass);

			if (CONFIG.getProperty("DatabaseURL") != null && CONFIG.getProperty("DatabaseUsername") != null && CONFIG.getProperty("DatabasePassword") != null) {
				conn = DriverManager.getConnection(CONFIG.getProperty("DatabaseURL"), CONFIG.getProperty("DatabaseUsername"), CONFIG.getProperty("DatabasePassword"));
			} else {
				logger.info("DB Credential is not specified by User Correctly in EnvironmentConfig File : Taking default hardcode Values");
				conn = DriverManager.getConnection("jdbc:postgresql://" + hostAddress + ":" + portName + "/" + dbName,
						userName, password);
			}

			logger.debug("Opened database successfully");


		} catch (Exception e) {
			logger.info(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement when there is only equal check in where clause.
	 *
	 * @param tableName   is the name of table on which query needed to built
	 * @param params      the list of parameters - name value
	 * @param orderParams the name of the parameters that will be used to sort the
	 *                    result and also the sorting order. Each entry will be (column,
	 *                    sortingOrder).
	 * @return the where and order by clause for a SQL statement
	 */


	public String getQueryClauses(final String tableName, final Map<String, Object> params, final Map<String, Object> orderParams) {
		final StringBuffer queryString = new StringBuffer();
		queryString.append("select * from " + tableName + " where ");

		if ((params != null) && !params.isEmpty()) {


			for (final Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
				final Map.Entry<String, Object> entry = it.next();
				if (entry.getValue() instanceof Boolean) {
					queryString.append(entry.getKey()).append(" is ").append(entry.getValue()).append(" ");
				} else {
					if (entry.getValue() instanceof Number) {
						queryString.append(entry.getKey()).append(" = ").append(entry.getValue());
					} else {
						// string equality
						queryString.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
					}
				}

				if (it.hasNext()) {
					queryString.append(" and ");
				}

			}
		}
		if ((orderParams != null) && !orderParams.isEmpty()) {
			queryString.append(" order by ");
			for (final Iterator<Map.Entry<String, Object>> it = orderParams.entrySet().iterator(); it.hasNext(); ) {
				final Map.Entry<String, Object> entry = it.next();
				queryString.append(entry.getKey()).append(" ");
				if (entry.getValue() != null) {
					queryString.append(entry.getValue());
				}
				if (it.hasNext()) {
					queryString.append(", ");
				}
			}
		}
		return queryString.toString();
	}

	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement(generic to any comparator for where clause)
	 *
	 * @param tableName  is the name of table on which query needed to built
	 * @param params     the list of parameters - name value
	 * @param comparator the list of comparator for parameters - name value pair
	 * @return the where and order by clause for a SQL statement
	 */
	public String getQueryClauses(final String tableName, final Map<String, Object> params, final List<String> comparator) {
		return getQueryClauses(tableName, params, comparator, null);
	}

	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement(generic to any comparator for where clause)
	 *
	 * @param tableName           is the name of table on which query needed to built
	 * @param columnNamesToSelect is the name of column names of table on which needed to selected
	 * @param params              the list of parameters - name value
	 * @param comparator          the list of comparator for parameters - name value pair
	 * @return the where and order by clause for a SQL statement
	 */
	public String getQueryClauses(final String tableName, final List<String> columnNamesToSelect, final Map<String, Object> params, final List<String> comparator) {
		return getQueryClauses(tableName, columnNamesToSelect, params, comparator, null);
	}


	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement(generic to any comparator for where clause)
	 *
	 * @param tableName   is the name of table on which query needed to built
	 * @param params      the list of parameters - name value
	 * @param comparator  the list of comparator for parameters - name value pair
	 * @param orderParams the name of the parameters that will be used to sort the
	 *                    result and also the sorting order. Each entry will be (column,
	 *                    sortingOrder).
	 * @return the where and order by clause for a SQL statement
	 */
	public String getQueryClauses(final String tableName, final Map<String, Object> params, final List<String> comparator, final Map<String, Object> orderParams) {
		return getQueryClauses(tableName, null, params, comparator, orderParams);
	}

	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement(generic to any comparator for where clause) with specific column Names to be selected from tableNAme
	 *
	 * @param tableName           is the name of table on which query needed to built
	 * @param columnNamesToSelect is the name of column names of table on which needed to selected
	 * @param params              the list of parameters - name value
	 * @param comparator          the list of comparator for parameters - name value pair
	 * @param orderParams         the name of the parameters that will be used to sort the
	 *                            result and also the sorting order. Each entry will be (column,
	 *                            sortingOrder).
	 * @return the where and order by clause for a SQL statement
	 */
	public String getQueryClauses(final String tableName, final List<String> columnNamesToSelect, final Map<String, Object> params, final List<String> comparator, final Map<String, Object> orderParams) {
		final StringBuffer queryString = new StringBuffer();
		queryString.append("select ");

		if ((columnNamesToSelect != null) && !columnNamesToSelect.isEmpty()) {

			for (int i = 0; i < columnNamesToSelect.size(); i++) {
				queryString.append(columnNamesToSelect.get(i));
				if (i != (columnNamesToSelect.size() - 1)) {
					queryString.append(" , ");
				}

			}

			queryString.append(" from " + tableName + " where ");


		} else // in case user haven't specify anything in columnNamesToSelect
			queryString.append(" * from " + tableName + " where ");

		return getQueryClausesBuilder(queryString, params, comparator, orderParams);
	}


	/**
	 * This method returns appropriate query clauses to be executed within a SQL
	 * statement(generic to any comparator for where clause)
	 *
	 * @param queryString is the query form till select clause
	 * @param params      the list of parameters - name value
	 * @param comparator  the list of comparator for parameters - name value pair
	 * @param orderParams the name of the parameters that will be used to sort the
	 *                    result and also the sorting order. Each entry will be (column,
	 *                    sortingOrder).
	 * @return the where and order by clause for a SQL statement
	 */
	public String getQueryClausesBuilder(final StringBuffer queryString, final Map<String, Object> params, final List<String> comparator, final Map<String, Object> orderParams) {
		int comparatorindex = 0; // index for comparator

		if ((params != null) && !params.isEmpty()) {


			for (final Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {


				if (comparator.get(comparatorindex).toLowerCase().trim().contentEquals("equal")) {

					final Map.Entry<String, Object> entry = it.next();
					if (entry.getValue() instanceof Boolean) {
						queryString.append(entry.getKey()).append(" is ").append(entry.getValue()).append(" ");
					} else {
						if (entry.getValue() instanceof Number) {
							queryString.append(entry.getKey()).append(" = ").append(entry.getValue());
						} else {
							// string equality
							queryString.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
						}
					}

				}

				if (comparator.get(comparatorindex).toLowerCase().trim().contentEquals("ilike")) {

					final Map.Entry<String, Object> entry = it.next();
					queryString.append(entry.getKey()).append(" ilike ").append(" '").append(entry.getValue()).append("'");


				}

				if (comparator.get(comparatorindex).toLowerCase().trim().contentEquals("greaterthan")) {

					final Map.Entry<String, Object> entry = it.next();
					queryString.append(entry.getKey()).append(" > '").append(entry.getValue()).append("'");


				}


				if (it.hasNext()) {
					queryString.append(" and ");
					comparatorindex++;

				}

			}
		}
		if ((orderParams != null) && !orderParams.isEmpty()) {
			queryString.append(" order by ");
			for (final Iterator<Map.Entry<String, Object>> it = orderParams.entrySet().iterator(); it.hasNext(); ) {
				final Map.Entry<String, Object> entry = it.next();
				queryString.append(entry.getKey()).append(" ");
				if (entry.getValue() != null) {
					queryString.append(entry.getValue());
				}
				if (it.hasNext()) {
					queryString.append(", ");
				}
			}
		}
		return queryString.toString();
	}


	/**
	 * @param sql - This is the Sql Query which will be executed in the database set by constructor
	 * @return - this function will the result of executed query as the list of list of string
	 * @throws SQLException
	 */
	public List<List<String>> doSelect(String sql) throws SQLException {
		List<List<String>> results = new ArrayList<List<String>>();

		logger.debug("Executing query : " + sql);

		Statement st = null;
		ResultSet rs = null;
		try {

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				ResultSetMetaData meta = rs.getMetaData();
				List<String> lines = new ArrayList<String>();

				for (int col = 1; col < meta.getColumnCount() + 1; col++) {
					lines.add(rs.getString(col));
				}
				results.add(lines);
			}

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}

		}

		logger.debug("Result returned after running " + sql + " is: " + results);
		return results;

	}


	// the following two function are there for verifying whether records are sorted on not based on postgres sorting algorithm
	// this function is for comparing two records based on sorting algorithm used in postgresql
	public boolean compareTwoRecordsForAscOrEqual(String firstRecord, String secondRecord) throws SQLException {
		Statement stmt;
		boolean result = false;
		stmt = conn.createStatement();
		ResultSet rs = null;
		try {
			// this replace all will avoid throwing exception if Record has "'" char
			String query = "select \'" + firstRecord.replaceAll("'", "") + "\' <= \'" + secondRecord.replaceAll("'", "") + "\' as result;";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				result = rs.getBoolean("result");

			}

		} catch (Exception e) {
			logger.info(e.getClass().getName() + ": " + e.getMessage());

		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			return result;

		}


	}

	// this function is for comparing two records based on sorting algorithm used in postgresql
	public boolean compareTwoRecordsForDescOrEqual(String firstRecord, String secondRecord) throws SQLException {
		Statement stmt;
		boolean result = false;
		stmt = conn.createStatement();
		ResultSet rs = null;
		try {
			// this replace all will avoid throwing exception if Record has "'" char
			String query = "select \'" + firstRecord.replaceAll("'", "") + "\' >= \'" + secondRecord.replaceAll("'", "") + "\' as result;";
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				result = rs.getBoolean("result");

			}


		} catch (Exception e) {

			logger.info(e.getClass().getName() + ": " + e.getMessage());

		} finally {

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			return result;

		}
	}


	/**
	 * Update db
	 *
	 * @param query
	 * @return true or false based on successful execution
	 */
	public boolean updateDBEntry(String query) throws SQLException {
		Statement st = null;
		try {
			st = conn.createStatement();
			boolean qstatus = st.execute(query);
			return qstatus;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}

		}
		return false;

	}

	/**
	 * delete db entry
	 *
	 * @param query
	 * @return true or false based on successful execution
	 */
	public boolean deleteDBEntry(String query) throws SQLException {
		Statement st = null;
		try {
			st = conn.createStatement();
			boolean qstatus = st.execute(query);
			return qstatus;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}
		}
		return false;
	}

	/**
	 * Once All the db operation is done it will be called to close the db
	 * connection.
	 */
	public void closeConnection() {
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
