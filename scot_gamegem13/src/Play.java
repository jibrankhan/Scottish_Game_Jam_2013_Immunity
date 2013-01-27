import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Sound;
//memo - sort if one destroyed either for red and virus
// 

public class Play extends BasicGameState{
	final int endX =1200;
	final int endY =750;
	final int max_virus = 20;
	final int virus_timer = 1000;
	final float[][] spawn= new float [][]{{400,130}, {600,450}, {800, 300}, {200, 700}, {1000, 600}};
	final int[] spawn_timer=new int[]{300,700,400,700,800};
	final int no_spawn = 5;
	final float player_speed = 2;
	final float virus_speed = (float) 1.5;
	final float red_speed = (float) 0.5;
	float p1X_temp, p1Y_temp, p2X_temp, p2Y_temp;
	float minX, minY;
	int[] collision = new int[70];
	int p1_slain = 0;
	int p2_slain = 0;
	float flow_speed = 0.5f;
	float heart_beat = 100.0f;
	float p1X = 500;
	float p1Y = 150;
	float p2X = 500;
	float p2Y = 350;
	float heartbeat_rate=100;
	Image background;
	Image p1;
	Image p2;
	Image wound;
	Image red;
	Image virus;
	int wound_active=2;
	int virus_existing =0;
	
	ArrayList<SimpleVirus> spawned_virus = new ArrayList<SimpleVirus>(20);
	//for red cells
	int initializer = 70;
	int red_alive =0; // passing right end -> respawn from left end 
	float[][] red_location = new float[70][2];
	Random lane_finder = new Random();
	public Play(int state){
	}
	
	// initializer
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		background = new Image("res/bg.png");
		p1 = new Image("res/white_p1.png");
		p2 = new Image("res/white_p2.png");
		wound = new Image("res/wound.png");
		red = new Image("res/red.png");
		virus = new Image("res/virus.png");
		for (;initializer>0; red_alive+=1, initializer-=1){
			//System.out.println(red_alive+" ");
			red_location[red_alive][1]=lane_finder.nextInt(endX-64)+32;
			red_location[red_alive][0]=(initializer*120)/7;
		}

	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{

		background.draw();
		for (int i = 0; i<no_spawn; i++){
			wound.drawCentered(spawn[i][0], spawn[i][1]);
		}
		for(int j=0; j<wound_active; j++){
			if(spawn_timer[j]<=0 && virus_existing <= max_virus){
				//System.out.println(virus_existing);
				spawned_virus.add(new SimpleVirus(spawn[j][0], spawn[j][1], red_location));

				virus_existing ++;
				spawn_timer[j]+=virus_timer*virus_existing;

				spawned_virus.get(virus_existing-1).computeShortestDistance();
			}
		}
		for (int k=0; k<virus_existing; k++){
			virus.drawCentered(spawned_virus.get(k).getX(), spawned_virus.get(k).getY());
		}
		for (int z=0; z<red_alive; z++){
			red.drawCentered(red_location[z][0], red_location[z][1]);
		}

		p1.drawCentered(p1X, p1Y);
		p2.drawCentered(p2X, p2Y);
		g.drawString("heart beat"+(heart_beat+70-red_alive) 
				+", p1: "+p1_slain+", p2: "+p2_slain+ ", Player Cords:"+p1X+", "+p1Y, 50, 50);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input=gc.getInput();
		for (int i=0; i<wound_active; i++){
			spawn_timer[i] -=10;
		}
		// For each virus
		for(int j=0; j<virus_existing; j++){
			//spawned_virus[j][0]+=flow_speed;
			
			if (spawned_virus.get(j).getX()>=endX-16)
				spawned_virus.get(j).setX(endX-16);
			if (spawned_virus.get(j).getX()<16)
				spawned_virus.get(j).setX(16);
			if (spawned_virus.get(j).getY()>=endY-16)
				spawned_virus.get(j).setY(endY-16);
			if (spawned_virus.get(j).getY()<16)
				spawned_virus.get(j).setY(16);
			if(((p1X-spawned_virus.get(j).getX())*(p1X-spawned_virus.get(j).getX())
					+(p1Y-spawned_virus.get(j).getY())*(p1Y-spawned_virus.get(j).getY()))<32*32){
				for (int y=j; y<virus_existing-1; y++){
					spawned_virus.get(y).setX(spawned_virus.get(y+1).getX());
					spawned_virus.get(y).setY(spawned_virus.get(y+1).getX());
				}
				virus_existing--;
				p1_slain++;
			}
			if(((p2X-spawned_virus.get(j).getX())*(p2X-spawned_virus.get(j).getX())
					+(p2Y-spawned_virus.get(j).getY())*(p2Y-spawned_virus.get(j).getY()))<32*32){
				for (int y=j; y<virus_existing-1; y++){
					spawned_virus.get(y).setX(spawned_virus.get(y+1).getX());
					spawned_virus.get(y).setY(spawned_virus.get(y+1).getY());
				}

				virus_existing--;
				p2_slain++;
			}
		}
		for (int k=0; k<red_alive; k++){
			red_location[k][0]+=red_speed+flow_speed;
			if(red_location[k][0]>endX){
				red_location[k][0]=0;
				//red_location[k][1]=blood_vessel[lane_finder.nextInt(active_vessels)];
				red_location[k][1]=lane_finder.nextInt(endX-64)+32;
			}
			for (int z=0; z<virus_existing; z++){

				spawned_virus.get(z).chasing();
				if(((red_location[k][0]-spawned_virus.get(z).getX())*(red_location[k][0]-spawned_virus.get(z).getX())
						+(red_location[k][1]-spawned_virus.get(z).getY())*(red_location[k][1]-spawned_virus.get(z).getY()))<32*32){
					for (int y=k; y<red_alive-1; y++){
						red_location[y][0]=red_location[y+1][0];
						red_location[y][1]=red_location[y+1][1];
					}
					spawned_virus.get(z).setRed(--red_alive);
					// red_location updated
					spawned_virus.get(z).setRedLocation(red_location);
					spawned_virus.get(z).computeShortestDistance();
				}
			}
		}
		for (int i=0; i<red_alive; i++){
			if((p1X-red_location[i][0])*(p1X-red_location[i][0])
					+(p1Y-red_location[i][1])*(p1Y-red_location[i][1])<32*32){
				collision[i]=0;
			}
			else{
				collision[i]=1;
			}
			if(collision[i]==0){
				red_location[i][0]+=(p1X-p1X_temp);
				red_location[i][1]+=(p1Y-p1Y_temp);
				p1X_temp=p1X;
				p1Y_temp=p1Y;
				collision[i]=1;
			}
			if((p2X-red_location[i][0])*(p2X-red_location[i][0])
					+(p2Y-red_location[i][1])*(p2Y-red_location[i][1])<32*32){
				collision[i]=2;
			}
			else{
				collision[i]=3;
			}
			if(collision[i]==2){
				red_location[i][0]+=(p2X-p2X_temp);
				red_location[i][1]+=(p2Y-p2Y_temp);
				p2X_temp=p2X;
				p2Y_temp=p2Y;
				collision[i]=3;
			}
		}
		if(input.isKeyDown(Input.KEY_W)){
			p1Y_temp=p1Y;
			p1Y-=player_speed;
			if(p1Y<=16)
				p1Y=16;
		}
		if(input.isKeyDown(Input.KEY_S)){
			p1Y_temp=p1Y;
			p1Y+=player_speed;
			if(p1Y>=endY-16)
				p1Y=endY-16;
		}
		if(input.isKeyDown(Input.KEY_A)){
			p1X_temp=p1X;
			p1X-=player_speed;
			if(p1X<=16)
				p1X=16;
		}
		if(input.isKeyDown(Input.KEY_D)){
			p1X_temp=p1X;
			p1X+=player_speed;
			if(p1X>=endX-16)
				p1X=endX-16;
		}
		if(input.isKeyDown(Input.KEY_UP)){
			p2Y_temp=p2Y;
			p2Y-=player_speed;
			if(p2Y<=16)
				p2Y=16;
		}
		if(input.isKeyDown(Input.KEY_DOWN)){
			p2Y_temp=p2Y;
			p2Y+=player_speed;
			if(p2Y>=endY-16)
				p2Y=endY-16;
		}
		if(input.isKeyDown(Input.KEY_LEFT)){
			p2X_temp=p2X;
			p2X-=player_speed;
			if(p2X<=16)
				p2X=16;
		}
		if(input.isKeyDown(Input.KEY_RIGHT)){
			p2X_temp=p2X;
			p2X+=player_speed;
			if(p2X>=endX-16)
				p2X=endX-16;
		}
		p1X_temp=p1X;
		p1X+=flow_speed;
		if(p1X>=endX-16)
			p1X=endX-16;
		p2X_temp=p2X;
		p2X+=flow_speed;
		if(p2X>=endX-16)
			p2X=endX-16;
		if(virus_existing==15)
			virus_existing-=1;
	}
	
	public int getID(){
		return 1;
	}
	
}

