package net.minecraft;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private Image bgImage;

	public LogoPanel()
	{
		try
		{
			BufferedImage src = ImageIO.read(LoginForm.class.getResource("VOSE-Epitech.png"));
			int w = src.getWidth();
			int h = src.getHeight();
			this.bgImage = src.getScaledInstance(2 * (w / 5), h / 3, 16);
			setPreferredSize(new Dimension(w / 3, h / 3));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paintComponent(Graphics g2) {
		g2.drawImage(this.bgImage, 0, 0, null);
	}
}