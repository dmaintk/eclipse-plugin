package connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import issueslist.model.Issue;
import issueslist.model.IssueSource;

public class DataBaseConnector extends ThreadedConnector {

	private Connection dbConn;
	private IssueSource source;
		
	public DataBaseConnector(IssueSource source) {
		this.source = source;
		
		String dbUrl = source.getFields().get("dbUrl"); 
		String user = source.getFields().get("user"); 
		String pass = source.getFields().get("pass");
		
		try {
			dbConn = DriverManager.getConnection("jdbc:mysql://"+dbUrl, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Statement stmt = dbConn.createStatement();
			String sql = "SELECT * FROM issue";
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()) {
				Issue issue = new Issue();

				issue.setSource(source.getId());
				issue.setTitle(rs.getString("title"));
				issue.setDescription(rs.getString("description"));
				issue.setCreatorName(rs.getString("creator"));
				issue.setCreationDate(rs.getDate("creation_date"));
				
				addIssueToBuffer(issue);
				
                try {
                    Thread.sleep(10);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
			}

			rs.close();
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
    }
	
	@Override
	public void dispose() {
		try {
			if(dbConn != null) {
				dbConn.close();
			}
		} catch(SQLException se) {
			se.printStackTrace();
		}
	}

}
