

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{
	Image background;
	Image playNow;
	Image exitGame;
	Sound s;
	int cnt=0;

	public Menu(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{	
		background = new Image("res/bg.png");
		playNow = new Image("res/start.png");
		
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		
		background.draw(0, 0);
		g.drawString("Welcome to the world of immunity system.", 100, 50);
		playNow.drawCentered(600, 375);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		int posX = Mouse.getX();
		int posY = Mouse.getY();

		if(posX>250 && posX<950&& posY>325 && posY<425){
			if(Mouse.isButtonDown(0)){
				sbg.enterState(1);
			}
		}
		
		//if((posX>(100-32)*2 && posX<(311-32)*2)&& (posY>(109+340-60)*2 && posY<(160+340-60)*2)){
		//	if(Mouse.isButtonDown(0)){
		//		System.exit(0);
		//	}
		//}
	}
	
	public int getID(){
		return 0;
	}
	
}
