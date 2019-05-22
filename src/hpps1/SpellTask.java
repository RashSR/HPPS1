package hpps1;
import net.canarymod.Canary;
import net.canarymod.logger.Logman;
import net.canarymod.tasks.ServerTask;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.world.effects.Particle.Type;
import net.canarymod.BlockIterator;
import net.canarymod.LineTracer;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.effects.Particle;
import net.canarymod.api.entity.living.humanoid.Player;
//Diese Klasse ermöglicht es den Zauber verzögert auszuführen
public class SpellTask extends ServerTask{
	private final EZPlugin plugin;
	public static Logman logger;
	private Spieler spieler;
	private BlockIterator sightItr;
	private Particle.Type pT;
	private Spell spell; //welcher zauber ausgeführt wird
	private int tick=0;
	private final int MAXTICKS=150; //gibt die zeit an ab wie viel ticks der Spieler wieder zaubern kann
	//Konstruktor EZPlugin notwendig für logger.info
	public SpellTask(EZPlugin plugin, Spieler spieler, Particle.Type pT, Spell spell) {
        super(Canary.getServer(), 1, true);
        this.plugin = plugin;
        this.spieler = spieler;
        this.pT = pT;
        this.spell = spell;
        logger = plugin.getLogman();
        sightItr = new BlockIterator(new LineTracer(spieler.getPlayer()), true);
    }

    //itterriert reihenweise durch blöcke vor dem Zauberer und überprüft ob diese Luft sind
	public void run(){
		if(tick > MAXTICKS){
			Canary.getServer().removeSynchronousTask(this);
			logger.info("Zauber ausserhalb Reichweite.");
			spieler.setIsSpellActive(true);
		}
		if(sightItr.hasNext()){
			Block b = sightItr.next();
        	Location blockLoc = b.getLocation();
        	EZPlugin.spawnParticle(blockLoc, pT);
        	//TODO: Soundeffect einfügen!  
        	if(b.getType() != BlockType.Air){
          		FlipendoBlock fB = Spieler.checkIfFlipendoBlock(blockLoc); //TODO: Zeile passt noch nicht evtl alle Blöcke checken?
          		if(fB!=null){ //TODO: hier mit || andere Sachen Checken 
          			fB.hitBySpell(spell, spieler.getPlayer());
          			spieler.setIsSpellActive(true);
          		}
          		spieler.setIsSpellActive(true);
          		Canary.getServer().removeSynchronousTask(this);
        	}
		}
		tick++;
	}
}
