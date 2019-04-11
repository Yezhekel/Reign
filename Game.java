package ch.yezhekel.reign;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import ch.yezhekel.reign.graphics.Screen;
import ch.yezhekel.reign.input.Keyboard;

public class Game extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = 168; // = width / 16 * 9;
	public static int scale = 3;
	public static String title = "Reign";
	
	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private boolean running = false;
	
	private Screen screen;
	
	private BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	
	public Game(){
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		
		addKeyListener(key);
		
	}
	
	//start() method
	public synchronized void start(){
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}//end start()
	
	//stop() method
	public synchronized void stop(){
		running = false;
		try{
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}//end stop() method
	
	//run() method
	public void run(){
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int ticks = 0;
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			while(delta >= 1){
				tick();
				ticks++;
				delta--;
			}
			
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				//System.out.println(ticks + " tps, " + frames + " fps");
				frame.setTitle(title + " | " + ticks + " tps, " + frames + " fps");
				ticks = 0;
				frames = 0;
			}
			
		}
		
		stop();
	}//end run() method
	
	int xOff = 0, yOff = 0;
	
	//tick() method
	public void tick(){
		key.update();
		if(key.up) yOff--;
		if(key.down) yOff++;
		if(key.left) xOff--;
		if(key.right) xOff++;
		
	}//end tick()
	
	//render() method
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		screen.render(xOff, yOff);
		
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
		
	}//end render()
	
	//main() method
	public static void main(String[] args){
		
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(Game.title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
		
	}//end main()

}//end of class
