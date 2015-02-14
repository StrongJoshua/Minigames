package game;

public class Tile
{
	public final static int size = 32;
	private int x, y;
	private boolean lit;
	
	public Tile(int x, int y)
	{
		this.x = x;
		this.y = y;
		lit = false;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setLit()
	{
		lit = true;
	}
	
	public boolean getLit()
	{
		return lit;
	}
	
	public String getImage()
	{
		if(lit)
			return "VampTileLit.png";
		else
			return "VampTile.png";
	}
}
