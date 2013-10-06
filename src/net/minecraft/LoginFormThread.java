package net.minecraft;

import java.net.URL;
import javax.swing.JTextPane;

	public class LoginFormThread extends Thread
	{
		private JTextPane editorPane;
 
 	public LoginFormThread(JTextPane editorPane)
 	{
 		this.editorPane = editorPane;
 	}
 
    public void run()
    {
        try {
        	editorPane.setPage(new URL("http://alienation-gaming.fr/LauncherMystiCraft/news_launcher.html"));
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
      }
}