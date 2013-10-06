package net.minecraft;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel
{
 private static final long serialVersionUID = 1L;
 private Image bgImage;
 private String imgPath = Util.URL_DL + "VOSE/bg.png";

 public BackgroundPanel()
 {
  setOpaque(true);
  try
  {
	  this.bgImage = ImageIO.read(new URL(imgPath));
  }
  catch (IOException e)
  {
   e.printStackTrace();
  }
  
 }

 public BackgroundPanel(LayoutManager layout)
 {
	 setOpaque(true);
	  try
	  {
		  this.bgImage = ImageIO.read(new URL(imgPath));
	  }
	  catch (IOException e)
	  {
	   e.printStackTrace();
	  }
   setLayout(layout);
 }
 
 public void update(Graphics g)
 {
  paint(g);
 }

 @Override
 public void paintComponent(Graphics g) {
     g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
 }
 
 /*public void paintComponent(Graphics g2)
 {
      this.img = createImage(this.getWidth(), this.getHeight());
      Graphics g = this.img.getGraphics();
      g.drawImage(this.bgImage, 0, 0, null);
      
     g2.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), null);
 }*/
} 