 package net.minecraft;
 
 import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

 
 	public class Launcher extends Applet
 	implements Runnable, AppletStub, MouseListener
 	{
 		private static final long serialVersionUID = 1L;
 		public Map<String, String> customParameters = new HashMap<String, String>();
 		public GameUpdater gameUpdater;
 		private boolean gameUpdaterStarted = false;
 		private Applet applet;
 		private boolean active = false;
 		private int context = 0;
 		private boolean hasMouseListener = false;
 		private VolatileImage img;
 		private BufferedImage update_img;
        private Image bgImage;
 		
   public Launcher() {
			try {
				update_img = ImageIO.read(Launcher.class.getResource("update.png"));
				/*int rows = 10;
		        int chunks = rows;  
		  
		        chunkWidth = update_img.getWidth();
		        chunkHeight = update_img.getHeight() / rows;  
		        update_imgs = new BufferedImage[chunks];
		        for (int x = 0; x < rows; x++) {  
		        	update_imgs[x] = new BufferedImage(chunkWidth, chunkHeight, update_img.getType());  
		  
		        	Graphics2D gr = update_imgs[x].createGraphics();  
		        	gr.drawImage(update_img, 0, 0, chunkWidth, chunkHeight, 0, chunkHeight * x, chunkWidth, chunkHeight * x + chunkHeight, null);  
		        	gr.dispose();
		        }*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

public boolean isActive()
   {
     if (this.context == 0) {
       this.context = -1;
       try {
         if (getAppletContext() != null) this.context = 1; 
       }
       catch (Exception localException) {
       }
     }
     if (this.context == -1) return this.active;
     return super.isActive();
   }
 
   public void init(String userName, String latestVersion, String downloadTicket, String sessionId)
   {
     try {
    	 this.setBgImage(ImageIO.read(new URL("http://62.210.215.38/VOSE/bg.png")));
     } catch (IOException e) {
       e.printStackTrace();
     }
 
     this.customParameters.put("username", userName);
     this.customParameters.put("sessionid", sessionId);
 
     this.gameUpdater = new GameUpdater(latestVersion, "minecraft.jar?user=" + userName + "&ticket=" + downloadTicket, this);
   }
 
   public boolean canPlayOffline() {
     return this.gameUpdater.canPlayOffline();
   }
 
   public void init() {
     if (this.applet != null) {
       this.applet.init();
       return;
     }
     init(getParameter("userName"), getParameter("latestVersion"), getParameter("downloadTicket"), getParameter("sessionId"));
   }
 
   public void start() {
     if (this.applet != null) {
       this.applet.start();
       return;
     }
     if (this.gameUpdaterStarted) return;
 
     Thread t = new Thread() {
       public void run() {
         Launcher.this.gameUpdater.run();
         try {
           if (!Launcher.this.gameUpdater.fatalError)
             Launcher.this.replace(Launcher.this.gameUpdater.createApplet());
         }
         catch (ClassNotFoundException e)
         {
           e.printStackTrace();
         } catch (InstantiationException e) {
           e.printStackTrace();
         } catch (IllegalAccessException e) {
           e.printStackTrace();
         }
       }
     };
     t.setDaemon(true);
     t.start();
 
     t = new Thread() {
       public void run() {
         while (Launcher.this.applet == null) {
           Launcher.this.repaint();
           try {
             Thread.sleep(10L);
           } catch (InterruptedException e) {
             e.printStackTrace();
           }
         }
       }
     };
     t.setDaemon(true);
     t.start();
 
     this.gameUpdaterStarted = true;
   }
   
   public void stop() {
     if (this.applet != null) {
       this.active = false;
       this.applet.stop();
       return;
     }
   }
 
   public void destroy() {
     if (this.applet != null) {
       this.applet.destroy();
       return;
     }
   }
 
   public void replace(Applet applet) {
     this.applet = applet;
     applet.setStub(this);
     applet.setSize(getWidth(), getHeight());
 
     setLayout(new BorderLayout());
     add(applet, "Center");
 
     applet.init();
     this.active = true;
     applet.start();
     System.out.println("Applet started!");
     validate();
   }
   
   public void update(Graphics g)
   {
     paint(g);
   }
   
   public static final void filterImage(BufferedImage img, Color c)
   {              
	   float cHSB[] = new float[3];
	   boolean gray = false;
	   
	   Color.RGBtoHSB(c.getRed(),c.getBlue(),c.getGreen(),cHSB);
	   if(cHSB[1] == 0)
	   {
		   gray = true; System.out.println("Gris");
	   }
	   WritableRaster trame = img.getRaster();
	   ColorModel model = img.getColorModel();
	   float[] hsb = new float[3];
	   Object data;
	   int w = img.getWidth();
	   int h = img.getHeight();
	   for (int x=0; x<w; x++)
		   for (int y=0; y<h; y++)
		   {
			   data = trame.getDataElements(x, y, null);
			   Color.RGBtoHSB(model.getRed(data), model.getGreen(data), model.getBlue(data), hsb);
			   if (gray) 
				   trame.setDataElements(x, y, model.getDataElements(Color.HSBtoRGB(1-cHSB[0], 0,hsb[2]), null));
			   else
				   trame.setDataElements(x, y, model.getDataElements(Color.HSBtoRGB(1-cHSB[0], hsb[1], hsb[2]), null));
		   }              
   }
 
   public void paint(Graphics g2) {
     if (this.applet != null) return;
 
     int w = getWidth() / 2;
     int h = getHeight() / 2;
     if ((this.img == null) || (this.img.getWidth() != w) || (this.img.getHeight() != h)) {
       this.img = createVolatileImage(w, h);
     }
 
     Graphics g = this.img.getGraphics();

     Graphics2D g2d = (Graphics2D)g;
     Color c1 = new Color(0x0D, 0x4F, 0x8B);
     Color c2 = new Color(0x74, 0xBB, 0xFB);
     Paint gradientPaint = new GradientPaint(0, 0, c1,
             0, getHeight(), c2);
     g2d.setPaint(gradientPaint);
     g2d.fillRect(0, 0, getWidth(), getHeight());
     
     
     if (this.gameUpdater.pauseAskUpdate) {
       if (!this.hasMouseListener) {
         this.hasMouseListener = true;
         addMouseListener(this);
       }
       g.setColor(Color.LIGHT_GRAY);
       String msg = "Une nouvelle mise � jour est disponible.";
       g.setFont(new Font(null, 1, 20));
       FontMetrics fm = g.getFontMetrics();
       g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - fm.getHeight() * 2);
 
       g.setFont(new Font(null, 0, 12));
       fm = g.getFontMetrics();
 
       g.fill3DRect(w / 2 - 56 - 8, h / 2, 56, 20, true);
       g.fill3DRect(w / 2 + 8, h / 2, 56, 20, true);
 
       msg = "Voulez-vous mettre � jour?";
       g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - 8);
 
       g.setColor(Color.BLACK);
       msg = "Oui";
       g.drawString(msg, w / 2 - 56 - 8 - fm.stringWidth(msg) / 2 + 28, h / 2 + 14);
       msg = "Non";
       g.drawString(msg, w / 2 + 8 - fm.stringWidth(msg) / 2 + 28, h / 2 + 14);
     }
     else
     {
       g.setColor(Color.LIGHT_GRAY);
 
       String msg = "Mise à jour de VOSE";
       if (this.gameUpdater.fatalError) {
         msg = "Echec de la Mise à Jour";
       }
 
       g.setFont(new Font(null, 1, 20));
       FontMetrics fm = g.getFontMetrics();
       //g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 - fm.getHeight() * 2);
 
       g.setFont(new Font(null, 0, 12));
       fm = g.getFontMetrics();
       msg = this.gameUpdater.getDescriptionForState();
       if (this.gameUpdater.fatalError) {
    	   msg = this.gameUpdater.fatalErrorDescription;
       }
 
       g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 + fm.getHeight() * 1);
       msg = this.gameUpdater.subtaskMessage;
       g.drawString(msg, w / 2 - fm.stringWidth(msg) / 2, h / 2 + fm.getHeight() * 2);
 
       if (!this.gameUpdater.fatalError) {
    	   
    	   BufferedImage current = update_img.getSubimage(0, 0, (int) (((this.gameUpdater.percentage / 100.0f) * update_img.getWidth()) + 1), update_img.getHeight());
    	   g.drawImage(current,
    			   w / 8, h / 8,
    			   w / 8 + current.getWidth(), h / 8 + current.getHeight(),
    			   0, 0,
    			   current.getWidth(), current.getHeight(), null);
    	   /*int pos = this.gameUpdater.percentage / 10;
    	   if (pos >= 10)
    		   pos = 9;
    	   g.drawImage(this.update_imgs[pos],
    			   w/2 - this.update_imgs[pos].getWidth() / 4, h / 8,
    			   w/2 + this.update_imgs[pos].getWidth() / 4, h / 8 + this.update_imgs[pos].getHeight() / 2,
    			   0, 0,
    			   this.update_imgs[pos].getWidth(), this.update_imgs[pos].getHeight(), null);*/
       }
     }
     g.dispose();
     g2.drawImage(this.img, 0, 0, w * 2, h * 2, null);
   }
 
   public void run() {
   }
 
   public String getParameter(String name) {
     String custom = (String)this.customParameters.get(name);
     if (custom != null) return custom; try
     {
       return super.getParameter(name);
     } catch (Exception e) {
       this.customParameters.put(name, null);
     }return null;
   }
 
   public void appletResize(int width, int height)
   {
   }
 
   public URL getDocumentBase() {
     try {
       return new URL("http://www.minecraft.net/game/");
     } catch (MalformedURLException e) {
       e.printStackTrace();
     }
     return null;
   }
 
   public void mouseClicked(MouseEvent arg0) {
   }
 
   public void mouseEntered(MouseEvent arg0) {
   }
 
   public void mouseExited(MouseEvent arg0) {
   }
 
   public void mousePressed(MouseEvent me) {
     int x = me.getX() / 2;
     int y = me.getY() / 2;
     int w = getWidth() / 2;
     int h = getHeight() / 2;
 
     if (contains(x, y, w / 2 - 56 - 8, h / 2, 56, 20)) {
       removeMouseListener(this);
       this.gameUpdater.shouldUpdate = true;
       this.gameUpdater.pauseAskUpdate = false;
       this.hasMouseListener = false;
     }
     if (contains(x, y, w / 2 + 8, h / 2, 56, 20)) {
       removeMouseListener(this);
       this.gameUpdater.shouldUpdate = false;
       this.gameUpdater.pauseAskUpdate = false;
       this.hasMouseListener = false;
     }
   }
 
   private boolean contains(int x, int y, int xx, int yy, int w, int h) {
     return (x >= xx) && (y >= yy) && (x < xx + w) && (y < yy + h);
   }
 
   public void mouseReleased(MouseEvent arg0)
   {
   }

public Image getBgImage() {
	return bgImage;
}

public void setBgImage(Image bgImage) {
	this.bgImage = bgImage;
}
 }