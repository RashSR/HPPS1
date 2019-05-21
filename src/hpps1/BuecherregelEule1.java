package hpps1;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.tasks.ServerTask;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.World;

public class BuecherregelEule1 extends ServerTask {

    private World world;
    private int sek = 0; //zählt durchläufe
    //Konstruktor
	public BuecherregelEule1(World myworld) {
    	super(Canary.getServer(), 20, true);
        world = myworld;
    }
    //Entfernt diesen Task
    public void stopIt(){
        Canary.getServer().removeSynchronousTask(this);
    }
    //Versetzt das Bücherregal in eine endlose Auf- und Abbewegung
    public void run(){
    	BlockType luft = BlockType.Air;
        BlockType buecherregal = BlockType.Bookshelf;
        Location rechterblockoben = new Location(4, 4, 4);
        Location rechterblockunten = new Location(5, 5, 5);
        Location[] bloecke = new Location[12];
        int f=0;
        if(sek == 0){
        	for(int i=0;i<4;i++){
            	for(int j=0;j<3;j++){
                	bloecke[f]= new Location(-331+i, 63, 369+j);
                	world.setBlockAt(bloecke[f], luft);
                	world.setBlockAt(-331+i, 66, 369+j, buecherregal);
                	f++;
            	}
        	}
    	}
    	if(sek == 1){
        	for(int i=0;i<4;i++){
            	for(int j=0;j<3;j++){
                	bloecke[f]= new Location(-331+i, 63, 369+j);
                	world.setBlockAt(bloecke[f], buecherregal);
                	world.setBlockAt(-331+i, 66, 369+j, luft);
                	f++;
    			}
			}
        	sek = 0;
        	return;
		}
    	sek++;
        world.setBlockAt(rechterblockunten, luft);
        world.setBlockAt((int)rechterblockoben.getX(),(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, buecherregal);
    }
}