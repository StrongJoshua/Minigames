package malaria.game;

import java.awt.Rectangle;
import java.util.Random;

public class Logic
{
	private MalariaCanvas mc;
	private int menuNum, numMosq;
	private Rectangle vamp;
	private Rectangle [] mosquitos;
	private int [][] mSpeed;
	private Tile [] map;
	private int pauseTime;
	private String vampImage, mosquitoImage;
	private Random rand;
	private Long startTime;
	private int score;
	private boolean collided;
	private final int maxMosqs = 30;
	
	public Logic(MalariaCanvas mc)
	{
		this.mc = mc;
		menuNum = 0;
		map = new Tile[(mc.getWidth() / mc.tileSize) * (mc.getHeight() / mc.tileSize)];
		pauseTime = 0;
		vampImage = "MalariaVamp.png";
		mosquitoImage = "MalariaMosquito.png";
		mosquitos = new Rectangle [maxMosqs];
		mSpeed = new int [maxMosqs][2];
		rand = new Random();
	}
	
	public void tick()
	{
		boolean [] keys = mc.input.getKeysPressed();
		if(menuNum == 0)
		{
			if(pauseTime > 0)
			{
				pauseTime--;
				return;
			}
			if(keys[0])
			{
				menuNum = 2;
				createMap();
				startTime = System.currentTimeMillis();
				return;
			}
		}
		else if(menuNum == 1)
		{
			if(keys[0])
			{
				menuNum = 0;
				pauseTime = 30;
			}
		}
		else
		{
			//score logic
			score = (int) (System.currentTimeMillis() - startTime) / 10;
			//vampire logic
			int speed = 3;
			if(isVampLit())
				speed = 2;
			if(keys[1])
				vamp.y -= speed;
			if(keys[2])
				vamp.x -= speed;
			if(keys[3])
				vamp.y += speed;
			if(keys[4])
				vamp.x += speed;
			if(vamp.x < 0)
				vamp.x = 0;
			if(vamp.y < 0)
				vamp.y = 0;
			if(vamp.x + vamp.width > mc.getWidth())
				vamp.x = mc.getWidth() - vamp.width;
			if(vamp.y + vamp.height > mc.getHeight())
				vamp.y = mc.getHeight() - vamp.height;
			//mosquito logic
			if(score % 500 == 0 && numMosq < maxMosqs - 1)
			{
				mosquitos[numMosq] = new Rectangle(rand.nextInt(mc.getWidth()), rand.nextInt(mc.getHeight()), mc.tileSize, mc.tileSize);
				numMosq++;
			}
			for(int i = 0; i < numMosq; i++)
			{
				int dx = mSpeed[i][0];
				int dy = mSpeed[i][1];
				if(dx == 0)
					dx = rand.nextInt(5) - 2;
				if(dy == 0)
					dy = rand.nextInt(5) - 2;
				mosquitos[i].x += dx;
				mosquitos[i].y += dy;
				if(mosquitos[i].x < 0)
				{
					mosquitos[i].x = 0;
					dx = 0;
				}
				if(mosquitos[i].x  + mosquitos[i].width > mc.getWidth())
				{
					mosquitos[i].x = mc.getWidth() - mosquitos[i].width;
					dx = 0;
				}
				if(mosquitos[i].y < 0)
				{
					mosquitos[i].y = 0;
					dy = 0;
				}
				if(mosquitos[i].y + mosquitos[i].height > mc.getHeight())
				{
					mosquitos[i].y = mc.getHeight() - mosquitos[i].height;
					dy = 0;
				}
				mSpeed[i][0] = dx;
				mSpeed[i][1] = dy;
			}
			//mosquito collision check
			for(int i = 0; i < numMosq; i++)
			{
				if(vamp.intersects(mosquitos[i]))
				{
					collided = true;
					break;
				}
			}
			if(collided)
			{
				menuNum = 1;
			}
		}
	}
	
	public int getMenuNumber()
	{
		return menuNum;
	}
	
	private void createMap()
	{
		// 1/3 of tiles are lit
		int bright = map.length / 3;
		for(int i = 0; i < map.length; i++)
		{
			map[i] = new Tile((i % (mc.getWidth()/mc.tileSize)) * mc.tileSize, (i / (mc.getWidth()/mc.tileSize)) * mc.tileSize);
		}
		
		boolean found;
		while(bright > 0)
		{
			found = false;
			while(!found)
			{
				Tile tile = map[rand.nextInt(map.length)];
				if(!tile.getLit())
				{
					tile.setLit();
					found = true;
				}
			}
			bright--;
		}
		for(int i = 0; i < mosquitos.length; i++)
		{
			mosquitos[i] = null;
		}
		mosquitos[0] = new Rectangle(mc.getWidth()/2, mc.getHeight()/2, mc.tileSize, mc.tileSize);
		numMosq = 1;
		score = 0;
		collided = false;
		vamp = new Rectangle(0, 0, mc.tileSize, mc.tileSize);
		for(int i = 0; i < mSpeed.length; i++)
		{
			for(int j = 0; j < mSpeed[i].length; j++)
			{
				mSpeed[i][j] = 0;
			}
		}
	}
	
	public Tile [] getMap()
	{
		return map;
	}
	
	public String getVampImage()
	{
		return vampImage;
	}
	
	public int getVampX()
	{
		return vamp.x;
	}
	
	public int getVampY()
	{
		return vamp.y;
	}
	
	private boolean isVampLit()
	{
		int cx = (int) vamp.getCenterX();
		int cy = (int) vamp.getCenterY();
		for(int i = 0; i < map.length; i++)
		{
			int x = map[i].getX();
			int y = map[i].getY();
			int w = mc.tileSize;
			
			if(cx > x && cx < x + w && cy > y && cy < y + w && map[i].getLit())
			{
				return true;
			}
		}
		return false;
	}
	
	public int getNumMosquitos()
	{
		return numMosq;
	}
	
	public Rectangle [] getMosquitos()
	{
		return mosquitos;
	}
	
	public String getMosquitoImage()
	{
		return mosquitoImage;
	}
	
	public int getScore()
	{
		return score;
	}
}