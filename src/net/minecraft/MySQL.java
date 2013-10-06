package net.minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class MySQL {
	
	private static Connection connexion;
	
	public static boolean connect() {
		System.out.println("Connecting to MySQL Database...");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connexion = DriverManager.getConnection(
					"jdbc:mysql://62.210.215.38:3306/VOSE?autoReconnect=true",
					"VoseWriter", "jdopife74");
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean disconnect() {
		try {
			connexion.close();
			if (connexion.isClosed())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Map<String, Integer> getUserList() {
		/* Get user list on mysql database */
		Map<String, Integer> listUsers = new HashMap<String, Integer>();
		try {
			Statement stm = connexion.createStatement();
			ResultSet res = stm.executeQuery("SELECT `pseudo`, `inUse` FROM `vose_users`");
			while (res.next()) {
				listUsers.put(res.getString("pseudo"), (Integer)res.getInt("inUse"));
			}
		} catch(Exception e) {
			//e.printStackTrace();
			System.err.println("Connexion à la base de donnée impossible.");
			return null;
		}
		return (listUsers);
	}
   
   public static void modifyUserList(String username, int value) { //TODO delete this!
		/* Get user list on mysql database */
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = '" + value + "' WHERE `vose_users`.`pseudo` = '" + username + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
   
   public static boolean freeUserList() {
		try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_users` SET `inUse` = 0, `id` = 0 WHERE `vose_users`.`inUse` = 1";
			stm.executeUpdate(SQL);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
   
   public static String checkUserInDb(String login, String password) {
		try {
			Statement stm = connexion.createStatement();
			ResultSet res = stm.executeQuery("SELECT `Password` FROM `vose_adm` WHERE `Login` = '" + login + "'");
			if (res.next()) {
				if (res.getString("Password").equals("null") && password.equals("37a6259cc0c1dae299a7866489dff0bd")) //md5("null")
					return "defpass";
				if (password.equals(res.getString("Password")))
					return "ok";
			}
		} catch(Exception e) {
			System.err.println("Connexion à la base de donnée impossible.");
		}
		System.err.println("Login ou mot de passe incorrect.");
		return "ko";
	}
   
   public static String getUserRank(String username) {
	   try {
		   Statement stm = connexion.createStatement();
		   String SQL = "SELECT `Rank` FROM `vose_adm` WHERE `Login` = '" + username + "'";
		   ResultSet res = stm.executeQuery(SQL);
		   if (res.next())
			   return (res.getString("Rank"));
		   SQL = "SELECT pseudo FROM `vose_users` WHERE `pseudo` = '" + username + "'";
		   res = stm.executeQuery(SQL);
		   if (res.next())
			   return ("Guest");
	   } catch(Exception e) {
		   System.err.println("Connexion à la base de donnée impossible.");
	   }
	   return "unknown";
   }
   
   
   public static boolean checkUserId(String id) {
	   try {
		   Statement stm = connexion.createStatement();
		   String SQL = "SELECT `id` FROM `facebook_users` WHERE `id` = '" + id + "'";
		   ResultSet res = stm.executeQuery(SQL);
		   if (res.next() && res.getString("id").isEmpty() == false)
			   return true;
	   } catch(Exception e) {
		   System.err.println("Connexion à la base de donnée impossible.");
		   return false;
	   }
	   return false;
   }

   public static boolean setUserCoords(String id, String user) {
	   try {
		   Statement stm = connexion.createStatement();
		   String SQL = "SELECT * FROM `facebook_users` WHERE `id` = '" + id + "'";
		   ResultSet res = stm.executeQuery(SQL);
		   System.out.println("MYSQL => SELECT * FROM `facebook_users` WHERE `id` = '" + id + "'");
		   if (res.next() && res.getString("id").isEmpty() == false)
		   {
				Statement stm_up = connexion.createStatement();
				String SQL_up = "UPDATE `vose_users` SET `id` = '" + res.getString("id") + "' WHERE `vose_users`.`pseudo` = '" + user + "'";
				stm_up.executeUpdate(SQL_up);
				System.out.println("MYSQL => UPDATE `vose_users` SET `id` = '" + res.getString("id") + "' WHERE `vose_users`.`pseudo` = '" + user + "'");
				return true;
		   }
	   } catch(Exception e) {
		   System.err.println("Connexion à la base de donnée impossible.");
		   return false;
	   }
	   return false;
   }
   
   public static void changePassword(String username, String password) {
	   try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_adm` SET `Password` = '" + password + "' WHERE `vose_adm`.`Login` = '" + username + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
   }
}
