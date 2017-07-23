package moc.lab.activities;

import ej.microui.MicroUI;
import ej.microui.display.Display;
import ej.wadapps.app.Activity;

public class MainActivity implements Activity {    

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRestart() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onStart() {
		MicroUI.start();
		Game game = new Game(null);
		game.show();
	
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}


}
