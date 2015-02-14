package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class Painter
{
	private MalariaCanvas mc;
	private String main;
	private String [] credits;
	
	public Painter(MalariaCanvas mc)
	{
		this.mc = mc;
		credits = new String [4];
		main = "MalariaMain.png";
		credits[0] = "Main Menu Image: http://www.hdwallpapersinn.com";
		credits[1] = "Vampire Image: http://www.how-to-draw-cartoons-online.com";
		credits[2] = "Mosquito Image: http://www.mystockvectors.com";
		credits[3] = "Fonts: Microsoft Word";
	}
	
	public void paint(Graphics g)
	{
		g.setFont(new Font("Chiller", Font.PLAIN, 144));
		g.setColor(Color.ORANGE);
		Input in = mc.input;
		int toShow = mc.logic.getMenuNumber();
		if(toShow == 0)
		{
			//main menu
			g.drawImage(in.getImage(main), 0, 0, mc.getWidth(), mc.getHeight(), null);
			String title = "Malaria Scare";
			g.drawString(title, getCenterX(title, g), 140);
			g.setFont(new Font("Chiller", Font.PLAIN, 72));
			g.setColor(Color.WHITE);
			String start = "Press Space to Play";
			g.drawString(start, getCenterX(start, g), 450);
		}
		else if(toShow == 1)
		{
			//show death menu
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, mc.getWidth(), mc.getHeight());
			g.setFont(new Font("Chiller", Font.PLAIN, 144));
			g.setColor(new Color(0x940000));
			String death = "You DIED!!!";
			g.drawString(death, getCenterX(death, g), getStringHeight(death, g));
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 24));
			String main = "Press Space to return to the Main Menu";
			g.drawString(main, getCenterX(main, g), 220);
			//write credits
			g.setColor(Color.WHITE);
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 36));
			String creds = "Credits:";
			g.drawString(creds, getCenterX(creds, g), 500 - getStringHeight(creds, g) - 25);
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 24));
			for(int i = 0; i < credits.length; i++)
			{
				g.drawString(credits[i], getCenterX(credits[i], g), 500 + (getStringHeight(credits[i], g) * i));
			}
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 30));
			g.setColor(new Color(0x0c00a6));
			String me = "Everything Else: Jan Risse";
			g.drawString(me, getCenterX(me, g), mc.getHeight() - getStringHeight(me, g));
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 42));
			g.setColor(new Color(0x4e006b));
			String score = "Score: " + mc.logic.getScore();
			g.drawString(score, getCenterX(score, g), 310);
		}
		else
		{
			//show game
			Tile [] map = mc.logic.getMap();
			for(int i = 0; i < map.length; i++)
			{
				Tile tile = map[i];
				g.drawImage(in.getImage(tile.getImage()), tile.getX(), tile.getY(), 64, 64, null);
			}
			//draw the vampire
			g.drawImage(in.getImage(mc.logic.getVampImage()), mc.logic.getVampX(), mc.logic.getVampY(), mc.tileSize, mc.tileSize, null);
			//draw the mosquitos
			int numMosq = mc.logic.getNumMosquitos();
			Rectangle [] mosqs = mc.logic.getMosquitos();
			for(int i = 0; i < numMosq; i++)
			{
				int x = mosqs[i].x;
				int y = mosqs[i].y;
				g.drawImage(in.getImage(mc.logic.getMosquitoImage()), x, y, mc.tileSize, mc.tileSize, null);
			}
			//draw score
			g.setFont(new Font("AR CHRISTY", Font.PLAIN, 24));
			g.setColor(Color.RED);
			int s = mc.logic.getScore();
			String score = "Score: " + s;
			g.drawString(score, mc.getWidth() - 150 - (20 * (s / 10000)), getStringHeight(score, g) + 10);
		}
	}
	
	public int getStringWidth(String s, Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		Rectangle2D bounds = metrics.getStringBounds(s, g);
		return (int)bounds.getWidth();
	}
	
	public int getStringHeight(String s, Graphics g)
	{
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		Rectangle2D bounds = metrics.getStringBounds(s, g);
		return (int)bounds.getHeight();
	}
	
	private int getCenterX(String s, Graphics g)
	{
		return (int) ((mc.getWidth()/2) - (getStringWidth(s, g)/2));
	}
}
