package hpps1;
import net.canarymod.api.world.position.Location;

abstract class Enemy implements Spellable{
	protected int health;
	protected Location enemyLocation;

	protected Enemy(int health, Location loc){
		this.health=health;
		this.enemyLocation=loc;
	}

	protected abstract void initEnemy();
	protected abstract void walk();

}