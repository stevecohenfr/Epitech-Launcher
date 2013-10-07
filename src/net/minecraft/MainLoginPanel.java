package net.minecraft;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.epitech.mediaplayer.MediaPlayer;


public class MainLoginPanel extends JPanel {
	private static final long serialVersionUID = -1503354383282926096L;
	private Insets insets;
	private JPanel Footer;
	private JTextField admUsernameField;
	private JPasswordField admPasswordField;
	private final JButton admLoginButton;
	private JPanel Buttons;
	private final String video1path = Util.URL_DL + "VOSE/video1.mp4";
	private final String video2path = Util.URL_DL + "VOSE/video2.mp4";
	public MediaPlayer mediaPlayer1;
	public MediaPlayer mediaPlayer2;
	public JPanel play1 = new TransparentPanel();
	public JPanel play2 = new TransparentPanel();

	public void showOrHideFooter() {
		if (Footer.isVisible())
			Footer.setVisible(false);
		else
			Footer.setVisible(true);
		this.validate();
	}
	
	

	@SuppressWarnings("unchecked")
	public MainLoginPanel(final LauncherFrame launcherFrame) {
		
		mediaPlayer1 = new MediaPlayer(video1path, launcherFrame, play1);
		mediaPlayer2 = new MediaPlayer(video2path, launcherFrame, play2);
		setLayout(new BorderLayout(10, 0));
		setOpaque(false);

		RoundedPanel News = new RoundedPanel();
		News.setLayout(new BorderLayout(0, 0));
		News.setPreferredSize(new Dimension(450, 430));
		News.setBackground(new Color(255, 255, 255, 75));
		News.add(getUpdateNews(2), "Center");
		add(News, BorderLayout.WEST);


		JPanel Videos = new TransparentPanel();
		Videos.setBorder(new EmptyBorder(0, 0, 0, 10));
		add(Videos, BorderLayout.CENTER);
		Videos.setLayout(new BoxLayout(Videos, BoxLayout.Y_AXIS));
		
		final JLabel Iconlbl = new TransparentLabel("");
		final ImageIcon playIcon = new ImageIcon(Launcher.class.getResource("play.png"));
		final JPanel Video1 = new RoundedPanel();
		Video1.setLayout(new BorderLayout());
		JLayeredPane lp = new JLayeredPane();
		Video1.setBorder(new EmptyBorder(3, 3, 3, 3));
		Video1.setPreferredSize(new Dimension(10, 78));
		Video1.setMinimumSize(new Dimension(10, 78));
		Video1.setBackground(new Color(0, 0, 0));
		lp.setOpaque(false);
		mediaPlayer1.getComponent().setSize(new Dimension(450, 250));
		Video1.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				play1.setSize(Video1.getSize());
				mediaPlayer1.getComponent().setSize(Video1.getSize());
				play1.setLocation((mediaPlayer1.getComponent().getWidth() / 2) - (play1.getWidth() / 2), (mediaPlayer1.getComponent().getHeight() / 2) - (2 * (play1.getHeight() / 3)));
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		Iconlbl.setIcon(playIcon);
		play1.add(Iconlbl, BorderLayout.CENTER);
		play1.setSize(new Dimension(450, 250));
		play1.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				play1.setVisible(false);
				mediaPlayer1.start();
			}
		});
		lp.add(play1, Integer.valueOf(2));
		lp.add(mediaPlayer1.getComponent(), Integer.valueOf(1));
		Video1.add(lp, BorderLayout.CENTER);
		Videos.add(Video1);
		
		Component rigidArea = Box.createRigidArea(new Dimension(0,5));
		Videos.add(rigidArea);
		
		final JLabel Iconlbl2 = new TransparentLabel("");
		final ImageIcon playIcon2 = new ImageIcon(Launcher.class.getResource("play.png"));
		final JPanel Video2 = new RoundedPanel();
		Video2.setLayout(new BorderLayout());
		JLayeredPane lp2 = new JLayeredPane();
		Video2.setBorder(new EmptyBorder(3, 3, 3, 3));
		Video2.setPreferredSize(new Dimension(10, 78));
		Video2.setMinimumSize(new Dimension(10, 78));
		Video2.setBackground(new Color(0, 0, 0));
		lp2.setOpaque(false);
		mediaPlayer2.getComponent().setSize(new Dimension(450, 250));
		Video2.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				mediaPlayer2.getComponent().setSize(Video2.getSize());
				play2.setSize(mediaPlayer2.getComponent().getSize());
				play2.setLocation((mediaPlayer2.getComponent().getWidth() / 2) - (play2.getWidth() / 2), (mediaPlayer2.getComponent().getHeight() / 2) - (2 * (play2.getHeight() / 3)));
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		Iconlbl2.setIcon(playIcon2);
		play2.add(Iconlbl2, BorderLayout.CENTER);
		play2.setSize(new Dimension(450, 250));
		play2.setOpaque(false);
		play2.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {
				play2.setVisible(false);
				mediaPlayer2.start();
			}
		});
		lp2.add(play2, Integer.valueOf(2));
		lp2.add(mediaPlayer2.getComponent(), Integer.valueOf(1));
		Video2.add(lp2, BorderLayout.CENTER);
		Videos.add(Video2);

		Buttons = new RoundedPanel();
		Buttons.setBackground(new Color(255, 255, 255, 75));
		Buttons.setPreferredSize(new Dimension(166, 10));
		add(Buttons, BorderLayout.EAST);
		Buttons.setLayout(new BorderLayout(0, 0));

		JPanel panel = new loginPanel(launcherFrame, false, null);
		Buttons.add(panel);

		JPanel Header = new TransparentPanel();
		Header.setBorder(new EmptyBorder(25, 0, 76, 300));
		Header.setPreferredSize(new Dimension(10, 150));
		add(Header, BorderLayout.NORTH);

		JPanel Title = new RoundedPanel();
		Title.setBackground(new Color(255, 255, 255, 75));
		Title.setPreferredSize(new Dimension(420, 70));
		Header.add(Title);
		Title.setLayout(null);

		JPanel logoPanel = new LogoPanel();
		logoPanel.setBounds(10, 11, 400, 48);
		Title.add(logoPanel);

		Footer = new RoundedPanel();
		Footer.setBackground(new Color(255, 255, 255, 75));
		Footer.setPreferredSize(new Dimension(10, 40));
		add(Footer, BorderLayout.SOUTH);
		Footer.setLayout(new CardLayout(0, 0));

		JPanel admLogin = new JPanel();
		admLogin.setPreferredSize(new Dimension(10, 20));
		Footer.add(admLogin, "admLogin");

		JLabel admLogin_username_Label = new JLabel("Login");
		admLogin.add(admLogin_username_Label);

		DocumentListener inTextFieldChange = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				admLoginButton.setEnabled(admUsernameField.getText().length() > 0 && new String(admPasswordField.getPassword()).length() > 0);
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				admLoginButton.setEnabled(admUsernameField.getText().length() > 0 && new String(admPasswordField.getPassword()).length() > 0);
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				admLoginButton.setEnabled(admUsernameField.getText().length() > 0 && new String(admPasswordField.getPassword()).length() > 0);
			}
		};
		
		admUsernameField = new JTextField();
		admLogin.add(admUsernameField);
		admUsernameField.setColumns(10);
		admUsernameField.getDocument().addDocumentListener(inTextFieldChange);
		admUsernameField.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		admUsernameField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					admPasswordField.requestFocusInWindow();
			}
		});
		
		JLabel admLogin_password_Label = new JLabel("Password");
		admLogin.add(admLogin_password_Label);
		
		admPasswordField = new JPasswordField();
		admPasswordField.setColumns(10);
		admLogin.add(admPasswordField);
		admPasswordField.getDocument().addDocumentListener(inTextFieldChange);
		admPasswordField.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		admPasswordField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					admLoginButton.requestFocus();
			}
		});

		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setPreferredSize(new Dimension(100, 0));
		admLogin.add(horizontalStrut);

		admLoginButton = new JButton("Administrer");
		admLoginButton.setEnabled(false);
		admLoginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { // adm login
				String result = MySQL.checkUserInDb(getAdmLogin(), getMd5AdminPass());
				if (result == "ok" || result == "defpass")
				{
					//backEnd = new Backend(getAdmLogin(), result, launcherFrame);
					//backEnd.setVisible(true);
					Buttons.removeAll();
					loginPanel admLoginPanel = new loginPanel(launcherFrame, true, result);
					admLoginPanel.setAdmLogin(getAdmLogin());
					admLoginPanel.setAdmResult(result);
					Buttons.add(admLoginPanel);
					validate();
				}
				else {
			        JOptionPane.showMessageDialog(null, "Login ou mot de passe incorrect !","Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		admLogin.add(admLoginButton);

		admLogin.setOpaque(false);
		Footer.setVisible(false);

	}

	public String getAdmLogin() {
		return (admUsernameField.getText());
	}
	
	public String getMd5AdminPass() {
		return (Util.md5(new String(admPasswordField.getPassword())));
	}
	
	private JScrollPane getUpdateNews(int nbr)
	{
		JTextPane editorPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(editorPane);
		try
		{
			editorPane.setContentType("text/html");
			editorPane.setText("nextNews");
			editorPane.setEditable(false);
			editorPane.setOpaque(false);
			editorPane.setBackground(new Color(50, 50, 50, 0));

			/*
			 * HTTP POST
			 */
			String reponse = HttpRequest.sendPost("http://jpo-virtuelle-epitech.eu/api/api.getNews.php", "limit", Integer.toString(nbr));
			parseJsonNews(editorPane, reponse, nbr);
			
			
			scrollPane.setBorder(null);
			scrollPane.setOpaque(false);
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
			scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
			scrollPane.setBackground(new Color(0, 0, 0, 0));
		} catch (Exception e2) {
			editorPane.setText("Impossible de charger le fil d'actualité...");
			e2.printStackTrace();
		}
		return scrollPane;
	}

	private void parseJsonNews(JTextPane editorPane, String jsonStr, int nbr)
	{

		JSONParser parser = new JSONParser();
	 
		try {
	 
			Object obj = parser.parse(jsonStr);
			JSONObject jsonObject = (JSONObject) obj;
	 
			/*
			 * Get all news
			 */
			for (int i = 0; i < nbr; i++) {
				String news = jsonObject.get(Integer.toString(i)).toString();
				Object eachNews = parser.parse(news);
				JSONObject newsObject = (JSONObject) eachNews;
				
				/*
				 * Parse each news
				 */
				String title = newsObject.get("title").toString();
				String iconPath = newsObject.get("icon").toString();
				String date = newsObject.get("date").toString();
				String content = newsObject.get("content").toString();
				String autor = newsObject.get("autor").toString();
				
				String formatedNews = Util.getNewsTemplate(date, title, iconPath, content, autor);
				editorPane.setText(editorPane.getText().replaceFirst("nextNews", formatedNews));
			}
			editorPane.setText(editorPane.getText().replaceFirst("nextNews", ""));
			
		} catch (ParseException e) {
			editorPane.setText("<font color=\"red\">Impossible de charger les news</font>");
			e.printStackTrace();
		}
	}

	public boolean isOpaque() {
		return false;
	}

	public void setInsets(int a, int b, int c, int d) {
		this.insets = new Insets(a, b, c, d);
	}

	public Insets getInsets() {
		if (this.insets == null) return super.getInsets();
		return this.insets;
	}
}
