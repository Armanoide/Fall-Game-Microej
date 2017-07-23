package moc.lab.activities;

import java.io.IOException;
import java.util.Random;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.components.dependencyinjection.ServiceLoaderFactory;
import ej.exit.ExitHandler;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.event.Event;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;
import moc.lab.activities.MenuGame;
import moc.lab.activities.MenuGame.Mode;

public class Game extends Displayable implements EventHandler  {

	static int LEFT_POSITION = 10;
	static int RIGHT_POSITION = 440;

	private enum Moving {
		LEFT,RIGHT, NONE
	}

	MenuGame menu;

	private int partialPos1;
	private int partialPos2;
	private int partialPos3;
	private Image partial2;
	private Image partial3;
	private Image overRight;
	private Image overLeft;
	private Image manLeft;
	private Image manRight;
	private Image border;
	private Moving moving;
	private Boolean isOnLeft;
	private int position;
	private Boolean gameOver;
	private Random rn = new Random();
	private int score;
	private Timer timerGameOver;
	private int speedDecrease;
	private int positionOvercomeLeft1;
	private int positionOvercomeRight1;
	private Boolean changedParitalPos;


	public Game(Display display)
	{
		super(Display.getDefaultDisplay());
		menu = new MenuGame(display);
		menu.mode = MenuGame.Mode.MENU;

		resetGame();

		try
		{

			partial2 = Image.createImage("/images/player_particle_2.png");
			partial3 = Image.createImage("/images/player_particle_3.png");

			overRight = Image.createImage("/images/overcome_right.png");
			overLeft = Image.createImage("/images/overcome_left.png");
			manLeft = Image.createImage("/images/hand_fall_perso_left.png");
			manRight = Image.createImage("/images/hand_fall_perso_right.png");
			border = Image.createImage("/images/border.png");
		} catch (IOException e) {
			e.printStackTrace();
		}	

		autoRedrawGame();
		animationPartial();
		increaseSpeedGame();
	}

	private void autoRedrawGame() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {				

				repaint();

			}
		}, 0, 10);
	}

	private void animationPartial()
	{
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {				

				changedParitalPos = true;
			}
		}, 0, 70);	

	}

	private void increaseSpeedGame() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {				

				speedDecrease++;
			}
		}, 0, 5000);
	}

	private void resetGame() {
		speedDecrease = 1;
		positionOvercomeLeft1 = 500;
		positionOvercomeRight1 = 100;
		score = 0;
		isOnLeft = false;
		moving = Moving.NONE;
		position = LEFT_POSITION;
		if (timerGameOver != null) {
			timerGameOver.cancel();
		}
		timerGameOver = null;
		gameOver = false;
		menu.needReset = false;
	}

	private void clearScreen(GraphicsContext g)
	{
		g.setColor(0xF25556);
		g.fillRect(0, 0, Display.getDefaultDisplay().getWidth(), Display.getDefaultDisplay().getHeight());
	}

	@Override
	public boolean handleEvent(int event) {
		if (menu.mode == MenuGame.Mode.MENU) {
			return menu.handleEvent(event);
		}
		if (menu.mode == MenuGame.Mode.GAME) {
			if (Event.getType(event) == Event.POINTER) {
				Event.getGenerator(event);	
				if (Pointer.isReleased(event)) {
					if (moving == Moving.NONE) {
						moving = isOnLeft ? Moving.RIGHT :  Moving.LEFT;
					}				
				}
			}
		}
		return false;
	}


	private void detectGameOver() {	


		if ((!isOnLeft
				&& 50 >= positionOvercomeRight1 
				&& 50 <= positionOvercomeRight1 + 32 
				&& Moving.NONE == moving)
				||
				(!isOnLeft
						&& 50 >= positionOvercomeRight1 
						&& 50 <= positionOvercomeRight1 + 32 
						&& Moving.NONE == moving 
						)
				) {
			gameOver = true;
		}


		if ((isOnLeft
				&& 50 >= positionOvercomeLeft1 
				&& 50 <= positionOvercomeLeft1 + 32 
				&& Moving.NONE == moving)
				||
				(isOnLeft
						&& 50 >= positionOvercomeLeft1 
						&& 50 <= positionOvercomeLeft1 + 32 
						&& Moving.NONE == moving 
						)
				) {
			gameOver = true;
		}
	}


	private void displayGameOver(GraphicsContext g)
	{
		Font font = Font.getFont(82, 50, Font.STYLE_PLAIN);
		String myS = new String("GAME OVER");
		g.setFont(font);
		g.setColor(Colors.WHITE);
		g.drawString(myS, 140, 110, 0);	

		// wait 3 second before return to menu
		if (timerGameOver == null) {
			timerGameOver = new Timer();
			timerGameOver.schedule(new TimerTask() {

				@Override
				public void run() {				
					menu.mode = Mode.MENU;
					resetGame();
				}
			}, 3000, 10);

		}
	}


	private void displayCurrentScore(GraphicsContext g) {
		Font font = Font.getFont(82, 50, Font.STYLE_PLAIN);
		String myS = new String(""+score);
		g.setFont(font);
		g.setColor(Colors.WHITE);
		g.drawString(myS, 400, 0, 0);
	}


	private void displayPartial(GraphicsContext g) {
		// generate some random position for partial
		if (changedParitalPos) {
			partialPos1 = rn.nextInt(10 - 1 + 1) + 1;
			partialPos2 = rn.nextInt(10 - 1 + 1) + 1;
			partialPos3 = rn.nextInt(10 - 1 + 1) + 1;
			changedParitalPos = false;
		}

		// display partial
		if (moving.equals(Moving.NONE)) {
			g.drawImage(partial2, position + partialPos1, 50 - 10, GraphicsContext.TOP);								
			g.drawImage(partial2, position + partialPos2, 50 - 20, GraphicsContext.TOP);								
			g.drawImage(partial3, position + partialPos3 + 1, 50 - 30, GraphicsContext.TOP);												
		}
	}

	private void displayOvercome(GraphicsContext g) {
		// manage overcome left
		positionOvercomeLeft1 -= speedDecrease;
		if (positionOvercomeLeft1 <= -10) {
			score += 10;
			positionOvercomeLeft1 = rn.nextInt(1000 - 500 + 1) + 500;
		}

		g.drawImage(overLeft, LEFT_POSITION, positionOvercomeLeft1, GraphicsContext.TOP);				

		// manage overcome right
		positionOvercomeRight1 -= speedDecrease;
		if (positionOvercomeRight1 <= -10) {
			score += 10;
			positionOvercomeRight1 = rn.nextInt(1000 - 500 + 1) + 500;
		}
		g.drawImage(overRight, RIGHT_POSITION, positionOvercomeRight1, GraphicsContext.TOP);	
	}
	
	private void preparePositionPlayer(GraphicsContext g){

		if (moving == Moving.RIGHT) {
			position += 15;
		} else if(moving == Moving.LEFT) {
			position -= 15;
		}

		if (position <= LEFT_POSITION) {
			position = LEFT_POSITION;
			isOnLeft = true;
			moving = Moving.NONE;
		}
		if (position >= RIGHT_POSITION) {
			position = RIGHT_POSITION;
			isOnLeft = false;
			moving = Moving.NONE;
		}

		if (isOnLeft) {
			g.drawImage(manLeft, position, 50, GraphicsContext.TOP);				
		} else {
			g.drawImage(manRight, position, 50, GraphicsContext.TOP);								
		}
	}
	
	@Override
	public void paint(GraphicsContext g) {

		if (score > menu.bestScore) {
			menu.bestScore = score;
		}

		if (menu.mode == MenuGame.Mode.MENU) {
			menu.paint(g);	
		}

		if (menu.mode == MenuGame.Mode.QUIT) {
			ExitHandler exitHandler = ServiceLoaderFactory.getServiceLoader().getService(ExitHandler.class);
			if (exitHandler != null) {
				System.out.println("ERROR EXIT");
				exitHandler.exit();
			}
		}

		if (menu.mode == MenuGame.Mode.GAME) {

			if (menu.needReset == true) {
				resetGame();
			}

			this.detectGameOver();

			if (gameOver) {				

				displayGameOver(g);
				return;
			}

			clearScreen(g);

			preparePositionPlayer(g);

			displayPartial(g);

			displayOvercome(g);
					

			// border left and right
			g.drawImage(border, 0, 0, GraphicsContext.TOP);
			g.drawImage(border, 470, 0, GraphicsContext.TOP);

			displayCurrentScore(g);	
		}
	}


	@Override
	public EventHandler getController() {
		if (menu.mode == MenuGame.Mode.MENU) {
			return menu;			
		}
		if (menu.mode == MenuGame.Mode.GAME) {
			return this;
		}

		return null;
	}


}
