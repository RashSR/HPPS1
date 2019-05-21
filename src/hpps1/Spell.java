package hpps1;

public enum Spell {
	//Enum über alle verfügbaren Zauber im Spiel
	FLIPENDO("Flipendo"),
	DOUBLEFLIPENDO("Double Flipendo"),
	WINGARDIUMLEVIOSA("Wingardium-Leviosa"),
	VERDIMELLIOUS("Verdimellious"),
	AVIFORS("Avifors"),
	INCENDIO("Incendio");

	private String spell;

	Spell(String spell){
		this.spell=spell;
	}

	public String getSpell(){
		return this.spell;
	}
}