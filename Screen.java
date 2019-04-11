package ch.yezhekel.reign.graphics;

import java.util.Random;

public class Screen{
	
	private int width, height;
	public int[] pixels;
	public final int MAP_SIZE = 16;
	public final int MAP_SIZE_MASK = MAP_SIZE -1;
	
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
	
	private Random random = new Random();
	
	//Screen constructor
	public Screen(int width, int height){
		
		this.width = width;
		this.height = height;
		pixels = new int[width * height]; //50,400 pixel indices
		
		for(int i = 0; i < MAP_SIZE * MAP_SIZE; i++){
			tiles[i] = random.nextInt(0xFFFFFF);
		}
		
	}//end Screen
	
	//clear() method
	public void clear(){
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
	}//end clear()
	
	//render() method
	public void render(int xOff, int yOff){
		for(int y = 0; y < height; y++){
			int yy = y + yOff;
			if(y >= height || y < 0) break;
			
			for(int x = 0; x < width; x++){
				int xx = x + xOff;
				if(x>= width || x < 0) break;
				//int tileIndex = ((xx / MAP_SIZE) & MAP_SIZE_MASK) + ((yy / MAP_SIZE) & MAP_SIZE_MASK) * 16;
				pixels[x + y * width] = Sprite.grass.pixels[(x & 15) + (y & 15) * Sprite.grass.SIZE];
			}
		}
	}//end render()

}
