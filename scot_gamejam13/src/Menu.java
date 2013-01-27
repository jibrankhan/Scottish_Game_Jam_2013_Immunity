

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{
	Image background;
	Image playNow;
	Image instruction;
	Sound s;

	public Menu(int state){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{	
		background = new Image("res/bg.png");
		playNow = new Image("res/start.png");
		instruction = new Image("res/instructions.png");
		if (s==null){
			s=new Sound ("sound/bg.wav");
			s.loop();
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		
		background.draw(0, 0);
		instruction.drawCentered(600, 300);
		playNow.drawCentered(600, 600);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		int posX = Mouse.getX();
		int posY = Mouse.getY();

		if(posX>385 && posX<815&& posY>100 && posY<200){
			if(Mouse.isButtonDown(0)){
				if(s!=null && s.playing())
					s.stop();
				sbg.enterState(1);
			}
		}
		
	}
	
	public int getID(){
		return 0;
	}
	
}
