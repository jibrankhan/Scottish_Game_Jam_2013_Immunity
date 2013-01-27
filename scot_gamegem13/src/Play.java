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
	float virus_timer = 10000;
	final float[][] spawn= new float [][]{{400,130}, {600,450}, {800, 300}, {200, 700}, {800, 100}
	, {1150,100}, {100,50}, {150,400}, {600,700}, {1100,380}, {1100,700}};
	final int[] spawn_timer=new int[]{300,700,5400,5700,10800,12000,14000,17500,20000,22000, 23000};
	final int no_spawn = 11;
	final float player_speed = 1;
	final float virus_speed = (float) 0.75;
	final float red_speed = (float) 0.5;
	final int red_respawn_timer = 200;
	final int max_red = 70;
	int temp_timer = 200;
	int[] flag =new int[20];
	final float beat_changes = (float) (100/70.0);
	boolean[] distance_checked = new boolean[20];
	float minX, minY;
	float p1X_temp, p1Y_temp, p2X_temp, p2Y_temp;
	int[] collision = new int[70];
	int p1_slain = 0;
	int p2_slain = 0;
	float flow_speed = 0.25f;
	float heart_beat = 100.0f;
	float p1X = 300;
	float p1Y = 375;
	float p2X = 900;
	float p2Y = 375;
	Sound[] s = new Sound[5];
	boolean isplayed = false;
	//float heartbeat_rate=100;
	Image background;
	Image p1;
	Image p2;
	Image wound;
	Image red;
	Image virus;
	int wound_active=11;
	int virus_existing =0;
	float[][] spawned_virus = new float[20][2];
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
		p1X_temp=p1X; p1Y_temp=p1Y;
		p2X_temp=p2X; p2Y_temp=p2Y;
		for (;initializer>0; red_alive+=1, initializer-=1){
			red_location[red_alive][1]=lane_finder.nextInt(endY-64)+32;
			red_location[red_alive][0]=(initializer*120)/7;
		}

	}
	private void init_sound() throws SlickException{
		s[0]=new Sound("sound/bg-play.wav");
		s[0].loop();
		s[1]=new Sound("sound/slow_heartbeat.wav");
		s[1].loop();
		
		s[2]= new Sound("sound/mid_heartbeat.wav");
		s[3]= new Sound("sound/fast_heartbeat.wav");
		s[4]= new Sound("sound/gameover.wav");
	}
	private void heart_changing(){
		if (s[1]!=null && s[1].playing() && red_alive<45){
			s[1].stop();
			
			s[2].loop();
		}
		if (s[2]!=null &&s[2].playing() && red_alive< 20){
			s[2].stop();
			s[3].loop();
		}
		if (s[3]!=null && s[3].playing()&& s[0]!=null&&s[0].playing() && red_alive==0){
			s[3].stop();
			s[0].stop();
			s[4].loop();
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		if(!isplayed){
			init_sound();
			isplayed=true;
		}
		background.draw();
		for (int i = 0; i<no_spawn; i++){
			wound.drawCentered(spawn[i][0], spawn[i][1]);
		}
		for(int j=0; j<wound_active; j++){
			if(spawn_timer[j]<=0 && virus_existing < max_virus){
				spawned_virus[virus_existing][0]=spawn[j][0];
				spawned_virus[virus_existing][1]=spawn[j][1];
				virus_existing ++;
				distance_checked[virus_existing-1]=true;
				flag[virus_existing-1]=virus_existing-1;
				spawn_timer[j]+=virus_timer*virus_existing;
			}
		}
		for (int k=0; k<virus_existing; k++){
			virus.drawCentered(spawned_virus[k][0], spawned_virus[k][1]);
		}
		for (int z=0; z<red_alive; z++){
			red.drawCentered(red_location[z][0], red_location[z][1]);
		}

		p1.drawCentered(p1X, p1Y);
		p2.drawCentered(p2X, p2Y);
		g.drawString("Heart Rate: "+(int)(heart_beat+(beat_changes*(70-red_alive))) 
				+", Player 1 Virus Kills: "+p1_slain+", Player 2 Virus Kills: "+p2_slain, 50, 50);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		
		Input input=gc.getInput();
		if (virus_timer >100)
			virus_timer-=0.3f;
		//System.out.println(virus_timer);
		temp_timer --;
		//System.out.println(temp_timer);
		if(red_alive!=70&&temp_timer <=0&&red_alive>10){
			//System.out.println(red_alive);
			red_location[red_alive][0]=0;
			red_location[red_alive][1]=lane_finder.nextInt(endY-64)+32;
			temp_timer+=red_respawn_timer;
			red_alive++;
			//System.out.println(red_alive);
			//System.out.println("respawned");
		}
		for (int i=0; i<wound_active; i++){
			spawn_timer[i] -=10;
		}
		for(int j=0; j<virus_existing; j++){
			//spawned_virus[j][0]+=flow_speed;
			if (spawned_virus[j][0]>=endX-16)
				spawned_virus[j][0]=endX-16;
			if (spawned_virus[j][0]<16)
				spawned_virus[j][0]=16;
			if (spawned_virus[j][1]>=endY-16)
				spawned_virus[j][1]=endY-16;
			if (spawned_virus[j][1]<16)
				spawned_virus[j][1]=16;
			if(((p1X-spawned_virus[j][0])*(p1X-spawned_virus[j][0])
					+(p1Y-spawned_virus[j][1])*(p1Y-spawned_virus[j][1]))<32*32){
				for (int y=j; y<virus_existing-1; y++){
					spawned_virus[y][0]=spawned_virus[y+1][0];
					spawned_virus[y][1]=spawned_virus[y+1][1];
				}
				virus_existing--;
				p1_slain++;
			}
			if(((p2X-spawned_virus[j][0])*(p2X-spawned_virus[j][0])
					+(p2Y-spawned_virus[j][1])*(p2Y-spawned_virus[j][1]))<32*32){
				for (int y=j; y<virus_existing-1; y++){
					spawned_virus[y][0]=spawned_virus[y+1][0];
					spawned_virus[y][1]=spawned_virus[y+1][1];
				}

				virus_existing--;
				p2_slain++;
			}
			if (flag[j]>=0&&flag[j]<red_alive&&distance_checked[j]){
				float min=((spawned_virus[j][0]-red_location[flag[j]][0])*(spawned_virus[j][0]-red_location[flag[j]][0])
						+(spawned_virus[j][1]-red_location[flag[j]][1])*(spawned_virus[j][1]-red_location[flag[j]][1]));

				float speed = 0;
				float prop = 0;
				if ((spawned_virus[j][0]-red_location[flag[j]][0])>0)
					speed = virus_speed-flow_speed;
				else
					speed = virus_speed+flow_speed;
				prop = (spawned_virus[j][0]-red_location[flag[j]][0]) / (spawned_virus[j][1]-red_location[flag[j]][1]);
				if (prop >1 || prop < -1)
					prop=1/prop;
				spawned_virus[j][0]-=(spawned_virus[j][0]-red_location[flag[j]][0])*speed*prop/Math.sqrt(min);
				
				spawned_virus[j][1]-=(spawned_virus[j][1]-red_location[flag[j]][1])*virus_speed*(1-prop)/Math.sqrt(min);
				distance_checked[j]=false;
				//System.out.println("speed!" + speed*prop);	
			}
			else
				distance_checked[j]=false;

			if(!distance_checked[j]){
			for (int x=0; x<red_alive; x++){
				float min=1000000;
				if(((spawned_virus[j][0]-red_location[x][0])*(spawned_virus[j][0]-red_location[x][0])
						+(spawned_virus[j][1]-red_location[x][1])*(spawned_virus[j][1]-red_location[x][1]))<min){
					min=((spawned_virus[j][0]-red_location[x][0])*(spawned_virus[j][0]-red_location[x][0])
							+(spawned_virus[j][1]-red_location[x][1])*(spawned_virus[j][1]-red_location[x][1]));
					minX=red_location[x][0];
					minY=red_location[x][1];
					if (j<red_alive-1 && (flag[j]==flag[j+1]))
					{		flag[j]=j; flag[j+1]=j+1;
					}
					distance_checked[j]=true;
					//System.out.println("inside loop");
				}
				
				
				//System.out.println(spawned_virus[j][0]+ " "+minX+" "+Math.sqrt(min)+" "+(spawned_virus[j][0]-minX)/Math.sqrt(min));
				
			}}	
		}
		for (int k=0; k<red_alive; k++){
			red_location[k][0]+=red_speed+flow_speed;
			if(red_location[k][0]>endX){
				red_location[k][0]=0;
				//red_location[k][1]=blood_vessel[lane_finder.nextInt(active_vessels)];
				red_location[k][1]=lane_finder.nextInt(endY-64)+32;
			}
			for (int z=0; z<virus_existing; z++){
				
				if(((red_location[k][0]-spawned_virus[z][0])*(red_location[k][0]-spawned_virus[z][0])
						+(red_location[k][1]-spawned_virus[z][1])*(red_location[k][1]-spawned_virus[z][1]))<32*32){
					for (int y=k; y<red_alive-1; y++){
						red_location[y][0]=red_location[y+1][0];
						red_location[y][1]=red_location[y+1][1];
						//System.out.println(red_location[0][1]);
					}
					flag[z]=0;
					distance_checked[z]=false;
					red_alive-=1;
				}
			}
		}
		for (int i=0; i<red_alive; i++){
			if(((p1X-red_location[i][0])*(p1X-red_location[i][0])
					+(p1Y-red_location[i][1])*(p1Y-red_location[i][1]))<32*32){
				
				collision[i]=0;
			}
			else{
				collision[i]=1;
			}
			if (collision[i]==0){
				red_location[i][0]+=(p1X-p1X_temp);
				red_location[i][1]+=(p1Y-p1Y_temp);
				p1X_temp=p1X;
				p1Y_temp=p1Y;
				collision[i]=1;
			}
			if(((p2X-red_location[i][0])*(p2X-red_location[i][0])
					+(p2Y-red_location[i][1])*(p2Y-red_location[i][1]))<32*32){
				collision[i]=2;
				/*red_location[i][0]=(float) ((red_location[i][0]*32)/Math.sqrt(((p2X-red_location[i][0])*(p2X-red_location[i][0])
						+(p2Y-red_location[i][1])*(p2Y-red_location[i][1]))));
				red_location[i][1]=(float) ((red_location[i][1]*32)/Math.sqrt(((p2X-red_location[i][0])*(p2X-red_location[i][0])
						+(p2Y-red_location[i][1])*(p2Y-red_location[i][1]))));*/
			}
			else{
				collision[i]=3;
			}
			if (collision[i]==2){
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
		heart_changing();
	}
	
	public int getID(){
		return 1;
	}
	
}

