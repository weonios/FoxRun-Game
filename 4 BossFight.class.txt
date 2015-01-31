package javagame;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class BossFight extends BasicGameState {

	
	Image backGround;
	Image redFox1;
	Image redFox2;
	Image fairy;
	
	boolean game = true;
	boolean left = true;
	boolean escape = false;
	
	int MouseX, MouseY;
	int playerX = 400;
	int playerY = 500;
	int fairyX = 400;
	int fairyY = 50;
	
	int delay = 5;
	int elapsedTime = 0;
	
	int bossHP = 300;
	int playerHP = 100;
	int fires = 1;
	int firecounting = 1;
	int bossmoving = 0;
	
	List<Fireballs> frameballs = new LinkedList<Fireballs>();
	List<Fireballs> ball = new LinkedList<Fireballs>(); 
	List<Fireballs> fairyballs = new LinkedList<Fireballs>();
	List<Tornado> fairyTornados = new LinkedList<Tornado>();
	List<Tornado> cyclone = new LinkedList<Tornado>();
	
	public BossFight(int state) {

	}


	
	private void drawFire(GameContainer gc, List<Fireballs> frameballs) {

		for (Fireballs fireball : frameballs) {
			
			fireball.fireball.draw(fireball.fireballX, fireball.fireballY);			
		}
	}
	
	
	private void drawFairyFire(GameContainer gc, List<Fireballs> fairyballs) {

		for (Fireballs fairyball : fairyballs) {
			fairyball.fireball.draw(fairyball.fireballX, fairyball.fireballY);
		}
	}
	
	
	private void drawTornado(GameContainer gc, List<Tornado> fairyTornados) {
		
		for (Tornado tornado : fairyTornados) {
			tornado.tornadoImage.draw(tornado.tornadoX, tornado.tornadoY);
		}
	}
	
	
	private boolean hittedBoss() {
		boolean hitted = false;
		
		
		for (Fireballs fire : frameballs) {
			
			if(inBox(fire.fireballX, fire.fireballY, fairyX, fairyY, fairyX + 85, fairyY + 80)) {
				
				ball.add(fire);
				frameballs.removeAll(ball);
				return true;
			}
		}
		
		return hitted;
	}
	
	
	private boolean hittedFox() {
		boolean hitted = false;
		Fireballs fireBall = null;
		for (Fireballs fairyball : fairyballs) {

			if (inBox(fairyball.fireballX, fairyball.fireballY, playerX, playerY, playerX + 30, playerY + 20)) {
				
				fireBall = fairyball;

				 hitted = true;
			}			
		}
				
		fairyballs.remove(fireBall);
		return hitted;
	}
	
	
	private boolean hittedFoxByTornado() {
		boolean hitted = false;		
		
		for (Tornado tornado : fairyTornados) {
			if (inBox(tornado.tornadoX, tornado.tornadoY, playerX, playerY - 10, playerX + 30, playerY)) {

				cyclone.add(tornado);
				
				hitted = true;
			}
		}
		fairyTornados.removeAll(cyclone);		
		return hitted;
	}
	
	
	
	
	private void movements() {
		
		
		for (Fireballs fire : frameballs) {
			fire.fireballY -= 3;
								
		}
		
		for (Fireballs fairyball : fairyballs) {
			fairyball.fireballY += 4;
		}
		
		for (Tornado tornado : fairyTornados) {
			tornado.tornadoY += 3;
		}
		
			
		if (fires < 3) {
			
			firecounting++;
			if (firecounting >= 180) {
				fires++;
				firecounting = 0;
			}
		}
		
		
		if (bossmoving >= 1200) {
			
			bossmoving = 0;
			
			if (left) {
				
				fairyX -= 50;
				
				if (fairyX - 50 <= 0) {
					left = false;
				}
				
			}else {
				
				fairyX += 50;
				
				if (fairyX + 100 >= 800) {
					left = true;
				}
			}
		}

	}
	
	
	
	private boolean fireOutside() { 
		
		boolean outside = false;
		 
		
		
		for (Fireballs fire : frameballs) {
			
			for (Tornado tornado : fairyTornados) {
				
				if(inBox(fire.fireballX, fire.fireballY, tornado.tornadoX, tornado.tornadoY - 10, tornado.tornadoX + 30, tornado.tornadoY)) {
					
					ball.add(fire);
					frameballs.removeAll(ball);
					return true;
				}
			}
		}
		
		
		for (Fireballs fire : frameballs) {
			
			for (Fireballs fairy : fairyballs) {
				
				if(inBox(fire.fireballX, fire.fireballY, fairy.fireballX, fairy.fireballY - 10, fairy.fireballX + 30, fairy.fireballY)) {
					
					ball.add(fire);
					ball.add(fairy);
					frameballs.removeAll(ball);
					fairyballs.removeAll(ball);
					return true;
				}
			}
		}
		
		
		for (Fireballs fire : frameballs) {
												
			if (fire.fireballY <= 5  ) {
				
				ball.add(fire);
				frameballs.removeAll(ball);
				
				return true;
			}
		}
		
		return outside;
	}
	
	
private boolean tornadoOutside() { 
		
		boolean outside = false;
		
		for (Tornado tornado : fairyTornados) {
												
			if (tornado.tornadoY >= 550  ) {
				
				cyclone.add(tornado);
				fairyTornados.removeAll(cyclone);
				
				return true;
			}
		}
		
		return outside;
	}
	
	
	
	private boolean fairyballOutside() { 
		
		boolean outside = false;
		 
		
		for (Fireballs fball : fairyballs) {
												
			if (fball.fireballY >= 590  ) {
				
				ball.add(fball);
				fairyballs.removeAll(ball);
				
				return true;
			}
		}
		
		return outside;
	}
	
	
	
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		
		backGround = new Image("res/field.jpg");
		redFox1 = new Image("res/foxRed1.png");
		redFox2 = new Image("res/foxRed2.png");
		fairy = new Image("res/Dragon.png");
		
	}

	
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame stb, Graphics g)
			throws SlickException {
		
		backGround.draw();
		redFox1.draw(playerX, playerY);
		redFox2.draw(playerX, playerY);	
		fairy.draw(fairyX - 25, fairyY);
		
		drawFire(gc, frameballs);
		drawFairyFire(gc, fairyballs);
		drawTornado(gc, fairyTornados);
		
		g.drawString("Fox Health = " + playerHP, 50, 560);
		g.drawString("Fireballs = " + fires, 550, 560);		
		g.drawString("Boss Health = " + bossHP, 550, 10);
		
		if (!game) {
			
			if (playerHP <= 0) {
			
				g.drawString("Game Over", 350, 250);
				g.drawString("Exit", 370, 300);				
			}else {
				if (bossHP <= 0) {	
					
					g.drawString("You Win!", 350, 250);
					g.drawString("Exit", 370, 300);				
				}else {
					
					g.drawString("Pause! (press escape to resume)", 340, 250);
					g.drawString("Exit", 340, 300);	
				}
			}
		}
		
	}

	
	
	
	@Override
	public void update(GameContainer gc, StateBasedGame stb, int g)
			throws SlickException {
		Input input = gc.getInput();
		MouseX = input.getMouseX();
		MouseY = input.getMouseY();
		
		if (escape) {
			game = false;
		}
		
		
		if (game) {
				
			if (bossHP <= 0 || playerHP <= 0) {
				game = false;
			}
			
			if (bossHP <= 100) {
				bossmoving += g;
			}
			bossmoving += g;
			
			if (bossHP > 200 && bossmoving == 10) {
				
				fairyballs.add(new Fireballs(fairyX + 10 , fairyY + 80, new Image("res/fairyfire.png")));
			}
			
			if (bossHP <= 200 && bossmoving == 10) {
				fairyTornados.add(new Tornado(fairyX + 10 , fairyY + 80, new Image("res/tornado.png")));
			}
			
			
			if (bossHP % 100 == 0 && bossHP < 300 && (bossmoving % 300 == 0 || bossmoving % 500 == 0)) {
				fairyTornados.add(new Tornado(50 * (new Random().nextInt(17)), 0, new Image("res/tornado.png")));
			}
			
			
			if (delay < elapsedTime) {

				elapsedTime = 0;
				movements();

			} else {
				elapsedTime += g;
			}
			
			if (hittedFox()) {
				ball.clear();
				playerHP -= 10;
			}
			
			if (hittedFoxByTornado()) {
				playerHP -= 10;
				cyclone.clear();
			}
			
			
			if (hittedBoss()) {
				ball.clear();
				bossHP -= 10;
			}
			
			
			
			
			if (fireOutside()) {
				ball.clear();
			}
			
			
			if (fairyballOutside()) {
				ball.clear();
			}
			
			if (tornadoOutside()) {
				cyclone.clear();
			}

			
			if (input.isKeyPressed(Input.KEY_LEFT)) {

				playerX -= 50;
			}

			if (input.isKeyPressed(Input.KEY_RIGHT)) {

				playerX += 50;
			}

			if (input.isKeyPressed(Input.KEY_SPACE)) {

				if (fires > 0) {
					frameballs.add(new Fireballs(playerX + 10 , playerY, new Image("res/fire.png")));
					fires--;
				}									

			}
			
			if (input.isKeyPressed(Input.KEY_ESCAPE)) {
				escape = true;
			}
			
			
		}else {
			
			
			if (escape) {
				if (input.isKeyPressed(Input.KEY_ESCAPE)) {
					escape = false;
					game = true;
				}
			}				
			
			if (input.isMousePressed(0) && inBox(MouseX, MouseY, 340, 300, 400, 340)) {
				System.exit(0);
			}
			

		}
		
		
	}
	
	public int getID() {
		return 2;
	}
	
	public boolean inBox(int x, int y, int xSmall, int ySmall, int xBig, int yBig) {
		return (x >= xSmall && x <= xBig) && (y >= ySmall && y <= yBig);
	}

	
}
