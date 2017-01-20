package connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import sources.IssuesSourceXml;

public class DataBaseConnector implements Connector {

	private Connection dbConn = null;

	public DataBaseConnector(IssuesSourceXml source) {
		this(source.getField("dbUrl"), source.getField("user"), source.getField("pass"));
	}
	
	public DataBaseConnector(String dbUrl, String user, String pass) {
		try {
			dbConn = DriverManager.getConnection("jdbc:mysql://"+dbUrl, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DataBaseConnector con = new DataBaseConnector("localhost/avantrip", "root", "root");
		for(Issue i : con.getIssuesList()) {
			System.out.println(i.getTitle());
		}
	}
	
	@Override
	public List<Issue> getIssuesList() {
		List<Issue> issues = new ArrayList<Issue>();
		
		try {
			Statement stmt = dbConn.createStatement();
			String sql = "SELECT * FROM issue";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				Issue issue = new Issue();

				issue.setSource("Database");
				issue.setTitle(rs.getString("title"));
				issue.setDescription(rs.getString("description"));
				issue.setCreatorName(rs.getString("creator"));
				issue.setCreationDate(rs.getDate("creation_date"));
				
				issues.add(issue);
			}

			rs.close();
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

		return issues;
	}

	@Override
	public void dispose() {
		try {
			if(dbConn!=null)
				dbConn.close();
		} catch(SQLException se) {
			se.printStackTrace();
		}
	}

}
