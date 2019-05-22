package hpps1;
import net.canarymod.api.entity.living.humanoid.Player;
import java.util.HashMap;
import net.canarymod.Canary;
import net.canarymod.database.exceptions.*;
import net.canarymod.database.Database;
import com.pragprog.ahmine.ez.EZPlugin;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.effects.SoundEffect;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.effects.Particle.Type;
import net.canarymod.api.world.effects.Particle;

public class Spieler extends EZPlugin implements PluginListener{
	protected static final int maxCards = 18;
	private Player player;
	//Database
	private boolean[] found_cards; //Speichert die gefundenen Zauberkarten in Array
	private HashMap<String, Object> search;
	private boolean haveSaveState;
	private SaveStates ss;
	//Spells
	private boolean gotFlipendo=true;
	private boolean gotWingardiumLeviosa;
	private boolean gotIncendio;
	private boolean gotVerdimellious;
	private boolean gotAvifors;
	private boolean gotDoubleFlipendo; //für alle roten Bohnen
	private boolean isSpellActive=true; //falls spieler gerade gezaubert hat kann er nicht spammen true=zaubern möglich 
	//Beans
	private int yellowBeans; //max 50
	private int redBeans;    //max 100
	private int greenBeans;  //max 80
	private int blueBeans;   //max 70
	//Quidditch
	private boolean gotQuidditchArmor; // für alle grünen Bohnen
	private boolean gotNimbus2000; // für alle gelben Bohnen
	//TODO: gefundene Bohnen
	//TODO: Häuserwahl + punkte sammeln

	//Manuell aufrufen, da sonst nur bei Hauptklasse aufgerufen wird
	@Override
    public boolean enable() {  
    	Canary.hooks().registerListener(this, this);
    	return super.enable();
    }
    //Constructor
    public Spieler(Player player){
		this.player = player;
		this.found_cards = new boolean[maxCards];
		this.search = new HashMap<>();
		this.haveSaveState = false;
		this.ss = new SaveStates();
		logger.info("Der Spieler "+player.getDisplayName()+" wurde angelegt.");
		enable();
	}
	protected void setIsSpellActive(boolean bool){
		this.isSpellActive=bool;
	}

	//Falls Karte noch nicht gefunden wurde, wird bool auf true gesetzt und gespeichert
	protected void foundCard(int secretRoom){
		if(secretRoom > 0 && secretRoom < maxCards){
			this.found_cards[secretRoom-1]=true;
			this.saveSaveState(false);
		} else {
			logger.info("Kartenindex falsch");
		}
	}
	//berechnet Anzahl gefundener Karten
	protected int getCards(){
    	int cards = 0;
    	for(Boolean bool : found_cards){
    		if(bool){
        		cards++;
        	}
    	}
    	return cards;
  	}

	protected boolean getHaveSaveState(){
		return this.haveSaveState;
	}

	protected Player getPlayer(){
		return this.player;
	}
	//Lässt den Spieler per Rechtsklick auf spezifisches Item "zaubern"
	@HookHandler
  	public void castSpell(ItemUseHook event){
  		if(this.gotFlipendo&&this.isSpellActive){
  			this.isSpellActive=false;
  			Particle.Type pT=null;
  			Spell spell = null;
  			if(FlipendoBlock.fBlocks != null){
  				if(event.getItem().getType() == ItemType.Stick){
  					spell = Spell.FLIPENDO;
  					pT = Particle.Type.FIREWORKS_SPARK;
  				} //TODO: hier über else if weitere spell und partikel setzen
  			}else {
  				logger.info("Noch keine FlipendoBlocks vorhanden.");
  				return;
  			}
  			Canary.getServer().addSynchronousTask(new SpellTask(this, this, pT, spell));
    	}
  	}

  	//Überprüft ob getroffener Block ein FlipendoBlock ist und gibt diesen zurück
	protected static FlipendoBlock checkIfFlipendoBlock(Location loc){
  		for(FlipendoBlock fB : FlipendoBlock.fBlocks){
  			for(Location loc1 : fB.getContainedBlocks()){
  				if(locEqual(loc, loc1)){
  					return fB;
  				}
  			}
  		}
  		return null;
  	}
  	//Speichert Spielstand
	protected void saveSaveState(boolean locsave){
  		ss.playerName = player.getDisplayName();
    	putBoolsInArraySave();
    	ss.haveSaveState = true;
    	if(locsave){
    		ss.posX = player.getX();
    		ss.posY = player.getY();
    		ss.posZ = player.getZ(); 	
    	}
    	search.put("player_name", player.getDisplayName());
    	try {
        	Database.get().update(ss, search); 
      	} catch (DatabaseWriteException e) {
        	logger.error(e);
        	logger.info("Spielstand von "+player.getDisplayName()+" konnte nicht erfolgreich gespeichert werden.");
        	return;
      	}
      	logger.info("Spielstand von "+player.getDisplayName()+" erfolgreich gespeichert.");
      	this.haveSaveState = true;
	}
	//Lädt Spielstand
	protected boolean loadSaveState(boolean teleport){
    	search.put("player_name", player.getDisplayName());
    	try {
        	Database.get().load(ss, search);
    	} catch (DatabaseReadException e) {
        	logger.info(player.getDisplayName() + " is not online");
        	return false;
    	}
    	if(ss.haveSaveState){
    		putBoolsInArrayLoad();
    		if(teleport){
    			player.setX(ss.posX);
    			player.setY(ss.posY);
    			player.setZ(ss.posZ);
    		} 
    		this.haveSaveState=ss.haveSaveState;
    		logger.info("Spielstand von "+player.getDisplayName()+" erfolgreich geladen.");
    	} else{
    		player.setX(-350.0);
			player.setY(64.0);
			player.setZ(264.0);
    	}
    	logger.info("Spielstand von "+player.getDisplayName()+" wurde angelegt.");
    	this.haveSaveState=true;
    	return true;
	}
	//Lädt gefundene Zauberkarten
	private void putBoolsInArrayLoad(){
		for(int i = 0; i<maxCards; i++){
			if(i==0 && ss.cardsFound1){
				found_cards[i] = ss.cardsFound1;
			} else if(i==1 && ss.cardsFound2){
				found_cards[i] = ss.cardsFound2;
			} else if(i==2 && ss.cardsFound3){
				found_cards[i] = ss.cardsFound3;
			}else if(i==3 && ss.cardsFound4){
				found_cards[i] = ss.cardsFound4;
			}else if(i==4 && ss.cardsFound5){
				found_cards[i] = ss.cardsFound5;
			}else if(i==5 && ss.cardsFound6){
				found_cards[i] = ss.cardsFound6;
			}else if(i==6 && ss.cardsFound7){
				found_cards[i] = ss.cardsFound7;
			}else if(i==7 && ss.cardsFound8){
				found_cards[i] = ss.cardsFound8;
			}else if(i==8 && ss.cardsFound9){
				found_cards[i] = ss.cardsFound9;
			}else if(i==9 && ss.cardsFound10){
				found_cards[i] = ss.cardsFound10;
			}else if(i==10 && ss.cardsFound11){
				found_cards[i] = ss.cardsFound11;
			}else if(i==11 && ss.cardsFound12){
				found_cards[i] = ss.cardsFound12;
			}else if(i==12 && ss.cardsFound13){
				found_cards[i] = ss.cardsFound13;
			}else if(i==13 && ss.cardsFound14){
				found_cards[i] = ss.cardsFound14;
			}else if(i==14 && ss.cardsFound15){
				found_cards[i] = ss.cardsFound15;
			}else if(i==15 && ss.cardsFound16){
				found_cards[i] = ss.cardsFound16;
			}else if(i==16 && ss.cardsFound17){
				found_cards[i] = ss.cardsFound17;
			}else if(i==17 && ss.cardsFound18){
				found_cards[i] = ss.cardsFound18;
			}
		}
	}
	//Speichert gefundenen Zauberkarten
	private void putBoolsInArraySave(){
		for (int i = 0; i<maxCards; i++) {
			if(i==0 && found_cards[i]){
				ss.cardsFound1 = found_cards[i];
			} else if(i==1 && found_cards[i]){
				ss.cardsFound2 = found_cards[i];
			} else if(i==2 && found_cards[i]){
				ss.cardsFound3 = found_cards[i];
			}else if(i==3 && found_cards[i]){
				ss.cardsFound4 = found_cards[i];
			}else if(i==4 && found_cards[i]){
				ss.cardsFound5 = found_cards[i];
			}else if(i==5 && found_cards[i]){
				ss.cardsFound6 = found_cards[i];
			}else if(i==6 && found_cards[i]){
				ss.cardsFound7 = found_cards[i];
			}else if(i==7 && found_cards[i]){
				ss.cardsFound8 = found_cards[i];
			}else if(i==8 && found_cards[i]){
				ss.cardsFound9 = found_cards[i];
			}else if(i==9 && found_cards[i]){
				ss.cardsFound10 = found_cards[i];
			}else if(i==10 && found_cards[i]){
				ss.cardsFound11 = found_cards[i];
			}else if(i==11 && found_cards[i]){
				ss.cardsFound12 = found_cards[i];
			}else if(i==12 && found_cards[i]){
				ss.cardsFound13 = found_cards[i];
			}else if(i==13 && found_cards[i]){
				ss.cardsFound14 = found_cards[i];
			}else if(i==14 && found_cards[i]){
				ss.cardsFound15 = found_cards[i];
			}else if(i==15 && found_cards[i]){
				ss.cardsFound16 = found_cards[i];
			}else if(i==16 && found_cards[i]){
				ss.cardsFound17 = found_cards[i];
			}else if(i==17 && found_cards[i]){
				ss.cardsFound18 = found_cards[i];
			}
		}
	}
}