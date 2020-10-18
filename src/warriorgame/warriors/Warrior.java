package warriorgame.warriors;

import warriorgame.grid.WaterGrid;
import warriorgame.healers.Restorer;
import warriorgame.water.Water;

import java.util.List;

public interface Warrior {
    public int getId();
    public int getAge();
    public int getInventorySize();
    public int getRow();
    public int getColumn();
    public float getHealth();
    public float getOffensePower();
    public float getDefenseStrength();
    public String getType();



    public void moveWarrior();
    public void updateHealth(List<Restorer> restorers, WaterGrid waterGrid);
    public void updateSpecialAbility();
    public void updateDefenseStrength(float newDefenseStrength);
    public void updatePotionsAndPeace(List<List<Object>> neighbors);
    public void battle(List<List<Object>> neighbors);
    public void updateAttributes();


    public float updateInventory(float offensePower);

    public String getDisplayString();
    public boolean isDead();

    // Testing
    public boolean getTranceCause();
    public boolean getInvisibility();
    public boolean getPeace();
    public boolean getAbilityState();
}
