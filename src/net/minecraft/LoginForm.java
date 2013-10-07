package net.minecraft;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LoginForm extends TransparentPanel
{
  private static final long serialVersionUID = 1L;
  private static final Color LINK_COLOR = new Color(8421631);
  
  public static String ServerAddr = "62.210.215.38";
  public static String ServerPort = "25565";

  //private TransparentCheckbox stopMusic = new TransparentCheckbox("Arreter la musique");
  private TransparentButton retryButton = new TransparentButton("Ré-essayer");
  private TransparentLabel errorLabel = new TransparentLabel("", 0);
  private JScrollPane updateNews;
  private boolean updating = false;
  private LauncherFrame launcherFrame;
  private boolean outdated = false;
  public MainLoginPanel mainLoginPanel;
  private JButton showhide = new JButton();
  private final ArrayList<Integer> pressed = new ArrayList<Integer>();
  
  public LoginForm(final LauncherFrame launcherFrame)
  {
    this.launcherFrame = launcherFrame;
    
    BorderLayout gbl = new BorderLayout();
    showhide.setIcon(new ImageIcon(Launcher.class.getResource("hide.png")));
    setLayout(gbl);

    add(buildMainLoginPanel(), "Center");

    readUsername();
    
    this.launcherFrame.addKeyListener(new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public synchronized void keyReleased(KeyEvent e) {
			if (pressed.size() > 0)
			{
				int index = pressed.indexOf(e.getKeyCode());
				if (index != -1)
					pressed.remove(index);				
			}
	    }
		
		@Override
		public synchronized void keyPressed(KeyEvent e) {
			/* Conbinaisons */
			pressed.add(e.getKeyCode());
			if (pressed.size() > 1) {
				// Now this is checked in the admin menu
				// CTRL + ALT + F = free database
				/*if (pressed.contains(KeyEvent.VK_CONTROL) && pressed.contains(KeyEvent.VK_ALT) && pressed.contains(KeyEvent.VK_F)) {
					MySQL.freeUserList();
				}*/
				// CTRL + A = show adm interface
				if (pressed.contains(KeyEvent.VK_CONTROL) && pressed.contains(KeyEvent.VK_A)) {
					mainLoginPanel.showOrHideFooter();
				}
			// Single Button Pressed
			}
		}
	});
    
    this.launcherFrame.setFocusable(true);
    showhide.addMouseListener(new MouseListener() {
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			
			if (updateNews.isVisible()) { 
				updateNews.setVisible(false);
				showhide.setIcon(new ImageIcon(Launcher.class.getResource("show.png")));
			}else {
				updateNews.setVisible(true);
				showhide.setIcon(new ImageIcon(Launcher.class.getResource("hide.png")));
			}
	        LoginForm.this.validate();
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
    
    this.retryButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        LoginForm.this.errorLabel.setText("");
        LoginForm.this.removeAll();
        LoginForm.this.add(LoginForm.this.buildMainLoginPanel(), "Center");
        LoginForm.this.validate();
      }
    });
  }
  
  //pour se logger
  public void doLogin(final String username) {
    setLoggingIn();
    loginPanel.logging = true;
    new Thread() {
      public void run() {
        try {
          LoginForm.this.launcherFrame.login(username, "");
        } catch (Exception e) {
          LoginForm.this.setError(e.toString());
        }
      }
    }
    .start();
  }

  private void readUsername() {
    try {
      File lastLogin = new File(Util.getWorkingDirectory(), "lastlogin");
 
      Cipher cipher = getCipher(2, "passwordfile");
      DataInputStream dis;
      if (cipher != null)
        dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastLogin), cipher));
      else {
        dis = new DataInputStream(new FileInputStream(lastLogin));
      }
      dis.close();
    } catch (Exception e) {
      System.out.println("lastlogin file not created yet.");
    }
  }
 
  private void writeUsername() {
    try {
      File lastLogin = new File(Util.getWorkingDirectory(), "lastlogin");
 
      Cipher cipher = getCipher(1, "passwordfile");
      DataOutputStream dos;
      if (cipher != null)
        dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), cipher));
      else {
        dos = new DataOutputStream(new FileOutputStream(lastLogin));
      }
      dos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Cipher getCipher(int mode, String password) throws Exception {
    Random random = new Random(43287234L);
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

    SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
    Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
    cipher.init(mode, pbeKey, pbeParamSpec);
    return cipher;
  }
  
 private JPanel buildMainLoginPanel()
{
	JPanel mainPanel = new BackgroundPanel(new BorderLayout());
	mainLoginPanel = new MainLoginPanel(this.launcherFrame);
	mainLoginPanel.setInsets(0, 0, 10, 0);
	mainPanel.add(mainLoginPanel, "Center");
    return mainPanel;
}
  
  private TransparentLabel getUpdateLink() {
    TransparentLabel accountLink = new TransparentLabel("Vous devez faire la mise à jour du Launcher !") {
      private static final long serialVersionUID = 0L;

      public void paint(Graphics g) { super.paint(g);

        int x = 0;
        int y = 0;

        FontMetrics fm = g.getFontMetrics();
        int width = fm.stringWidth(getText());
        int height = fm.getHeight();

        if (getAlignmentX() == 2.0F) x = 0;
        else if (getAlignmentX() == 0.0F) x = getBounds().width / 2 - width / 2;
        else if (getAlignmentX() == 4.0F) x = getBounds().width - width;
        y = getBounds().height / 2 + height / 2 - 1;

        g.drawLine(x + 2, y, x + width - 2, y); }

      public void update(Graphics g)
      {
        paint(g);
      }
    };
    accountLink.setCursor(Cursor.getPredefinedCursor(12));
    accountLink.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent arg0) {
        try {
          Util.openLink(new URL("http://www.minecraft.net/download.jsp").toURI());
      //    Util.openLink(new URL("http://62.210.215.38/index.html").toURI());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    accountLink.setForeground(LINK_COLOR);
    return accountLink;
  }

  private JPanel buildMainOfflinePanel() {
		JPanel p = new BackgroundPanel(new BorderLayout());
	    JPanel northPanel = new TransparentPanel();
	    JPanel southPanel = new TransparentPanel(new BorderLayout());
	    JPanel centerPanel = new TransparentPanel(new BorderLayout());
	    
	    northPanel.add(new LogoPanel(), "Center");
	    //southPanel.add(center(buildLoginPanel()), "Center");
	    
	    this.errorLabel.setFont(new Font(null, 2, 16));
	    this.errorLabel.setForeground(Color.RED);
	    this.errorLabel.setText("Vous ne pouvez pas vous connecter maintenant");
	    centerPanel.add(this.errorLabel, "South");
	    
	    southPanel.setPreferredSize(new Dimension(450, 100));
	    
	    p.add(northPanel, "North");
	    p.add(southPanel, "South");
	    p.add(centerPanel, "Center");
	    
	    return p;
  }

  private Component center(Component c) {
    TransparentPanel tp = new TransparentPanel(new GridBagLayout());
    tp.add(c);
    return tp;
  }

  @SuppressWarnings("unused")
private TransparentPanel buildOfflinePanel()
  {
    TransparentPanel panel = new TransparentPanel();
    panel.setInsets(0, 0, 0, 20);

    BorderLayout layout = new BorderLayout();
    panel.setLayout(layout);

    TransparentPanel loginPanel = new TransparentPanel(new BorderLayout());

    GridLayout gl = new GridLayout(0, 1);
    gl.setVgap(2);
    TransparentPanel pp = new TransparentPanel(gl);
    pp.setInsets(0, 8, 0, 0);

    pp.add(this.retryButton);

    loginPanel.add(pp, "East");

    panel.add(loginPanel, "Center");

    TransparentPanel p2 = new TransparentPanel(new GridLayout(0, 1));
    this.errorLabel.setFont(new Font(null, 2, 16));
    this.errorLabel.setForeground(new Color(16728128));
    p2.add(this.errorLabel);
    if (this.outdated) {
      TransparentLabel accountLink = getUpdateLink();
      p2.add(accountLink);
    }

    loginPanel.add(p2, "Center");

    return panel;
  }

  public void setError(String errorMessage) {
    removeAll();
    add(buildMainLoginPanel(), "Center");
    this.errorLabel.setText(errorMessage);
    validate();
  }

  public void loginOk() {
    writeUsername();
  }

  public void setLoggingIn() {  
	removeAll();
    JPanel panel = new JPanel(new BorderLayout());
    panel = new BackgroundPanel();
    JPanel southPanel = new TransparentPanel();
    JPanel northPanel = new TransparentPanel();
    southPanel.setLayout(new BorderLayout());
    northPanel.add(new LogoPanel(), "North");
    JLabel label = new TransparentLabel("Connexion en cours...      ", 0);
    label.setFont(new Font(null, 1, 16));
    label.setForeground(Color.BLACK);
    southPanel.add(center(label), "Center");
    southPanel.setPreferredSize(new Dimension(450, 100));

    panel.add(northPanel, "North");
    panel.add(southPanel, "South");

    add(panel, "Center");
    validate();
  }
  


  public void setNoNetwork() {
    removeAll();
    add(buildMainOfflinePanel(), "Center");
    validate();
  }

  public void setOutdated()
  {
    this.outdated = true;
  }

public boolean isUpdating() {
	return updating;
}

public void setUpdating(boolean updating) {
	this.updating = updating;
}
}