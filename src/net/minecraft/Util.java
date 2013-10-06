 package net.minecraft;
 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
 
public class Util
 {
   public static String URL_DL = "http://62.210.215.38/";
   private static File workDir = null;
	
   /**
    * Encrypt a string with md5 digest
    * @param string : plain string to encrypt
    * @return encrypted string
    */
   public static String md5(String string)
   {
	   byte[] bytesOfMessage;
	   byte[] thedigest = null;
	   BigInteger bigInt = null;
	   String hashText = null;
	   try {
		   bytesOfMessage = string.getBytes("UTF-8");
		   MessageDigest md;
		   md = MessageDigest.getInstance("MD5");
		   thedigest = md.digest(bytesOfMessage);
		   bigInt = new BigInteger(1,thedigest);
		   hashText = bigInt.toString(16);
		   while(hashText.length() < 32 ){
			   hashText = "0"+hashText;
			 }
	   } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
	   } catch (UnsupportedEncodingException e1) {
		   e1.printStackTrace();
	   }
	   return (hashText);
   }

   /**
    * This fonction get the news in html already formated. You can use the result in JtextPane.
    *  
    * @param date
    * @param title
    * @param icon : format "/images/icon.png"
    * @param content : use \r\n for new line
    * @param autor
    * @return formated html news. 
    */
   public static String getNewsTemplate(String date, String title, String icon, String content, String autor) {
	   return (
			   "<p><span style=\"color: #000000; font-size: small;\"><span style=\"font-size: x-small;\">" + date + "</span></span></p>" +
			   "<p><span style=\"font-size: medium;\"><strong><span style=\"font-size: x-large;\">" + title + "</span></strong></span></p>" +
			   "<p>" +
			   "<table border=\"0\">" +
			   "<tbody>" +
			   "<tr>" +
			   "<td><img src=\"http://jpo-virtuelle-epitech.eu/" + icon + "\" alt=\"newsIcon\" width=\"128\" height=\"128\" /></td>" +
			   "<td>" +
			   "<p><span style=\"font-size: medium;\">" + content + "</span></p>" +
			   "</td>" +
			   "</tr>" +
			   "</tbody>" +
			   "</table>" +
			   "</p>" +
			   "<p style=\"text-align: right;\"><span style=\"color: #000000;\"><em><font size=\"2\">" + autor + "</font><br /></em></span></p>" +
			   "<hr />" +
			   "<p><span style=\"color: #888888; font-size: small;\"><em>&nbsp;</em></span></p>" +
			   "nextNews");
   }
   
   public static File getWorkingDirectory() {
     if (workDir == null) workDir = getWorkingDirectory("vose");
     return workDir;
   }
 
   
     
    public static File getWorkingDirectory(String applicationName) {
    String userHome = System.getProperty("user.home", ".");
    File workingDirectory;
    if(getPlatform() == OS.solaris || getPlatform() ==  OS.linux)
    {
      workingDirectory = new File(userHome, '.' + applicationName + '/');
    }
    else if(getPlatform() == OS.windows)
    {
      String applicationData = System.getenv("APPDATA");
      if (applicationData != null) workingDirectory = new File(applicationData, "." + applicationName + '/'); else
        workingDirectory = new File(userHome, '.' + applicationName + '/');
    }
    else if(getPlatform() == OS.macos)
    {
      workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
    }
    else
    {
      workingDirectory = new File(userHome, applicationName + '/');
    }
    
    if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) throw new RuntimeException("Le répertoire de travail n'a pas pu être créé: " + workingDirectory);
    return workingDirectory;
  }
 
   private static OS getPlatform() {
     String osName = System.getProperty("os.name").toLowerCase();
     if (osName.contains("win")) return OS.windows;
     if (osName.contains("mac")) return OS.macos;
     if (osName.contains("solaris")) return OS.solaris;
     if (osName.contains("sunos")) return OS.solaris;
     if (osName.contains("linux")) return OS.linux;
     if (osName.contains("unix")) return OS.linux;
     return OS.unknown;
   }
 
   public static String excutePost(String targetURL, String urlParameters)
   {
     HttpsURLConnection connection = null;
     try
     {
       URL url = new URL(targetURL);
       connection = (HttpsURLConnection)url.openConnection();
       connection.setRequestMethod("POST");
       connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 
       connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
       connection.setRequestProperty("Content-Language", "en-US");
 
       connection.setUseCaches(false);
       connection.setDoInput(true);
       connection.setDoOutput(true);
 
       connection.connect();
       Certificate[] certs = connection.getServerCertificates();
 
       byte[] bytes = new byte[294];
       DataInputStream dis = new DataInputStream(Util.class.getResourceAsStream("minecraft.key"));
       dis.readFully(bytes);
       dis.close();
 
       Certificate c = certs[0];
       PublicKey pk = c.getPublicKey();
       byte[] data = pk.getEncoded();
 
       for (int i = 0; i < data.length; i++) {
         if (data[i] == bytes[i]) continue; throw new RuntimeException("Les clés publics sont différentes");
       }
 
       DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
       wr.writeBytes(urlParameters);
       wr.flush();
       wr.close();
 
       InputStream is = connection.getInputStream();
       BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 
       StringBuffer response = new StringBuffer();
       String line;
       while ((line = rd.readLine()) != null)
       {
         response.append(line);
         response.append('\r');
       }
       rd.close();
 
       String str1 = response.toString();
       return str1;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       return null;
     }
     finally
     {
       if (connection != null)
         connection.disconnect();
     }
   }
 
   public static boolean isEmpty(String str) {
     return (str == null) || (str.length() == 0);
   }
 
   public static void openLink(URI uri) {
     try {
       Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
       o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, new Object[] { uri });
     } catch (Throwable e) {
       System.out.println("Erreur à l'ouverture du lien " + uri.toString());
     }
   }
 
   private static enum OS
   {
     linux, solaris, windows, macos, unknown;
   }
 }