package hpps1;
import net.canarymod.api.entity.living.humanoid.Player;
//interface gibt an ob Entity angezaubert werden kann oder nicht
public interface Spellable {
  void hitBySpell(Spell spell, Player player); 
}