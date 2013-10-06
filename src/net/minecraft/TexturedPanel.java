package net.minecraft;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TexturedPanel extends JPanel
{
 private static final long serialVersionUID = 1L;
 private Image img;
 private Image bgImage;

 public TexturedPanel()
 {
  setOpaque(true);
  try
  {
   this.bgImage = ImageIO.read(LoginForm.class.getResource("dirt.png")).getScaledInstance(32, 32, 16);
  }
  catch (IOException e)
  {
   e.printStackTrace();
  }
 }

 public void update(Graphics g)
 {
  paint(g);
 }

 public void paintComponent(Graphics g2)
 {
      this.img = createImage(this.getWidth(), this.getHeight());
      Graphics g = this.img.getGraphics();
      g.drawImage(this.bgImage, 0, 0, null);
      
     g2.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), null);
 }
}