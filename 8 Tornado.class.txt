package javagame;

import org.newdawn.slick.Image;

public class Tornado {
		
	Image tornadoImage;
	int tornadoX;
	int tornadoY;
	
	
	
	public Tornado(int tornadoX, int tornadoY, Image tornadoImage) {
		
		this.tornadoX = tornadoX; 
		this.tornadoY = tornadoY;
		this.tornadoImage = tornadoImage;				
	}

}
