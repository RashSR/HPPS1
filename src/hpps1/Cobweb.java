package hpps1;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.entity.living.humanoid.Player;
import java.util.ArrayList;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.BlockType;

public class Cobweb extends EZPlugin implements Spellable{
	private ArrayList<Location> containedLocs = new ArrayList<>();
	protected static ArrayList<Cobweb> allCobWebs;
	private final int HOUSEPOINTS=10;

	public Cobweb(ArrayList<Location> containedLocs){
		this.containedLocs = containedLocs;
	}

	protected static void initCobWebs(){
		allCobWebs = new ArrayList<>();
		ArrayList<Location> cB = new ArrayList<>();
		cB.add(new Location(-337, 80, 319));
		cB.add(new Location(-338, 79, 321));
		cB=makeLocList(cB);
		cB.add(new Location(-337, 81, 320));
		cB.add(new Location(-338, 81, 319));
		cB.add(new Location(-338, 81, 321));
		cB.add(new Location(-338, 81, 320));
		cB.add(new Location(-338, 82, 320));
		allCobWebs.add(new Cobweb(cB));
		for(Cobweb c : allCobWebs){
			c.changeWebs(BlockType.SpiderWeb);
		}

	}
	private ArrayList<Location> getContainedLocs(){
		return this.containedLocs;
	}

	protected static Cobweb checkIfCobWeb(Location loc){
		for(Cobweb c : allCobWebs){
			for(Location loc1 : c.getContainedLocs()){
				if(locEqual(loc1, loc)){
					return c;
				}
			}
		}
		return null;
	}

	private void changeWebs(BlockType bT){
		for(Location loc : this.containedLocs){
			loc.getWorld().setBlockAt(loc, bT);
		}
	}

	@Override
	public void getHitBySpell(Spell spell, Spieler spieler){
		this.changeWebs(BlockType.Air);
		spieler.changeHousePoints(HOUSEPOINTS);
	}
}