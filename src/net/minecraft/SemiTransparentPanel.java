 package net.minecraft;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;
 
 public class SemiTransparentPanel extends JPanel
 {
   private static final long serialVersionUID = 1L;
   private Insets insets;
   private Color color;
 
   public SemiTransparentPanel()
   {
   }
 
   public SemiTransparentPanel(LayoutManager layout)
   {
     setLayout(layout);
   }
 
   public boolean isOpaque() {
     return false;
   }
 
   public void paintComponent(Graphics g)
   {
       super.paintComponent(g);
       Color ppColor = this.color;
       g.setColor(ppColor);
       g.fillRect(0,0,this.getSize().width,this.getSize().height); //x,y,width,height
   }    
   
   public void setInsets(int a, int b, int c, int d) {
     this.insets = new Insets(a, b, c, d);
   }
 
   public Insets getInsets() {
     if (this.insets == null) return super.getInsets();
     return this.insets;
   }
   
   public void setBackground(Color c) {
	   this.color = c;
   }
 }