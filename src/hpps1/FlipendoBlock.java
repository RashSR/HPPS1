package hpps1;
import com.pragprog.ahmine.ez.EZPlugin;
import java.util.ArrayList;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.effects.SoundEffect;

public class FlipendoBlock extends EZPlugin implements Spellable{
	private final int MAXDISTANCE=10; //maximal in eine Richtung verschiebbare Distanz von Flipendoblock
	private final int SUCHRADIUS=14; //Pfad der Flipendoblöcke werden in diesem Radius gesucht
	private final int xWidth=2; //FlipendoBlock ist immer 2 breit
	private final int zLength=2; //FlipendoBlock ist immer 2 lang
	private int yHeight; //Höhe variabel
	private ArrayList<Location> containedBlocks; //Blöcke die der Flipendoblock enthält
	private ArrayList<Location> lowestLocations; //Blöcke mit den niedrigsten y-Werten im Flipendoblock
	private ArrayList<Location> path; //Rutschbahn des FlipendoBlock
	protected static ArrayList<FlipendoBlock> fBlocks; //Spieler-Klasse hat Zugriff -> überprüft ob sie ein gültigen Block erwischt haben

	//TODO evtl einrastfunktion 
	//TODO Bloecke per file einlesbar

	//Initialisiert die FlipendoBlocks und zeichnet sie in die Spielwelt
	protected static void initFlipendoBlocks(){
		fBlocks = new ArrayList<>();
		logger.info("Neue Flipendoblockliste erstellt.");
		ArrayList<Location> f1 = new ArrayList<>();
  		f1.add(new Location(-333, 77, 316));
  		f1.add(new Location(-332, 76, 317));
  		f1.add(new Location(-320, 80, 303));
  		f1.add(new Location(-321, 79, 302));
  		for(int i=1; i<=f1.size()/2; i++){
  			if(isFb(makeLocList(get2outOfMany(f1, i)))){
  				fBlocks.add(new FlipendoBlock(2, get2outOfMany(f1, i)));
  			}else{
  				logger.info("Positon "+i+" war kein gueltiger FlipendoBlock.");
  			}
  		}
  		for(FlipendoBlock fB : fBlocks){
  			fB.clear();
  			fB.initBlock();
  		}
	}
	//Methode die ArrayList<Location>(2 Einträge) aus ArrayList<Location>(Beliebig lang, ganzzahlig teilbar durch 2) erstellt
	private static ArrayList<Location> get2outOfMany(ArrayList<Location> many, int position){
		if(many.size()%2!=0){
			logger.info("Uebergebenes Array enthält ein Element zu viel bzw. zu wenig.");
			return null;
		}
		if(position <= many.size()/2 && position > 0){
			ArrayList<Location> rA = new ArrayList<>();
			rA.add(many.get(position*2-2));
			rA.add(many.get(position*2-1));
			return rA;
		} else{
			logger.info("Fehler bei get2outOfMany.");
			return null;
		}
	}

	protected static FlipendoBlock checkIfFlipendoBlock(Location loc){
  		for(FlipendoBlock fB : fBlocks){
  			for(Location loc1 : fB.getContainedBlocks()){
  				if(locEqual(loc, loc1)){
  					return fB;
  				}
  			}
  		}
  		return null;
  	}

	//Überprüft aus File ob es ein gültiger FlipendoBlock ist
	private static boolean isFb(ArrayList<Location> containedBlocks){
		ArrayList<Location> lowestLs=new ArrayList<>();
		lowestLs=getLowestLocations(containedBlocks);
		for(Location loc : lowestLs){
			int x = (int)loc.getX();
			int y = (int)loc.getY();
			int z = (int)loc.getZ();
			if(!(loc.getWorld().getBlockAt(x, y-1, z).getType()==BlockType.CyanStainedClay)){
				return false;
			}
		}
		return true;
	}
	//Konstruktor
	private FlipendoBlock(int yHeight, ArrayList<Location> containedBlocks){
		if(containedBlocks.size()!=2){
			wrongSizeLogMessage();
			return;
		} else {
			this.containedBlocks = new ArrayList<>();
			this.containedBlocks = makeLocList(containedBlocks);
			if(this.containedBlocks.size()!=(yHeight*4)){
				logger.info("Der Block ist kein FlipendoBlock. Uebergebene ArrayList<Location> enthaelt zu wenig oder zu viele Argumente.");
				return;
			}
			if(this.isFlipendoBlock(this.containedBlocks)){
				this.yHeight=yHeight;
				logger.info("Erstellt einen FlipendoBlock mit der Hoehe: "+yHeight);
				makePath();
			} else {
				wrongLocationLogMessage();
				return;
			}
		}
	} 
	//bewegt den FlipendoBlock
	protected void moveBlock(Player player){
		Direction dir = whichDirection(player);
		if(dir == null){
			logger.info("FlipendoBlock wird nicht bewegt.");
			return;
		} else {
			logger.info("Die richtige Richtung ist "+dir+" und es sind "+getSteps(dir)+" Schirtte moeglich.");
		}
		if(getSteps(dir)>0){
			playSound(lowestLocations.get(0), SoundEffect.Type.SKELETON_STEP); //TODO: OriginalSounds aufnehmen
			drawBlock(BlockType.Air, 0, 0);
			if(dir == Direction.POS_X){
				drawBlock(BlockType.StoneBrick, 1, 0);
			} else if(dir == Direction.NEG_X){
				drawBlock(BlockType.StoneBrick, -1, 0);
			} else if(dir == Direction.POS_Z){
				drawBlock(BlockType.StoneBrick, 0, 1);
			} else if(dir == Direction.NEG_Z){
				drawBlock(BlockType.StoneBrick, 0, -1);
			}
		}
	}
	//Entfernt die vorherigen Blöcke -> beim init Prozess sind nur noch die richtigen vorhanden
	private void clear(){
		makePath();
		for(Location loc : path){
			for(int i=1; i<=this.yHeight; i++){
				Location loc1 = new Location(loc.getX(), loc.getY()+i, loc.getZ());
				for(Location loc2 : containedBlocks){
					if(!locEqual(loc1, loc2) && loc1.getWorld().getBlockAt((int)loc1.getX(), (int)loc1.getY(), (int)loc1.getZ()).getType()!=BlockType.Air){
						loc.getWorld().setBlockAt(loc1, BlockType.Air);
					}
				}
			}
		}
	}
	//Erstellt eine ArrayList aus Locations, welche den Pfad des Flipendoblocks darstellt
	private void makePath(){
		this.path = new ArrayList<>();
		Location loc = lowestLocations.get(0);
		double x0=loc.getX()-7;
		double y0=loc.getY()-1;
		double z0=loc.getZ()-7;
		for(int x=(int)x0; x<(x0+SUCHRADIUS); x++){
			for(int z=(int)z0; z<(z0+SUCHRADIUS); z++){
				if(loc.getWorld().getBlockAt((int)x, (int)y0, (int)z).getType()==BlockType.CyanStainedClay){
					this.path.add(new Location(x, y0, z));
				}
			}
		}
	}
	//Überprüft ob die übergebene ArrayList<Location> auf CyanStainedClay steht
	private boolean isFlipendoBlock(ArrayList<Location> containedBlocks){
		this.lowestLocations=new ArrayList<>();
		this.lowestLocations=getLowestLocations(containedBlocks);
		for(Location loc : this.lowestLocations){
			int x = (int)loc.getX();
			int y = (int)loc.getY();
			int z = (int)loc.getZ();
			if(!(loc.getWorld().getBlockAt(x, y-1, z).getType()==BlockType.CyanStainedClay)){
				return false;
			}
		}
		return true;
	}
	//Stell den FlipendoBlock in der Welt dar
	private void drawBlock(BlockType b, int ofsetX, int ofsetZ){
		ArrayList<Location> newCBlocks = new ArrayList<>();
		for(Location loc : this.containedBlocks){
			Block block = loc.getWorld().getBlockAt((int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
			Location l = new Location((int)loc.getX()+ofsetX, (int)loc.getY(), (int)loc.getZ()+ofsetZ);
			newCBlocks.add(l);
			loc.getWorld().setBlockAt(l, b);
		}
		this.containedBlocks.clear();
		this.containedBlocks = newCBlocks;
		this.lowestLocations.clear();
		this.lowestLocations=getLowestLocations(this.containedBlocks);
	}
	//gibt Richtung zurück in die der übergebene Player schaut
	private Direction whichDirection(Player player){
		if(lowestLocations==null){
			return null;
		}
		float headRotation = player.getLocation().getRotation();
		headRotation=cleanRot(headRotation);
		Location loc = lowestLocations.get(0);
		if(player.getX()<loc.getX() && headRotation < 300 && headRotation > 240){
			return Direction.POS_X;
		} else if(player.getX()>loc.getX() && headRotation < 120 && headRotation > 60){
			return Direction.NEG_X;
		} else if(player.getZ()<loc.getZ() && ((headRotation > 330 && headRotation <= 360) || (headRotation < 30 && headRotation >= 0)) ){
			return Direction.POS_Z;
		} else if(player.getZ()>loc.getZ() && headRotation < 210 && headRotation > 150){
			return Direction.NEG_Z;
		}
		return null;
	}
	//Erstellt aus einer float-Zahl die bereinigte 360 Grad Zahl
	private float cleanRot(float rot){
		if(rot < 0){
			return rot+360;
		} else {
			return rot;
		}
	}
	//Gibt die Anzahl der möglichen Schritte in eine Richtung des Flipendoblocks zurück
	private int getSteps(Direction direction){ //posX=1, negX=2, posZ=3, negZ=4
		int count = 0;
		if(direction==Direction.POS_X){
			int steppPosX=0;
			for(int i = 1; i<MAXDISTANCE; i++){
				for(Location loc : this.lowestLocations){
					if((loc.getWorld().getBlockAt((int)loc.getX()+i, (int)loc.getY()-1, (int)loc.getZ()).getType()==BlockType.CyanStainedClay)){
					count++;
					}
				}
				if(count == 4){
					steppPosX++;
				} 
				count = 0;
			}
			return steppPosX;
		} else if(direction==Direction.NEG_X){
				int steppNegX=0;
				for(int i = 1; i<MAXDISTANCE; i++){
					for(Location loc : this.lowestLocations){
						if((loc.getWorld().getBlockAt((int)loc.getX()-i, (int)loc.getY()-1, (int)loc.getZ()).getType()==BlockType.CyanStainedClay)){
							count++;
						}
					}
					if(count == 4){
					steppNegX++;
				} 
				count = 0;
			}
			return steppNegX;
		} else if(direction==Direction.POS_Z){
				int steppPosZ=0;
				for(int i = 1; i<MAXDISTANCE; i++){
					for(Location loc : this.lowestLocations){
						if((loc.getWorld().getBlockAt((int)loc.getX(), (int)loc.getY()-1, (int)loc.getZ()+i).getType()==BlockType.CyanStainedClay)){
							count++;
						}
					}
					if(count == 4){
					steppPosZ++;
				} 
				count = 0;
			}
			return steppPosZ;
		} else if(direction==Direction.NEG_Z){
				int steppNegZ=0;
				for(int i = 1; i<MAXDISTANCE; i++){
					for(Location loc : this.lowestLocations){
						if((loc.getWorld().getBlockAt((int)loc.getX(), (int)loc.getY()-1, (int)loc.getZ()-i).getType()==BlockType.CyanStainedClay)){
							count++;
						}
					}
					if(count == 4){
					steppNegZ++;
				} 
				count = 0;
			}
			return steppNegZ;
		}
		return -1;
	}
	//Erstellt eine ArrayList<Location> aus einer ArrayList<Location> und gibt die Locations mit den kleinsten Y-Wert(en) zurück
	private static ArrayList<Location> getLowestLocations(ArrayList<Location> containedBlocks){
		ArrayList<Location> lowestLocations = new ArrayList<>();
		double lowestY=256.0;
		for(Location loc : containedBlocks){
			if(loc.getY() < lowestY)
				lowestY = loc.getY();
		}
		for(Location loc : containedBlocks){
			if(loc.getY() == lowestY){
				lowestLocations.add(loc);
			}
		}
		return lowestLocations;
	}

	//Füllt das Klassenattribut containedBlocks(ArrayList<Location>)
	private void initBlock(){
	   	for(Location loc : this.containedBlocks){
	   		loc.getWorld().setBlockAt(loc, BlockType.StoneBrick);
    	}
	}
	//Kommt aus Interface Spellable und beschreibt was passiert wenn Flipendoblock von Zauber getroffen wird
	@Override
	public void hitBySpell(Spell spell, Spieler spieler){
		if(spell==Spell.FLIPENDO || spell==Spell.DOUBLEFLIPENDO){
			this.moveBlock(spieler.getPlayer());
		}else {
			logger.info("Wrong Spell!");
			//TODO: PlaySoundEffect
		}
	}

	protected ArrayList<Location> getContainedBlocks(){
		return this.containedBlocks;
	}

	private void wrongLocationLogMessage(){
		logger.info("Der Block ist kein FlipendoBlock. FlipendoBlock muss auf komplett auf CyanStainedClay stehen.");
	}

	private void wrongSizeLogMessage(){
		logger.info("Der Block ist kein FlipendoBlock. Uebergebene ArrayList<Location> enthaelt zu wenig oder zu viele Argumente(nur 2 angeben).");
	}
}