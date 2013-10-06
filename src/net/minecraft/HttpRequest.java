package net.minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HttpRequest {

	public static String sendPost(String adress, String key, String value) throws IOException{
		String result = "";
		OutputStreamWriter writer = null;
		BufferedReader reader = null;
		try {
			//encodage des paramètres de la requête
			String data="";
			data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
			
			//création de la connection
			URL url = new URL(adress);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			//envoi de la requête
			writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(data);
			writer.flush();

			//lecture de la réponse
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String ligne;
			while ((ligne = reader.readLine()) != null) {
				result += ligne;
			}
		}catch (Exception e) {
			return "Impossible de charger les news";
			//e.printStackTrace();
		}finally{
			try{writer.close();}catch(Exception e){}
			try{reader.close();}catch(Exception e){}
		}
		return result;
	}
}