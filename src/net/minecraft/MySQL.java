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
	   /* Set all inUse = 1 to 0 */
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
	   /* Check if login and password are correct according to the DB */
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
   
   public static boolean checkIfUserExistsInDb(String login) {
	   /* Check if the user named 'login' exists in DB */
		try {
			Statement stm = connexion.createStatement();
			ResultSet res = stm.executeQuery("SELECT `Login` FROM `vose_adm` WHERE `Login` = '" + login + "'");
			if (res.next()) {
				if (login.equals(res.getString("Login")))
					return true;
			}
		} catch(Exception e) {
			System.err.println("Connexion à la base de donnée impossible.");
		}
		System.err.println("Login doesn't exist in DB.");
		return false;
	}
   
   public static String getUserRank(String username) {
	   /* Get the user Rank value */
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
	   /* Check if the id 'id' correspond to a user in DB */
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
	   /* Set the id 'id' to a user in the guest base */
	   try {
		   Statement stm = connexion.createStatement();
		   String SQL = "SELECT * FROM `facebook_users` WHERE `id` = '" + id + "'";
		   ResultSet res = stm.executeQuery(SQL);
		   if (res.next() && res.getString("id").isEmpty() == false)
		   {
				SQL = "UPDATE `vose_users` SET `id` = '" + res.getString("id") + "' WHERE `vose_users`.`pseudo` = '" + user + "'";
				stm.executeUpdate(SQL);
				return true;
		   }
	   } catch(Exception e) {
		   System.err.println("Connexion à la base de donnée impossible.");
		   return false;
	   }
	   return false;
   }
   
   public static void changePassword(String username, String password) {
	   /* Everything is in the function name */
	   try {
			Statement stm = connexion.createStatement();
			String SQL = "UPDATE `vose_adm` SET `Password` = '" + password + "' WHERE `vose_adm`.`Login` = '" + username + "'";
			stm.executeUpdate(SQL);
		} catch(Exception e) {
			e.printStackTrace();
		}
   }
   
   public static boolean createUser(String username, String password, String rank) {
	   /* Create new user */
	   try {
		   Statement stm = connexion.createStatement();
		   String SQL = "INSERT INTO `VOSE`.`vose_adm` (`Login`, `Password`, `Rank`) VALUES ('" + username + "', '" + password + "', '" + rank + "');";
		   stm.executeUpdate(SQL);
		   return true;
	   } catch(Exception e) {
			e.printStackTrace();
			return false;
		}
   }
}
