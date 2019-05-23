package hpps1;
import net.canarymod.Canary;
import net.canarymod.commandsys.*;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.api.entity.living.humanoid.Player;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.canarymod.chat.ChatFormat;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.api.world.Chunk;
import net.canarymod.hook.world.ChunkLoadedHook;
import net.canarymod.hook.world.ChunkUnloadHook;
import net.canarymod.api.world.World;
import java.util.HashMap;
import java.util.ArrayList;

public class HPPS1 extends EZPlugin implements PluginListener {
	protected String msg1 = "[HP-PS1] ";
  	private int gemeinschaftsraumnachrichtenzahl = 1;
  	private int zeitungsartikelzahl = 1;
	private int taskzahl = 1;
	private int clicks;
	private boolean click4air = false;
	public final int CHUNK_SIZE = 16;
	public HashMap<Integer, BuecherregelEule1> all = new HashMap<>();
	private Spieler sp;
	private double posX;
	private double posY;
	private double posZ;
	public static boolean noSRopen=true;
	//TODO: /hpps1 nur tpt nur zum beginn -> /hpps1load tpt zum speicherpunkt evtl laderaum?
	private int blockMoveClicks=0;

  	@Override
    public boolean enable() {  
    	Canary.hooks().registerListener(this, this);
    	return super.enable(); 
    }

    private void thisSetPlayer(Player player){
    	if(sp == null || !(sp.getPlayer().getDisplayName().equals(player.getDisplayName()))){
    		sp = new Spieler(player);
    		sp.loadSaveState(false);
    	} 
    }

	@Command(aliases = {"hpps1"},
            description = "Teleportiert den Spieler zu HPPS1.",
            permissions = {""},
            toolTip = "/hpps1")
	public void hpps1(MessageReceiver caller, String[] parameters){
    	if (caller instanceof Player){
			Player player = (Player)caller;
			thisSetPlayer(player);
			FlipendoBlock.initFlipendoBlocks();
			Cobweb.initCobWebs();
			Snail.initSnails();
			if(sp.loadSaveState(true)){
				posX = sp.getPlayer().getX();
				posY = sp.getPlayer().getY();
				posZ = sp.getPlayer().getZ();
			} else {
				posX=-350.0;
				posY=64.0;
				posZ=264.0;
			}
      		Location startloc = new Location(posX, posY, posZ);
      		player.teleportTo(startloc);
        }
  	}

	@Command(aliases = {"hpps1stats"},
            description = "Zeigt dem Spieler seinen HPPS1 Speicherstand.",
            permissions = {""},
            toolTip = "/hpps1stats")
	public void statshpps1Command(MessageReceiver caller, String[] args){
    	if(caller instanceof Player){
    		Player player = (Player)caller;
    		thisSetPlayer(player);
			Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Speicherstand von "+ChatFormat.BLUE+player.getDisplayName()+ChatFormat.DARK_GREEN + ":");
    		Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_GREEN+" - Karten gefunden: "+ChatFormat.GOLD+sp.getCards()+ChatFormat.DARK_GREEN+"/"+Spieler.maxCards+"\n");	
		}
 	}

	@HookHandler
  	public void saveBooks(BlockRightClickHook event){
  		Block eB = event.getBlockClicked();
  		if(eB.getType()==BlockType.EnchantmentTable){
  			event.setCanceled();
  			thisSetPlayer(event.getPlayer());
			sp.saveSaveState(true);
			Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Spieldaten erfolgreich gespeichert.");
  		}
  	}


 @Command(aliases = { "fackelflieg" },
            description = "hpps1 plugin",
            permissions = { "*" },
            toolTip = "/fackelflieg")
 	public void fackelfliegCommand(MessageReceiver caller, String[] args) {
    	if (caller instanceof Player) { 
      		Player player = (Player)caller;
      		if(clicks > 0){
        		return;
      		}
      		if(args.length == 1){
        		clicks = 1;
       			click4air = true;
        		Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Sie koennen ingesamt "+ChatFormat.GOLD+clicks+ChatFormat.DARK_GREEN+" Mal klicken.");
      		}
      		if(args.length == 2){
      			int eingegebenezahl = Integer.valueOf(args[1]);
        		click4air = true;
        		if(eingegebenezahl > 0){
        			clicks = eingegebenezahl;
          			Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Sie koennen ingesamt "+ChatFormat.GOLD+clicks+ChatFormat.DARK_GREEN+" Mal klicken.");
        		}else{
          			Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Bitte positive Zahl angeben!");
          			return;
        		}
    		}
    		if(args.length > 2){
    			Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA+msg1+ChatFormat.DARK_GREEN+"Zu viele Argumente!");
    		}
    	}
	}

    @HookHandler
    public void startmovebookshelf(ChunkLoadedHook event){
    	World world = event.getWorld();
      	Chunk chunk = event.getChunk();
      	if(chunk.getX()*CHUNK_SIZE >= -330 && chunk.getX()*CHUNK_SIZE <= -310){
        	if(chunk.getZ()*CHUNK_SIZE >= 360 && chunk.getZ()*CHUNK_SIZE <= 390){
          		if(taskzahl == 1){
        			BuecherregelEule1 task = new BuecherregelEule1(world);  
        			logger.info("Buecherregal bewegt sich.");
        			Canary.getServer().addSynchronousTask(task);
        			all.put(taskzahl, task);
        			taskzahl++;
          		}else{
            		taskzahl = 1;
                }
            }
      	}
    }

    @HookHandler
    public void stoppmovebookshelf(ChunkUnloadHook event){
      	Chunk chunk = event.getChunk();
      	World world = event.getWorld();
      	if(chunk.getX()*CHUNK_SIZE >= -330 && chunk.getX()*CHUNK_SIZE <= -310){
        	if(chunk.getZ()*CHUNK_SIZE >= 360 && chunk.getZ()*CHUNK_SIZE <= 390){
          		if(all.size()>0){
          			logger.info("Buecherregal bewegt sich nicht mehr.");
          			all.get(1).stopIt();
          			all.remove(1);
        		}
        	}
      	}
    }

  @HookHandler
  public void geheimraumoeffner(BlockRightClickHook event){
  	Block geklickterblock = event.getBlockClicked();
    Player player = event.getPlayer();
    if(click4air){
    	geklickterblock.getWorld().setBlockAt(geklickterblock.getLocation(), BlockType.Air);
      	clicks = clicks - 1;
      	if(clicks < 1){
        	click4air = false;
        }
      	return;
    }else{
    	if(geklickterblock.getType() == BlockType.Bookshelf && noSRopen){
        	if(geheimraum1(geklickterblock.getX(), geklickterblock.getY(), geklickterblock.getZ())){
        		opengeheimraum1(player);
    			thisSetPlayer(player);
    			sp.foundCard(1);	
        		}
    		}
    	}
	}

	public boolean geheimraum1(double x, double y, double z){
    	if(x >= -367 && x <= -365 && y >= 67 && y <= 68 && z == 294){
      		return true;
    	}
    	return false;
  	}

  	public void opengeheimraum1(Player player){
    	Canary.getServer().addSynchronousTask(new GeheimraumTask(player, this));
   	}
  
  	@HookHandler
 	public void schwarzesbrettgemeinschaftsraum(BlockRightClickHook event){
  		Block geklickterblock = event.getBlockClicked();
    	if(geklickterblock.getType() == BlockType.WallSign && geklickterblock.getX() == -347 && geklickterblock.getZ() == 298){
    		gemeinschaftsraumnachrichten();
    	}
	}

	public void gemeinschaftsraumnachrichten(){
    	String msg="";
    	if(gemeinschaftsraumnachrichtenzahl == 1){
        	msg="Bitte haltet den Gemeinschaftsraum"+ChatFormat.GOLD+"sauber"+ChatFormat.DARK_GREEN+"! \n" ;
        }
     	if(gemeinschaftsraumnachrichtenzahl == 2){
         	msg="Zu verkaufen: \n \n Ausgabe 1-6 von "+ChatFormat.GOLD+"Abenteuer von Martin Miggs, dem mickrigen Muggel"+ChatFormat.DARK_GREEN+". Drei Sickel pro Stueck.\n";
        }
     	if(gemeinschaftsraumnachrichtenzahl == 3){
          	msg=ChatFormat.GOLD+"Verloren"+ChatFormat.DARK_GREEN+": Eine Kroete. \n Der Finder wird gebeten, sie bei "+ChatFormat.GOLD+"Neville Longbottom"+ChatFormat.DARK_GREEN + "abzugeben.\n";
        }
     	if(gemeinschaftsraumnachrichtenzahl == 4){
          	msg="Zaubertrankunterricht,"+ChatFormat.GOLD+" 1.Stunde:"+ChatFormat.DARK_GREEN+" Bitte ein "+ChatFormat.GOLD+"Faultierhirn"+ChatFormat.DARK_GREEN+" mitbringen.\n";
        }
     	if(gemeinschaftsraumnachrichtenzahl == 5){
          	msg=ChatFormat.GOLD+"Jetzt erhaeltlich!"+ChatFormat.DARK_GREEN+"Der "+ChatFormat.GOLD+"Nimbus 2000"+ChatFormat.DARK_GREEN+"!\n";
        }
     	Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA + msg1 + ChatFormat.DARK_GREEN + msg);
     	if(gemeinschaftsraumnachrichtenzahl <= 5){
     		gemeinschaftsraumnachrichtenzahl = gemeinschaftsraumnachrichtenzahl + 1;
        }
     	if(gemeinschaftsraumnachrichtenzahl > 5){
      		gemeinschaftsraumnachrichtenzahl = 1;
     	}
	}

  	@HookHandler
	public void tagesprohetartikel(BlockRightClickHook event){
    	Block geklickterblock = event.getBlockClicked();
     	if(geklickterblock.getType() == BlockType.WhiteCarpet && geklickterblock.getX() == -346 && geklickterblock.getZ() == 303){
      		String msg = "";
      		if(zeitungsartikelzahl == 1){
        		msg=ChatFormat.GOLD+"Neues vom Einbruch bei Gringotts:"+ChatFormat.DARK_GREEN+"\n\nDie Ermittlungen im Fall des Einbruchs bei Gringotts werden fortgesetzt. Allgemein wird vermutet, dass es sich um die Tat"+ChatFormat.GOLD+"schwarzer Magier"+ChatFormat.DARK_GREEN+"handelt.\n";
            }
      		if(zeitungsartikelzahl == 2){
        		msg="Vertreter der Kobolde bei Gringotts bekraefrigen heute noch einmal, dass "+ChatFormat.GOLD+"nichts"+ChatFormat.DARK_GREEN+" gestohlen wurde. Der "+ChatFormat.GOLD+"unbekannte Inhalt"+ChatFormat.DARK_GREEN+"des Verlieses wurde bereits vorher entfernt.\n";
    		}
      		Canary.instance().getServer().broadcastMessage(ChatFormat.DARK_AQUA + msg1 + ChatFormat.DARK_GREEN + msg);
      		if(zeitungsartikelzahl <= 2){
        		zeitungsartikelzahl = zeitungsartikelzahl + 1;
        	}
      		if(zeitungsartikelzahl > 2){
        		zeitungsartikelzahl = 1;
        	}
       	}
    }
}