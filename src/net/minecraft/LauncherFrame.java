package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class LauncherFrame extends Frame 
{
	public static final int VERSION = 13;
	private static final long serialVersionUID = 1L;
	public Map<String, String> customParameters = new HashMap<String, String>();
	public Launcher launcher;
	public LoginForm loginForm;
	public LauncherFrame frame;

	public LauncherFrame() {
		super("[VOSE]");
		setBackground(Color.BLACK);
		this.frame = this;
		this.loginForm = new LoginForm(this);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(this.loginForm, "Center");

		p.setPreferredSize(new Dimension(1050, 600));

		setLayout(new BorderLayout());
		add(p, "Center");

		pack();
		setLocationRelativeTo(null);
		try
		{
			setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				new Thread() {
					public void run() {
						try {
							Thread.sleep(30000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("FORCING EXIT!");
						System.exit(0);
					}
				}
				.start();
				if (LauncherFrame.this.launcher != null) {
					LauncherFrame.this.launcher.stop();
					LauncherFrame.this.launcher.destroy();
				}
				System.exit(0);
			} } );
	}

	public void updateDialog() {
		final JOptionPane optionPane = new JOptionPane(
				"Une nouvelle mise a jour est disponible.\n"
						+ "Mettre a jour ?",
						JOptionPane.QUESTION_MESSAGE,
						JOptionPane.YES_NO_OPTION);

		final JDialog dialog = new JDialog(this, "Mise a jour", true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		optionPane.addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent e) {
						String prop = e.getPropertyName();

						if (dialog.isVisible() 
								&& (e.getSource() == optionPane)
								&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
							dialog.setVisible(false);
						}
					}
				});
		dialog.pack();
		dialog.setVisible(true);

		int value = ((Integer)optionPane.getValue()).intValue();
		if (value == JOptionPane.YES_OPTION) {
			this.launcher.gameUpdater.shouldUpdate = true;
			this.launcher.gameUpdater.pauseAskUpdate = false;
		} else if (value == JOptionPane.NO_OPTION) {
			this.launcher.gameUpdater.shouldUpdate = false;
			this.launcher.gameUpdater.pauseAskUpdate = false;
		}
	}

	public void playCached(String userName) {
		try {
			if ((userName == null) || (userName.length() <= 0)) {
				userName = "Player";
			}
			this.launcher = new Launcher();
			this.launcher.customParameters.putAll(this.customParameters);
			this.launcher.customParameters.put("userName", userName);
			this.launcher.init();
			removeAll();
			add(this.launcher, "Center");
			validate();
			this.launcher.start();
			this.loginForm = null;
			setTitle("Mysticraft - Minecraft");
		} catch (Exception e) {
			e.printStackTrace();
			showError(e.toString());
		}
	}

	public static String getFreeUser() {
		/* Get user list on mysql database */
		Map<String, Integer> listUsers = MySQL.getUserList();
		String freeUser = "";

		if (listUsers == null)
			return ("NoNetwork");
		/* Check if there's no free slot */
		if (!listUsers.containsValue(0))
			return ("Full");

		/* Get first free user in list */
		Set<String> keys = listUsers.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()){
			String key = it.next();
			int value = listUsers.get(key);
			if (value == 0) {
				freeUser = key;
				break;
			}
		}
		return (freeUser);
	}

	public void login(String userName, String password)
	{
		//String result = getFreeUser();
		System.out.println("Free user found: " + userName);
		try {
			if (userName.equals("NoNetwork")) {
				showError("Can't connect to EPITECH server.");
				this.loginForm.setNoNetwork();
				return;
			} else if (userName.equals("Full")) {
				showError("No free slot!");
			} else {
				if (userName.startsWith("Guest"))
					MySQL.modifyUserList(userName, 1);
				MySQL.disconnect();
				String[] values = getFakeResult(userName).split(":");
				launcher = new Launcher();
				launcher.customParameters.putAll(customParameters);
				launcher.customParameters.put("userName", values[2].trim());
				launcher.customParameters.put("sessionId", values[3].trim());
				launcher.customParameters.put("latestVersion", values[0].trim());
				launcher.customParameters.put("downloadTicket", values[1].trim());
				launcher.customParameters.put("server", LoginForm.ServerAddr);
				launcher.customParameters.put("port", LoginForm.ServerPort);
				launcher.init();
				removeAll();
				add(launcher, "Center");
				validate();
				launcher.start();
				loginForm.loginOk();
				loginForm = null;
				setTitle("[VOSE] Visite Of School Epitech");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showError(e.toString());
		}
	}
	public String getFakeLatestVersion() {
		try {
			File dir = new File(Util.getWorkingDirectory() + File.separator + "bin" + File.separator);
			File file = new File(dir, "version");
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			String version = dis.readUTF();
			dis.close();
			if (version.equals("0")) {
				return "1285241960000";
			}
			return version; } catch (IOException ex) {
			}
		return "1285241960000";
	}

	public String getFakeResult(String userName) {
		return this.getFakeLatestVersion() + ":35b9fd01865fda9d70b157e244cf801c:" + userName + ":12345:";
	}

	private void showError(String error) {
		removeAll();
		add(this.loginForm);
		this.loginForm.setError(error);
		validate();
	}

	public boolean canPlayOffline(String userName) {
		Launcher launcher = new Launcher();
		launcher.customParameters.putAll(this.customParameters);
		launcher.init(userName, null, null, null);
		return launcher.canPlayOffline();
	}
	public static void main(String[] args) {
		try {
			SplashScreen.showIt(new URL(Util.URL_DL + "VOSE/splashscreen.png"));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		boolean isConnected = false;
		
		isConnected = MySQL.connect();
		if (isConnected == true)
			System.out.println("Database connected successfully");
		else
		{
			//BETA : Mais pour la version finale, il faudra trouver une meilleure solution
			JOptionPane.showMessageDialog(null, "Vous devez avoir internet pour lancer le jeu.", "Erreur : Pas d'accès internet", JOptionPane.ERROR_MESSAGE);
			System.err.println("Please check your Internet connection");
		    System.exit(-1);			
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception localException) {
		}
		final LauncherFrame launcherFrame = new LauncherFrame();
		launcherFrame.setMinimumSize(new Dimension(1050, 600));
		launcherFrame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				MySQL.disconnect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		SplashScreen.hideIt();
		launcherFrame.setVisible(true);
		launcherFrame.customParameters.put("stand-alone", "true");
		//launcherFrame.loginForm.mainLoginPanel.mediaPlayer1.start();
		if (args.length >= 3) {

			String ip = args[2];
			String port = "25565";
			if (ip.contains(":")) {
				String[] parts = ip.split(":");
				ip = parts[0];
				port = parts[1];
			}

			launcherFrame.customParameters.put("server", ip);
			launcherFrame.customParameters.put("port", port);
		}
		/*try {
			wp = new WavPlayer(new URL("http://62.210.215.38/VOSE/EpicLauncher.wav"));
			wp.open();
			wp.play();           
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		/*if (args.length >= 1) {
       LoginForm.userName.setText(args[0]);
       if (args.length >= 2) {
         LoginForm.password.setText(args[1]);
         launcherFrame.loginForm.doLogin();
       }
     }*/
	}
}