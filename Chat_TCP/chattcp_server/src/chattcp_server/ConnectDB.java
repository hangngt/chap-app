package chattcp_server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
	private Connection 		  connec = null;
	private PreparedStatement pre 	 = null;
	private Statement 		  stmt 	 = null;
	private ResultSet 		  rs1 	 = null;
	
	public ConnectDB() {

		try {
			String userName = "root";
			String password = "123456789";
			String url = "jdbc:mysql://localhost:3307/chat_tcp";
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Connecting database ...");
			connec = DriverManager.getConnection(url, userName, password);
			System.out.println("1. Connected ok");
		} catch (Exception e) {
			System.out.println("2. Connected error");
		}	
	}

	public void newAccount(String username, String pass) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_INF_ACCOUNT (t_Username,t_Pass) VALUES(?,?)");
			pre.setString(1, username);
			pre.setString(2, pass);
			pre.executeUpdate();
		} catch (Exception e) {
		}
	}
	
	public void newRoom(String nameroom) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_INF_ROOMS (r_Name) VALUES(?)");
			pre.setString(1, nameroom);
			pre.executeUpdate();
		} catch (Exception e) {
		}
	}

	public ResultSet login(String username) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Pass from TA_INF_ACCOUNT where t_Username = '" + username + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet roomsdisplay(String nameroom) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select r_Name from TA_INF_ROOMS where r_Name = '" + nameroom + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet checkPass() {
		try {
			stmt 	   = connec.createStatement();
			String sql = "select t_Username, t_Pass from TA_INF_ACCOUNT";
			rs1 	   = stmt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs1;
	}

	public void changePass(String username, String new_pass, String pass_repeat) {
		try {
			stmt 	   = connec.createStatement();
			String sql = "UPDATE TA_INF_ACCOUNT " + " SET " + " t_Pass= '" + new_pass + "'" + "WHERE t_Username = '" + username + "'";
			stmt.executeUpdate(sql);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet checkExist() {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Username from TA_INF_ACCOUNT ");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResultSet checkID(String username) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_ID from TA_INF_ACCOUNT where t_Username = '" + username + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet checkIDroom(String nameroom) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_ID from TA_INF_ROOMS where t_Username = '" + nameroom + "'");
			return rs1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setMsg(int idSend, String msg, String date) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_MSG_MESS (i_IDuser,t_Msg,d_Date) VALUES(?,?,?)");
			pre.setInt(1, idSend);
			pre.setString(2, msg);
			pre.setString(3, date);
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getMsg() {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_IDuser, t_Msg from  TA_MSG_MESS ORDER BY i_IDmsg");
			return rs1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setMsgroom(int idSend, String msg, String date) {
		try {
			pre = connec.prepareStatement("INSERT INTO TA_ROOM_MSG (i_IDuser,r_Messbofy,d_Date) VALUES(?,?,?)");
			pre.setInt(1, idSend);
			pre.setString(2, msg);
			pre.setString(3, date);
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet getMsgroom() {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select i_IDuser, r_Messbofy from  TA_ROOM_MSG ORDER BY r_IDroom");
			return rs1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public ResultSet name(int id) {
		try {
			stmt = connec.createStatement();
			rs1  = stmt.executeQuery("select t_Username from TA_INF_ACCOUNT where i_ID = '" + id + "'");
			return rs1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
