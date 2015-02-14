package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MalariaCanvas extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static JPanel panel;
	private boolean isRunning;
	private Dimension def;
	public final int tileSize = 64;
	public Logic logic;
	private Painter paint;
	private Timer paintTimer;
	private BufferStrategy bs;
	public Input input;
	private Thread tickThread;
	
	public MalariaCanvas()
	{
		def = new Dimension(tileSize * 15, tileSize * 12);
		this.setSize(def);
		this.setFocusable(true);
		this.requestFocus();
		isRunning = false;
		logic = new Logic(this);
		paint = new Painter(this);
		paintTimer = new Timer(1000/100, new PaintTimerListener());
		input = new Input(this);
		this.addKeyListener(input);
		tickThread = new Thread(this);
	}

	public synchronized void start()
	{
		if(isRunning)
			return;
		isRunning = true;
		input.bufferImages();
		paintTimer.start();
		tickThread.start();
	}
	
	public synchronized void stop()
	{
		if(!isRunning)
			return;
		isRunning = false;
		paintTimer.stop();
		try{
			tickThread.join();}
		catch (InterruptedException e){}
		System.exit(0);
	}
	
	@Override
	public void run()
	{
		int ticks = 0;
		long startTime = System.currentTimeMillis();
		long lastTick = System.nanoTime();
		
		double nanosPerTick = 1000000000.0 / 60;
		double unprocessed = 0;
		
		requestFocus();
		
		while(isRunning)
		{
			long currentTimeMilli = System.currentTimeMillis();
			long currentTimeNano = System.nanoTime();
			
			unprocessed += (currentTimeNano - lastTick) / nanosPerTick;
			lastTick = currentTimeNano;
			
			while(unprocessed >= 1)
			{
				ticks++;
				unprocessed -= 1;
				logic.tick();
			}
			
			try
			{
				Thread.sleep(2);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(currentTimeMilli - startTime > 1000)
			{
				startTime += 1000;
				System.out.println(ticks + " ticks");
				ticks = 0;
			}
		}
	}
	
	public void render()
	{
		bs = getBufferStrategy();
		if(bs == null)
		{
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		try //try and catch clause for the case of the BufferStrategy being null after resizing the frame
		{
			Graphics g = bs.getDrawGraphics();
			paint.paint(g);
			g.dispose();
		}
		catch(Exception e)
		{
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		if(!bs.contentsLost())
			bs.show();
	}

	private class PaintTimerListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			render();
		}
	}
	
	public static void main(String[] args) {
		MalariaCanvas canvas = new MalariaCanvas();

		frame = new JFrame("Jail Escape");
		frame.setUndecorated(true);

		panel = new JPanel(new BorderLayout());
		panel.add(canvas, BorderLayout.CENTER);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtilities.invokeLater(new Runnable(){
									public void run()
									{
										frame.setVisible(true);
									}});

		canvas.start();
	}
}
