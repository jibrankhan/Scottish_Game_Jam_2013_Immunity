

import java.io.File;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame{
	
	public static final String gamename = "Immunity!";
	public static final int menu = 0;
	public static final int play = 1;
	
	public Game(String gamename){
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
	}
	
	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.enterState(menu);
	}
	public static void linkLwjgl() {

		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
	}

	public static void main(String args[]){
		linkLwjgl();
		AppGameContainer appgc;
		
		try{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setDisplayMode(1200, 750, false);
			appgc.setShowFPS(false);
			appgc.setTargetFrameRate(200);
			appgc.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
	
}
