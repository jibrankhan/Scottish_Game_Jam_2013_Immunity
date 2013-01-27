
public class SimpleVirus implements Virus {

	private float xCoordinate;
	private float yCoordinate;
	private float [][] red_blood_array;
	
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
	}

	@Override
	public void setY(float yCoordinate) {
		// TODO Auto-generated method stub
		this.yCoordinate = yCoordinate;
	}	
}
