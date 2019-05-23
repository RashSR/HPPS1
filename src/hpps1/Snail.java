package hpps1;
import net.canarymod.api.world.position.Location;
import java.util.ArrayList;
import net.canarymod.api.world.blocks.BlockType;

public class Snail extends Enemy{
	protected static ArrayList<Snail> allSnails;
	private static final int HEALTH=20;
	private static final int DAMAGE_GIVE=5;
	private static final int DAMAGE_TAKE=10;

	protected static void initSnails(){
		allSnails = new ArrayList<>();
		allSnails.add(new Snail(HEALTH, new Location(-322, 79, 359), DAMAGE_GIVE, DAMAGE_TAKE));
		for(Snail s : allSnails){
			s.changeEnemy(BlockType.Mycelium);
		}
	}
	protected Snail(int health, Location loc, int damageGive, int damageTake){
		super(health, loc, damageGive, damageTake);
	}
	protected void walk(){
		//TODO: ServerTask
	}
	protected void killEnemy(){
		changeEnemy(BlockType.Air);
	}
	private void changeEnemy(BlockType bT){
		this.getEnemyLocation().getWorld().setBlockAt(this.getEnemyLocation(), bT);
	}
	public void getHitBySpell(Spell spell, Spieler spieler){
		if(spell == Spell.FLIPENDO){
			this.setHealth(this.getHealth()-this.getDamageTake());
		} else if(spell == Spell.DOUBLEFLIPENDO){
			this.setHealth(this.getHealth()-this.getDamageTake()*2);
		}
	}
	protected static Snail checkIfSnail(Location loc){
		for(Snail s : allSnails){
			if(locEqual(s.getEnemyLocation(), loc)){
				return s;
			}
		}
		return null;
	}
}