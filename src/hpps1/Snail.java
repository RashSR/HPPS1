package hpps1;
import net.canarymod.api.world.position.Location;
import java.util.ArrayList;
import net.canarymod.Canary;
import net.canarymod.api.world.blocks.BlockType;

public class Snail extends Enemy{
	protected static ArrayList<Snail> allSnails;
	private static final int HEALTH=20;
	private static final int DAMAGE_GIVE=5;
	private static final int DAMAGE_TAKE=10;

	protected static void initSnails(){
		allSnails = new ArrayList<>();
		allSnails.add(new Snail(HEALTH, new Location(-337, 79, 362), DAMAGE_GIVE, DAMAGE_TAKE));
		for(Snail s : allSnails){
			s.changeEnemy(BlockType.Mycelium);
			s.walk();
		}
	}
	protected Snail(int health, Location loc, int damageGive, int damageTake){
		super(health, loc, damageGive, damageTake);
	}
	protected void walk(){
		Canary.getServer().addSynchronousTask(new EnemyWalkTask(this, this));
	}
	protected void move(Direction dir){
		Location loc = getEnemyLocation();
		if(dir==Direction.POS_X){
			this.setEnemyLocation(new Location(loc.getX()+1, loc.getY(), loc.getZ()));
		}else if(dir==Direction.NEG_X) {
			this.setEnemyLocation(new Location(loc.getX()-1, loc.getY(), loc.getZ()));
		}else if(dir==Direction.POS_Z){
			this.setEnemyLocation(new Location(loc.getX(), loc.getY(), loc.getZ()+1));
		}else {
			this.setEnemyLocation(new Location(loc.getX(), loc.getY(), loc.getZ()-1));
		}
		this.changeEnemy(BlockType.Mycelium); 
	}
	protected void killEnemy(){
		changeEnemy(BlockType.Air);
	}
	protected void changeEnemy(BlockType bT){
		this.getEnemyLocation().getWorld().setBlockAt(this.getEnemyLocation(), bT);
	}
	public void getHitBySpell(Spell spell, Spieler spieler){
		if(spell == Spell.FLIPENDO){
			this.setHealth(this.getHealth()-this.getDamageTake());
		} else if(spell == Spell.DOUBLEFLIPENDO){
			this.setHealth(this.getHealth()-this.getDamageTake()*2);
		}
		if(this.getHealth()<0){
			spieler.changeHousePoints(5);
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