package hpps1;
import net.canarymod.api.world.position.Location;
import com.pragprog.ahmine.ez.EZPlugin;

abstract class Enemy extends EZPlugin implements Spellable{
	protected int health;
	protected Location enemyLocation;
	protected int damageTake;
	protected int damageGive;

	protected Enemy(int health, Location loc, int damageGive, int damageTake){
		this.health=health;
		this.enemyLocation=loc;
		this.damageTake=damageTake;
		this.damageGive=damageGive;
	}

	protected abstract void walk();
	protected abstract void killEnemy();
	protected void setHealth(int health){
		this.health=health;
		logger.info("Snail hat jetzt "+this.health+" HP");
		if(health <=0){
			this.killEnemy();
		}
	}
	protected int getHealth(){
		return this.health;
	}
	protected int getDamageGive(){
		return this.damageGive;
	}
	protected int getDamageTake(){
		return this.damageTake;
	}
	protected Location getEnemyLocation(){
		return this.enemyLocation;
	}
	protected void setEnemyLocation(Location enemyLocation){
		this.enemyLocation=enemyLocation;
	}

}