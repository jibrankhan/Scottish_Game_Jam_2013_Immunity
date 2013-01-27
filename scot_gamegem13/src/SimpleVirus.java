
public class SimpleVirus implements Virus {

	final float virus_speed = (float) 1.5;
	private float xCoordinate;
	private float yCoordinate;
	private float [][] red_blood_array;
	private int red_alive =70;
	private int flag = -1;
	private float min = Float.MAX_VALUE;
	private float minX,minY;
	private float dist;
	public SimpleVirus(float xCoord, float yCoord, float [][] red_blood_array){
		
		this.xCoordinate = xCoord;
		this.yCoordinate = yCoord;
		this.red_blood_array = red_blood_array;
	}
	
	public void searchBloodCells(){
		
		for(int i = 0; i < red_blood_array.length; i++){
			
			for(int j = 0; j < 3; j++){
				
				System.out.print(red_blood_array[i][j]);
			}
		}
	}
	
	@Override
	public float getX() {
		
		return xCoordinate;
	}

	@Override
	public float getY() {
		
		return yCoordinate;
	}

	@Override
	public void setX(float xCoordinate) {
		
		this.xCoordinate = xCoordinate;
		xCoordinate+=virus_speed;
	}

	@Override
	public void setY(float yCoordinate) {
		
		this.yCoordinate = yCoordinate;
	}
	
	public void setRedLocation(float[][] red_location){
	
		this.red_blood_array = red_location;
	}
	
	public void computeShortestDistance(){
		for (int i=0; i<red_alive; i++){
			dist = (xCoordinate-red_blood_array[i][0])*(xCoordinate-red_blood_array[i][0])
			+(yCoordinate-red_blood_array[i][1])*(yCoordinate-red_blood_array[i][1]);
			if(dist<min){
				System.out.println(min);
				min = dist;
				System.out.println(min);
				minX=red_blood_array[i][0];
				minY=red_blood_array[i][1];
				flag = i;
			}
		}
	}
	public void chasing(){
		if (flag != -1){
			//System.out.println(((xCoordinate-minX))/Math.sqrt(min));
			//yCoordinate+=((yCoordinate-minY))/Math.sqrt(min);
			//System.out.println(minX+" "+minY);
			flag=-1;
			min=Float.MAX_VALUE;
		}
	}
	// When collision happens with red cell
	public void setRed (int red_alive){
		this.red_alive=red_alive;
		flag=-1;
	}
	
}
