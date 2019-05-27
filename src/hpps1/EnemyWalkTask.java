package hpps1;
import net.canarymod.Canary;
import net.canarymod.logger.Logman;
import net.canarymod.tasks.ServerTask;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.BlockType;
public class EnemyWalkTask extends ServerTask{
	private final EZPlugin plugin;
	public static Logman logger;
	private Enemy enemy;
	private int count = 0;
	public EnemyWalkTask(EZPlugin plugin, Enemy enemy) {
        super(Canary.getServer(), 15, true);
        logger = plugin.getLogman();
        this.plugin = plugin;
        this.enemy=enemy;
    }

    protected void killTask(){
    	Canary.getServer().removeSynchronousTask(this);
    }

    public void run(){
    	if(enemy instanceof Snail){
    		Snail snail = (Snail)enemy;
    		if(snail.getHealth()>0){
    			Location loc=snail.getEnemyLocation();
    			//TODO: pattern im stile von snailGoPath(Direction dir, width, length)!
    			BlockType check=loc.getWorld().getBlockAt((int)loc.getX()+1, (int)loc.getY(), (int)loc.getZ()).getType(); //Blockcheck für alle Richtungen 
    			if(check==BlockType.Air || check==BlockType.Mycelium){ 
    				snail.changeEnemy(BlockType.Air);
    				if(count<5){
    					snail.move(Direction.POS_X);
    				} else if(count<9){
    					snail.move(Direction.NEG_Z);
    				} else if(count<14){
    					snail.move(Direction.NEG_X);
    				} else{
    					snail.move(Direction.POS_Z);
    					if(count == 17){
    						count = -1;
    					}
    				} 
    			} else {
    				logger.info("Snail ist gegen Block gelaufen.");
    				snail.changeEnemy(BlockType.Air);
    				killTask();
    			}
    		} else{
    			killTask();
    		}
    	}
    	count++;
	}
}