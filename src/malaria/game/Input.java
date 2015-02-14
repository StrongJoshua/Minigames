package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Input implements KeyListener
{
	private boolean [] keys;
	private MalariaCanvas mc;
	private HashMap <String, BufferedImage> imageMap;
	private int toLoad, loading;
	
	public Input(MalariaCanvas mc)
	{
		this.mc = mc;
		keys = new boolean [5];
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		//nothing here
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[0] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[1] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[2] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			keys[3] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[4] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			mc.stop();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[0] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[1] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[2] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			keys[3] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[4] = false;
		}
	}
	
	public boolean [] getKeysPressed()
	{
		return keys;
	}
	
	public void bufferImages()
	{
		String checkJar = this.getClass().getResource("Input.class").toString();
		imageMap = new HashMap<String, BufferedImage>();
		String imagebase = "images";
		toLoad = 0;
		loading = 0;
		System.out.println(checkJar);
		if(!checkJar.startsWith("rsrc"))
		{
			URL u = ClassLoader.getSystemResource(imagebase);
			File folder = new File(u.getFile());
			String [] imageFiles = folder.list();
			toLoad = imageFiles.length;
			try
			{
				for(; loading < toLoad; loading++)
				{
					imageMap.put(imageFiles[(int)loading], ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(imagebase + "/" + imageFiles[(int)loading])));
				}
				System.out.println("Finished Loading Images");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Image Loading Error");
				System.exit(0);
			}
		}
		else
		{
			System.out.println("In a Jar");
			Scanner fileIn = new Scanner(Input.class.getResourceAsStream("/resources.txt"));
			ArrayList<String> paths = new ArrayList<String>();
			while(fileIn.hasNextLine())
			{
				String s = fileIn.nextLine();
				if(s.endsWith("png"))
					paths.add(s);
			}
			fileIn.close();
			toLoad = paths.size();
			try
			{
				for(; loading < toLoad; loading++)
				{
					System.out.println("Loaded: " + paths.get((int) loading));
					imageMap.put(paths.get((int)loading), ImageIO.read(this.getClass().getResourceAsStream("/" + imagebase + "/" + paths.get((int)loading))));
				}
				System.out.println("Finished Loading Images");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("... Image Loading Error");
				System.exit(0);
			}
		}
	}
	
	public BufferedImage getImage(String s)
	{
		return imageMap.get(s);
	}
}
