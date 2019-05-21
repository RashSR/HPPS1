package hpps1;

public enum HogwartsHouse{
	//Enum über alle vier Hogwartshäuser 
	GRYFFINDOR("Gryffindor"),
	RAVENCLAW("Ravenclaw"),
	SLYTHERIN("Slytherin"),
	HUFFLEPUFF("Hufflepuff");

	private String name;

	HogwartsHouse(String name){
		this.name=name;
	}

	public String getHogwartsHouse(){
		return this.name;
	}
}