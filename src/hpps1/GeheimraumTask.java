package hpps1;
import net.canarymod.logger.Logman;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.tasks.ServerTask;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.World;
import net.canarymod.api.world.effects.SoundEffect;

public class GeheimraumTask extends ServerTask{
    private Player player;
    private int halfsec=0;
    private final EZPlugin plugin;
    public static Logman logger;

    public GeheimraumTask(Player player, EZPlugin plugin) {
        super(Canary.getServer(), 10, true);
        HPPS1.noSRopen=false;
        this.player = player;
        this.plugin = plugin;
        logger = plugin.getLogman();
    }

    public void stoppSecretRoom(){
        Canary.getServer().removeSynchronousTask(this);
    }

    public void run(){
        BlockType luft = BlockType.Air;
        BlockType buecherregal = BlockType.Bookshelf;

        Location rechterblockoben = new Location(-365, 68, 294);
        Location rechterblockunten = new Location(-365, 67, 294);
        Location linkerblockunten = new Location(-367, 67, 294);
        Location linkerblockoben = new Location(-367, 68, 294);
        World world = rechterblockunten.getWorld();
        SoundEffect closeSecretRoom = new SoundEffect(SoundEffect.Type.CHEST_CLOSED, rechterblockunten.getX(), rechterblockunten.getY(), rechterblockunten.getZ(), 0.5f, 0.3f);
        
        if(halfsec == 0){
            plugin.playSound(rechterblockunten, SoundEffect.Type.CHEST_OPEN, 0.5f, 0.3f);
        	logger.info("Geheimraum 1 wurde von "+player.getDisplayName()+ " geoeffnet.");
            world.setBlockAt(rechterblockunten, luft);
            world.setBlockAt(rechterblockoben, luft);
            world.setBlockAt(linkerblockoben, luft);
            world.setBlockAt(linkerblockunten, luft);
            
            world.setBlockAt((int)rechterblockoben.getX(),(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, buecherregal);
            world.setBlockAt((int)rechterblockunten.getX(),(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, buecherregal);
            world.setBlockAt((int)linkerblockunten.getX(), (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, buecherregal);
            world.setBlockAt((int)linkerblockoben.getX(), (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, buecherregal);
        }
        if(halfsec == 1){
            world.setBlockAt((int)rechterblockoben.getX(),(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, luft);
            world.setBlockAt((int)rechterblockunten.getX(),(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, luft);
            world.setBlockAt((int)linkerblockunten.getX(), (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, luft);
            world.setBlockAt((int)linkerblockoben.getX(), (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, luft);

            world.setBlockAt((int)rechterblockoben.getX() - 1,(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, buecherregal);
            world.setBlockAt((int)rechterblockunten.getX() - 1 ,(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, buecherregal);
            world.setBlockAt((int)linkerblockunten.getX() + 1, (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, buecherregal);
            world.setBlockAt((int)linkerblockoben.getX() + 1, (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, buecherregal);
        }
        if(halfsec == 60){
            world.playSound(closeSecretRoom);
            world.setBlockAt((int)rechterblockoben.getX() - 1,(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, luft);
            world.setBlockAt((int)rechterblockunten.getX() - 1 ,(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, luft);
            world.setBlockAt((int)linkerblockunten.getX() + 1, (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, luft);
            world.setBlockAt((int)linkerblockoben.getX() + 1, (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, luft);

            world.setBlockAt((int)rechterblockoben.getX(),(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, buecherregal);
            world.setBlockAt((int)rechterblockunten.getX(),(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, buecherregal);
            world.setBlockAt((int)linkerblockunten.getX(), (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, buecherregal);
            world.setBlockAt((int)linkerblockoben.getX(), (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, buecherregal);
        }
        if(halfsec == 61){
            world.setBlockAt((int)rechterblockoben.getX(),(int)rechterblockoben.getY(),(int)rechterblockoben.getZ() -1, luft);
            world.setBlockAt((int)rechterblockunten.getX(),(int)rechterblockunten.getY(),(int)rechterblockunten.getZ() - 1, luft);
            world.setBlockAt((int)linkerblockunten.getX(), (int)linkerblockunten.getY(),(int)linkerblockunten.getZ() + 1, luft);
            world.setBlockAt((int)linkerblockoben.getX(), (int)linkerblockoben.getY(), (int)linkerblockoben.getZ() + 1, luft);

            world.setBlockAt(rechterblockunten, buecherregal);
            world.setBlockAt(rechterblockoben, buecherregal);
            world.setBlockAt(linkerblockoben, buecherregal);
            world.setBlockAt(linkerblockunten, buecherregal);
            HPPS1.noSRopen=true;
            this.stoppSecretRoom();
        }
        halfsec++;
    }
}