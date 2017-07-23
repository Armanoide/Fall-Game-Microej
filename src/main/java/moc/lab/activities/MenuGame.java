package moc.lab.activities;

import java.io.IOException;

import ej.bon.Timer;
import ej.bon.TimerTask;
import ej.microui.display.Colors;
import ej.microui.display.Display;
import ej.microui.display.Displayable;
import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.microui.display.Image;
import ej.microui.event.Event;
import ej.microui.event.EventGenerator;
import ej.microui.event.generator.Pointer;
import ej.microui.util.EventHandler;

public class MenuGame extends Displayable implements EventHandler {

	private Image logo;
	private Image start;
	private Image score;
	private Image quit;
	public Mode mode;
	public Boolean needReset = false;
	public int bestScore;
	
	public enum Mode {
		MENU,GAME,SCORE,QUIT
	}

	public MenuGame(Display display) {
		super(Display.getDefaultDisplay());

		try
		{
			logo = Image.createImage("/images/logo.png");
			start = Image.createImage("/images/start.png");
			score = Image.createImage("/images/score.png");
			quit = Image.createImage("/images/quit.png");
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		/*
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {				

				repaint();
				
			}
		}, 0, 10);*/
	}
	

	@Override
	public boolean handleEvent(int event) {
		// TODO Auto-generated method stub
		
		if (Event.getType(event) == Event.POINTER) {
			Pointer p = (Pointer)Event.getGenerator(event);	
			if (Pointer.isReleased(event)) {
				int pointerX = p.getAbsoluteX();
				int pointerY = p.getAbsoluteY();

				if (isInside(pointerX, pointerY, 400, 30, 230, 140)) {
					System.out.println("START");
					this.mode = Mode.GAME;
					this.needReset = true;
				}
				if (isInside(pointerX, pointerY, 400, 30, 230, 190)) {
					this.mode = Mode.QUIT;
				}				
			}
		} else {
			repaint();
		}
		return false;
	}
	
	private boolean isInside(int xObject, int yObject, 
			int width, int height, int xRef, int yRef) {
			
		return     xObject <= width  + xRef && xObject >= xRef 
				&& yObject <= height + yRef && yObject >= yRef ;		
	}
	

	private void clearScreen(GraphicsContext g)
	{
		g.setColor(Colors.BLACK);
		g.fillRect(0, 0, Display.getDefaultDisplay().getWidth(), Display.getDefaultDisplay().getHeight());
	}
	
	@Override
	public void paint(GraphicsContext g) {
		// TODO Auto-generated method stub

		this.clearScreen(g);
		
		Font font = Font.getFont(83, 50, Font.STYLE_PLAIN);
		String myS = new String("best score: " + bestScore);
		g.setFont(font);
		g.setColor(Colors.WHITE);
		g.drawString(myS, 210, 110, 0);

		
		g.drawImage(logo, this.getDisplay().getWidth()/2, this.getDisplay().getHeight()/2 - logo.getHeight()/2, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		g.drawImage(start, 230, 140, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
		g.drawImage(quit, 230, 190, GraphicsContext.HCENTER | GraphicsContext.VCENTER);
	}

	@Override
	public EventHandler getController() {
		return this;
	}
	
	
	
}
