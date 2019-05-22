package hpps1;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.entity.living.humanoid.Player;
import java.util.ArrayList;
import net.canarymod.api.world.position.Location;

public class Cobweb extends EZPlugin implements Spellable{
	private ArrayList<Location> containedLocs = new ArrayList<>();
	protected static ArrayList<Cobweb> allCobWebs;

	public Cobweb(ArrayList<Location> containedLocs){
		this.containedLocs = containedLocs;
	}

	protected void initCobWebs(){
		this.allCobWebs = new ArrayList<>();
	}

	@Override
	public void hitBySpell(Spell spell, Spieler spieler){

	}
}