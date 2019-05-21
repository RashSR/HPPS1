package hpps1;
/*Speichert folgende Daten des Spielers: - X-,Y- und Z-Koordinaten
                                         - Spielername
                                         - boolsche Variable ob der Spieler schon einen Speicherstand besitzt
                                         - Anzahl der Bohnen(gelb, grün, blau, rot) <--TODO!
                                         - boolsche Variabeln ob Spieler gewisse Zauber schon gelernt hat <--TODO!
                                         - boolsche Variabeln für Quidditch Ausrüstung <--TODO!
                                         - Variable für Haus <--TODO!
                                         - int Variable für Anzahlhauspunkte <--TODO!
  
*/
import net.canarymod.database.Column;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;

public class SaveStates extends DataAccess {
  @Column(columnName = "player_name",
          columnType = Column.ColumnType.PRIMARY,
          dataType   = DataType.STRING)
  public String playerName;
  
  @Column(columnName = "cards_found_1", dataType = DataType.BOOLEAN)
  public boolean cardsFound1;

  @Column(columnName = "cards_found_2", dataType = DataType.BOOLEAN)
  public boolean cardsFound2;

  @Column(columnName = "cards_found_3", dataType = DataType.BOOLEAN)
  public boolean cardsFound3;

  @Column(columnName = "cards_found_4", dataType = DataType.BOOLEAN)
  public boolean cardsFound4;

  @Column(columnName = "cards_found_5", dataType = DataType.BOOLEAN)
  public boolean cardsFound5;

  @Column(columnName = "cards_found_6", dataType = DataType.BOOLEAN)
  public boolean cardsFound6;

  @Column(columnName = "cards_found_7", dataType = DataType.BOOLEAN)
  public boolean cardsFound7;

  @Column(columnName = "cards_found_8", dataType = DataType.BOOLEAN)
  public boolean cardsFound8;

  @Column(columnName = "cards_found_9", dataType = DataType.BOOLEAN)
  public boolean cardsFound9;

  @Column(columnName = "cards_found_10", dataType = DataType.BOOLEAN)
  public boolean cardsFound10;

  @Column(columnName = "cards_found_11", dataType = DataType.BOOLEAN)
  public boolean cardsFound11;

  @Column(columnName = "cards_found_12", dataType = DataType.BOOLEAN)
  public boolean cardsFound12;

  @Column(columnName = "cards_found_13", dataType = DataType.BOOLEAN)
  public boolean cardsFound13;

  @Column(columnName = "cards_found_14", dataType = DataType.BOOLEAN)
  public boolean cardsFound14;

  @Column(columnName = "cards_found_15", dataType = DataType.BOOLEAN)
  public boolean cardsFound15;

  @Column(columnName = "cards_found_16", dataType = DataType.BOOLEAN)
  public boolean cardsFound16;

  @Column(columnName = "cards_found_17", dataType = DataType.BOOLEAN)
  public boolean cardsFound17;

  @Column(columnName = "cards_found_18", dataType = DataType.BOOLEAN)
  public boolean cardsFound18;
  
  @Column(columnName = "pos_x", dataType = DataType.DOUBLE)
  public double posX;
  
  @Column(columnName = "pos_y", dataType = DataType.DOUBLE)
  public double posY;

  @Column(columnName = "pos_z", dataType = DataType.DOUBLE)
  public double posZ;
  
  @Column(columnName = "have_save_state", dataType = DataType.BOOLEAN)
  public boolean haveSaveState;
  
  
  public SaveStates() {
    super("savestates");
  } 
  
  public DataAccess getInstance() {
    return new SaveStates();
  }
}