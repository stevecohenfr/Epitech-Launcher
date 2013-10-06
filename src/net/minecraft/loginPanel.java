package net.minecraft;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class loginPanel extends JPanel {
	private static final long serialVersionUID = 8377469770849998949L;
	private TransparentLabel errorLabel = new TransparentLabel("", 0);
	private TransparentTextPane lblWait = new TransparentTextPane();
	private TransparentButton guestButton = new TransparentButton("Visiter EPITECH");
	private JTextField userId = new JTextField();
	public static boolean logging = false;
	//private boolean outdated = false;
	public static String ServerAddr = "62.210.215.38";
	public static String ServerPort = "25565";
	private JButton refresh = new JButton(" Rafraîchir");
	private LauncherFrame launcherFrame;
	public static boolean forceRedefinePassword = false;
	private String rank;
	private boolean admMode = false;
	private String admLogin;
	private String admResult;
	
	public loginPanel(final LauncherFrame launcherFrame, boolean admMode, String admResult) {
		this.admResult = admResult;
		this.admMode = admMode;
		this.launcherFrame = launcherFrame;
		setOpaque(false);
		setLayout(new GridLayout(10, 1));
		
		add(new TransparentPanel());
		
		JPanel timepanel = new JPanel();
		BoxLayout box = new BoxLayout(timepanel, BoxLayout.X_AXIS);
		timepanel.setLayout(box);
		timepanel.setOpaque(false);
		timepanel.setBackground(new Color(0, 0, 0, 0));
		JTextPane horloge = new JTextPane();
		Date time = new Date();
        DateFormat dd = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat dh = DateFormat.getTimeInstance(DateFormat.SHORT);
		horloge.setText(dd.format(time) + "\n" + dh.format(time));
		horloge.setFont(new Font("Courier", Font.BOLD, 13));
		horloge.setOpaque(false);
		horloge.setBackground(new Color(0, 0, 0, 0));
		horloge.setEditable(false);
		JLabel icon = new JLabel("");
		icon.setIcon(new ImageIcon(Launcher.class.getResource("heure.png")));
		icon.setPreferredSize(new Dimension(32, 32));
		timepanel.add(icon);
		timepanel.add(horloge);
		add(timepanel);
		
		lblWait.setFont(new Font("Rockwell Condensed", Font.PLAIN, 15));
		lblWait.setForeground(Color.GREEN);
		lblWait.setText("...");
		lblWait.setEditable(false);
		lblWait.setBackground(new Color(0, 0, 0, 0));
		add(lblWait);
		update_queue();
		
		/* Guest mode */
		if (admMode == false) {
			
			add(new TransparentPanel());

			final Font oldFont = userId.getFont();
			final Font autoFillFont = new Font(userId.getFont().getName(), Font.ITALIC, 12);
			userId.setForeground(Color.GRAY);
			userId.setText("Entrez votre ID");
			userId.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					if (userId.getText().equals("Entrez votre ID")) {
						userId.setForeground(Color.BLACK);
						userId.setFont(oldFont);
						userId.setText("");
					}
				}
				@Override
				public void focusLost(FocusEvent e) {
					if (userId.getText().isEmpty()) {
						userId.setForeground(Color.GRAY);
						userId.setFont(autoFillFont);
						userId.setText("Entrez votre ID");
					}
				}
			});
			add(userId);
			
		    guestButton.setFont(new Font("Rockwell Condensed", Font.BOLD, 14));
			guestButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String freeUser = LauncherFrame.getFreeUser();
					if (MySQL.checkUserId(getGuestID()))
					{
						if (MySQL.setUserCoords(userId.getText(), freeUser))
						{
							launcherFrame.loginForm.mainLoginPanel.mediaPlayer1.getComponent().release(true);
							launcherFrame.loginForm.mainLoginPanel.mediaPlayer2.getComponent().release(true);
							launcherFrame.customParameters.put("server", ServerAddr);
							launcherFrame.customParameters.put("port", ServerPort);
							launcherFrame.loginForm.doLogin(freeUser);
						}
					}
					else
						JOptionPane.showMessageDialog(null, "Veuillez entrer un ID valide !\nVous pouvez récupérer un ID sur Facebook ou dans votre mail","Erreur : ID invalide ou manquant", JOptionPane.ERROR_MESSAGE);
				}
			});
			add(guestButton);
			
			errorLabel.setForeground(Color.RED);
		    this.errorLabel.setText("");
		    add(this.errorLabel);

		    add(new TransparentPanel());
		    add(new TransparentPanel());
		/* Admin mode */
		}else {
			rank = MySQL.getUserRank(launcherFrame.loginForm.mainLoginPanel.getAdmLogin());
			forceRedefinePassword = admResult.equals("defpass");
			errorLabel.setForeground(Color.RED);
		    this.errorLabel.setText("");
		    add(this.errorLabel);
		    add(AdmLoginButton(launcherFrame));
		    if (rank.equals("DevTeam")){
		    	add(AdmFreeDatabaseButton());
		    	add(AdmChangeUserPassword());
		    }
		    add(AdmChangePasswordButton());
		    
		    if (!rank.equals("DevTeam")){
		    	add(new TransparentPanel());
		    	add(new TransparentPanel());
		    }
		    add(new TransparentPanel());
		}
		
	    refresh.setIcon(new ImageIcon(Launcher.class.getResource("refresh.png")));
	    refresh.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				refresh_waitList();
			}

			@Override
			public void mouseEntered(MouseEvent e){
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
	    refresh.setOpaque(false);
	    refresh.setContentAreaFilled(false);
	    refresh.setBorderPainted(false);
	    refresh.setHorizontalAlignment(SwingConstants.LEFT);
	    refresh.setFocusable(false);
	    add(refresh);
	}

	
	private JButton AdmChangeUserPassword() {
		final JButton changeUserPassButton = new JButton("Change user password");
		changeUserPassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeUserPassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog redef = new RedefineSpecificPassword(launcherFrame);
				redef.setVisible(true);
				redef.pack();
				//Code here
			}
		});
		return changeUserPassButton;
	}
	
	/**
	 * Create and return JButton to clear database with actionListener implemented.
	 * JButton is disabled after click and success.
	 * @return JButton
	 */
	private JButton AdmFreeDatabaseButton() {
		final JButton freeButton = new JButton("Clear database");
		freeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		freeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean success = MySQL.freeUserList();
				if (success) {
					freeButton.setForeground(new Color(0, 102, 0));
					freeButton.setText("Success");
					freeButton.setEnabled(false);
				}
			}
		});
		return freeButton;
	}
	
	/**
	 * Create and return JButton to change password with actionListener implemented.
	 * Open ChangePassword popup at click.
	 * @return JButton
	 */
	private JButton AdmChangePasswordButton() {
		JButton passwordButton = new JButton("Change password");
		passwordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog redefinePassword = new RedefinePassword(admLogin, launcherFrame);
				redefinePassword.setVisible(true);
				redefinePassword.pack();
			}
		});
		return passwordButton;
	}
	
	/**
	 * Create and return JButton for adm login with actionListener implemented.
	 * Log player with his adm-login or ask to change password if using default password.
	 * @param launcherFrame
	 * @param login: adm login
	 * @return JButton
	 */
	private JButton AdmLoginButton(final LauncherFrame launcherFrame) {
		JButton loginButton = new JButton("Se connecter");
		loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginButton.setFont(new Font("Rockwell Condensed", Font.BOLD, 14));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (forceRedefinePassword == false) {
					launcherFrame.loginForm.mainLoginPanel.mediaPlayer1.getComponent().release(true);
					launcherFrame.loginForm.mainLoginPanel.mediaPlayer2.getComponent().release(true);
					launcherFrame.customParameters.put("server", loginPanel.ServerAddr);
					launcherFrame.customParameters.put("port", loginPanel.ServerPort);
					launcherFrame.loginForm.doLogin(admLogin);
				}else {
					JOptionPane.showMessageDialog(null, "Vous utilisez le mot de passe par default.\nVous devez le redéfinir avant de lancer le jeu.", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		return loginButton;
	}
	
	public void refresh_waitList() {
		refresh.setIcon(new ImageIcon(Launcher.class.getResource("loading.gif")));
		this.validate();
		new Thread() {
			public void run() {
				Map<String, Integer> listUsers = MySQL.getUserList();
				if (listUsers == null)
					return ;
				int size = listUsers.size();
				if (!listUsers.containsValue(0)) {
					lblWait.setForeground(Color.RED);
					lblWait.setText("Plus de place.");
					guestButton.setEnabled(false);
				}else {
					int free = 0;
					Set<String> keys = listUsers.keySet();
					Iterator<String> it = keys.iterator();
					while (it.hasNext()){
						String key = it.next();
						int value = listUsers.get(key);
						if (value != 2)
							free++;
					}
					lblWait.setForeground(Color.GREEN);
					if (lblWait.getText().equals("..."))
						lblWait.setText("");
					if (free < size) {
						lblWait.setText("Dejà " + (size - free) + " personnes connectées\n");
						lblWait.concat("Il reste " + free + " places.\n");
					}
					else
						lblWait.setText("Il reste " + free + " places.\n");
				}
				refresh.setIcon(new ImageIcon(Launcher.class.getResource("refresh.png")));
				loginPanel.this.validate();
			}
		}.start();
	}

	public void update_queue() {
		new Thread() {
			public void run() {
				while (!logging) {
					try {
						refresh_waitList();
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public boolean isAdmMode() {
		return admMode;
	}

	public void setAdmMode(boolean admMode) {
		this.admMode = admMode;
	}

	public String getAdmLogin() {
		return admLogin;
	}

	public void setAdmLogin(String admLogin) {
		this.admLogin = admLogin;
	}

	public String getAdmResult() {
		return admResult;
	}

	public void setAdmResult(String admResult) {
		this.admResult = admResult;
	}

	public String getGuestID() {
		if (userId == null || userId.getText().equals("Entrez votre ID"))
			return "";
		return userId.getText();
	}
}
