package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {
	private static final long serialVersionUID = -2049465268349586283L;
	private static SplashScreen screen;

	public SplashScreen(Object filename, Frame f) throws MalformedURLException
    {
        super(f);
        ImageIcon icon = null;
        if (filename instanceof URL)
        	icon = new ImageIcon((URL)filename);
        else if (filename instanceof String)
        	icon = new ImageIcon((String)filename);
        JLabel l = new JLabel(icon);
        l.setOpaque(false);
        l.setBackground(new Color(0, 0, 0, 0));
        getContentPane().add(l, BorderLayout.CENTER);
        setBackground(new Color(0, 0, 0, 0));
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2), screenSize.height/2 - (labelSize.height/2));
    }
	
	public static void hideIt() {
		if (screen == null) {
			System.err.println("The screen is not shown");
			return;
		}
		screen.setVisible(false);
        screen.dispose();
	}
	
	public static void showIt(Object urlorpath) {
		Frame frame = new Frame();
		frame.setSize(600, 400);
		
		try {
			screen = new SplashScreen(urlorpath, frame);
			screen.setVisible(true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}