package eOSB.binder.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import eOSB.game.data.IconFactory;

public class SplashPanel extends JPanel {
	private BufferedImage splash;
	
	public SplashPanel() {
		try 
		{
			splash = ImageIO.read(ClassLoader.getSystemResource(IconFactory.SPLASH));
			this.setPreferredSize(new Dimension(this.splash.getWidth(), this.splash.getHeight()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.splash, 0, 0, null);
	}
}
