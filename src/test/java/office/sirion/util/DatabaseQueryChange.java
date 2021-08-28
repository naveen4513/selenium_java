package office.sirion.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import office.sirion.base.TestBase;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DatabaseQueryChange extends TestBase {	
	private static Scanner scanner;
	static File file = new File(CONFIG.getProperty("DatabaseQueryFilePath"));	
	static Connection connection = null;
	static Statement statement = null;
	static Statement resultStatement = null;
	static ResultSet resultSet = null;
	static String clientShowPageLoginID = null;

	public static void clientUserPasswordChange(String query) throws ClassNotFoundException, SQLException, InterruptedException {
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(CONFIG.getProperty("DatabaseURL"),	CONFIG.getProperty("DatabaseUsername"),	CONFIG.getProperty("DatabasePassword"));
		Logger.debug("Connected to Database -- " + CONFIG.getProperty("DatabaseURL"));

		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				clientShowPageLoginID = Integer.toString(id);
				}
			
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				final String lineFromFile = scanner.nextLine();
				if (lineFromFile.contains("id = "+clientShowPageLoginID+";")) {
					Logger.debug("We found " + clientShowPageLoginID + " in file "+ file.getName());
					statement.executeUpdate(lineFromFile);
					}
				}
			} catch (Exception e) {
				Logger.debug("Failed to Execute" + " " + query + " The error is "+ e.getMessage());
				}
				
				finally {
					connection.close();
				}
		}
	
	public static String getClauseDatabaseID (String query) throws ClassNotFoundException, SQLException {
		String clauseID = null;

		Class.forName("org.postgresql.Driver");

		connection = DriverManager.getConnection(CONFIG.getProperty("DatabaseURL"),	CONFIG.getProperty("DatabaseUsername"),	CONFIG.getProperty("DatabasePassword"));
		Logger.debug("Connected to Database -- " + CONFIG.getProperty("DatabaseURL"));
		
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		resultSet = statement.executeQuery(query);

		boolean resultStatus = resultSet.last();

		try {
			if (resultStatus) {
				if (resultSet.getRow()>1)
					Assert.fail("System has more than two entities with the Same Name for Same Client");
				else {
					int clauseDBID = resultSet.getInt("client_entity_seq_id");
					clauseID = "CL0"+Integer.toString(clauseDBID);
					}
				}
			else
				Assert.fail("There is no Such Clause in the System for this Client");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				connection.close();
				}

		return clauseID;
		}
	}