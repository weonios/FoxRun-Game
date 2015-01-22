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

public class Play extends BasicGameState {

	Image backGround;
	Image whiteFox1;
	Image whiteFox2;
	Image redFox1;
	Image redFox2;	
	
	
	
	boolean game = true;
	boolean white = true;
	boolean escape = false;
	

	int delay = 5;
	int elapsedTime = 0;
	int appearing = 0;

	List<EnemyTree> treeEnemies = new LinkedList<EnemyTree>();
	List<Tornado> tornadoEnemy = new LinkedList<Tornado>();
	List<Cake> cakes = new LinkedList<Cake>();
	List<Fireballs> frameballs = new LinkedList<Fireballs>();
	
	List<EnemyTree> deadTree = new LinkedList<EnemyTree>();

	int MouseX, MouseY;
	int playerX = 400;
	int playerY = 500;
	
	int score = 0;
	int miniscore = 0;
	int fireBalls = 0;
	int ghostLeft = 30;

	public Play(int state) {

	}

	public int getID() {
		return 1;
	}

	
	private void drawTornado(GameContainer gc, List<Tornado> treeEnemies) {

		for (Tornado tornado : treeEnemies) {

			tornado.tornadoImage.draw(tornado.tornadoX, tornado.tornadoY);			
		}

	}
	
	
	
	private void drawTree(GameContainer gc, List<EnemyTree> treeEnemies) {

		for (EnemyTree trees : treeEnemies) {

			trees.enemyTree.draw(trees.treeX, trees.treeY);			
		}

	}

	private void movements() {

		for (EnemyTree tree : treeEnemies) {
			tree.treeY++;
		}
		
		for (Cake cake : cakes) {
			cake.cakeY++;
		}
		
		for (Fireballs fire : frameballs) {
			fire.fireballY -= 2;
		}
		
		for (Tornado tornado : tornadoEnemy) {
			tornado.tornadoY += 2;
		}

	}
	
	
	private void drawCake(GameContainer gc, List<Cake> cakes) {

		for (Cake cake : cakes) {
			
			cake.cake.draw(cake.cakeX, cake.cakeY);
			
		}

	}
	
	private void drawFire(GameContainer gc, List<Fireballs> frameballs) {

		for (Fireballs fireball : frameballs) {
			
			fireball.fireball.draw(fireball.fireballX, fireball.fireballY);			
		}
	}
	
	
	
	private boolean hittedCake() {		
		boolean hitted = false;
		Cake eatenCake = null;
		
		for (Cake cake : cakes) {
			
			if ((cake.cakeY + 40 == playerY || cake.cakeY == playerY) && (cake.cakeX == playerX || cake.cakeX + 20 == playerX)) {
				
				eatenCake = cake;
				cakes.remove(eatenCake);
				
				hitted = true;
			}
		}			
		return hitted;
	}
	

	private boolean hittedFox() {
		boolean hitted = false;

		for (EnemyTree tree : treeEnemies) {

			if ((tree.treeY + 30 == playerY || tree.treeY == playerY) && tree.treeX == playerX) {

				hitted = true;
			}
		}
		
		for (Tornado tornado : tornadoEnemy) {
			if ((tornado.tornadoY + 25 == playerY || tornado.tornadoY == playerY) && tornado.tornadoX == playerX) {

				hitted = true;
			}
		}
		
		
		return hitted;
	}
	
	
	private boolean hittedTree() {
		boolean hitted = false;
		
		for (Fireballs fire : frameballs) {
			for (EnemyTree tree : treeEnemies) {
								
				if(inBox(fire.fireballX, fire.fireballY, tree.treeX, tree.treeY, tree.treeX + 40, tree.treeY + 20)) {
	
					deadTree.add(tree);
					treeEnemies.removeAll(deadTree);
					return true;										
				}
			}
		}
		return hitted;
	}
	
	
	private boolean treeOutOfBorder() {
		boolean outside = false;
				
		for (EnemyTree tree : treeEnemies) {
			
			if (tree.treeY > 580) {

				deadTree.add(tree);	
				
				treeEnemies.removeAll(deadTree);
				return true;										
			}
		}
	
		return outside;
	}
	
	
	
	

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {

		backGround = new Image("res/field.jpg");
		whiteFox1 = new Image("res/foxWhite1.png");
		whiteFox2 = new Image("res/foxWhite2.png");
		treeEnemies.add(new EnemyTree(50 * (new Random().nextInt(17)), 0, new Image("res/Ghost.png")));
				
								
		redFox1 = new Image("res/foxRed1.png");
		redFox2 = new Image("res/foxRed2.png");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		backGround.draw();
		
		if (white) {
			whiteFox1.draw(playerX, playerY);
			whiteFox2.draw(playerX, playerY);
		}else {
			
			redFox1.draw(playerX, playerY);
			redFox2.draw(playerX, playerY);			
		}
		

		drawTree(gc, treeEnemies);
		drawCake(gc, cakes);
		drawFire(gc, frameballs);
		drawTornado(gc, tornadoEnemy);
		
		g.drawString("Ghost to kill = " + ghostLeft, 50, 560);
		g.drawString("Fireballs = " + fireBalls, 550, 560);
		
		if (!game) {
			g.drawString("Game Over", 350, 250);
			g.drawString("Exit", 370, 300);
		}
		
		if (escape) {
			
			g.drawString("Pause! (press escape to resume)", 340, 250);
			g.drawString("Exit", 340, 300);	
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int g)
			throws SlickException {
		Input input = gc.getInput();
		MouseX = input.getMouseX();
		MouseY = input.getMouseY();

		if (ghostLeft <= 0) {
			
			sbg.enterState(2);
		}
		
		if (escape) {
			game = false;
		}
		
		
		if (game) {
			
			
			if (treeOutOfBorder()) {
				deadTree.clear();
			}
			
			if (fireBalls <= 0) {
				
				white = true;
				fireBalls = 0;
			}
			
			if (delay < elapsedTime) {

				elapsedTime = 0;
				movements();

			} else {
				elapsedTime += g;
			}

			if (appearing >= 200) {

				appearing = 0;
				treeEnemies.add(new EnemyTree(50 * (new Random().nextInt(17)),
						0, new Image("res/Ghost.png")));
				
				
				if ((score % 300 == 0 || score % 300 == 5 || score % 300 == 10)&& score > 0) {
					tornadoEnemy.add(new Tornado(50 * (new Random().nextInt(17)), 0, new Image("res/tornado.png")));
				}
				
				
				miniscore++;
				
				if (miniscore >= 5) {
					score += 5;
					miniscore = 0;
					
					if (score % 50 == 0 && score > 0) {
						
						cakes.add(new Cake(50 * (new Random().nextInt(17)), 50 * (new Random().nextInt(6)), new Image("res/fireHeart.png")));
					}
				}
				

			} else {
				appearing += g;
			}

			if (hittedFox()) {

				game = false;
			}
			
			if (hittedCake()) {
				white = false;
				
//				for (Cake cake : cakes) {
//					cakes.remove(cake);
//				}				
				fireBalls += 3;
			}
			
			if (hittedTree()) {
				deadTree.clear();
				score += 50;
				ghostLeft--;
			}

			

			if (input.isKeyPressed(Input.KEY_LEFT)) {

				playerX -= 50;
			}

			if (input.isKeyPressed(Input.KEY_RIGHT)) {

				playerX += 50;
			}

			if (input.isKeyPressed(Input.KEY_SPACE)) {

				if (fireBalls > 0 && !white) {
					
					frameballs.add(new Fireballs(playerX + 10 , playerY, new Image("res/fire.png")));					
					fireBalls--;
				}
			}
			
			if (input.isKeyPressed(Input.KEY_ESCAPE)) {
				escape = true;
			}
		}
		
		else {
			
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

	public boolean inBox(int x, int y, int xSmall, int ySmall, int xBig, int yBig) {
		return (x >= xSmall && x <= xBig) && (y >= ySmall && y <= yBig);
	}
}