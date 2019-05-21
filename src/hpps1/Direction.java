package hpps1;

public enum Direction{
	//Enum über alle Himmelsrichtungen 
	POS_X(1),
	NEG_X(2),
	POS_Z(3),
	NEG_Z(4);

	private int direction;

	Direction(int direction){
		this.direction=direction;
	}

	public int getDirection(){
		return this.direction;
	}
}