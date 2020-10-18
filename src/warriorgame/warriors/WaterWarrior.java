package warriorgame.warriors;

import warriorgame.grid.WaterGrid;
import warriorgame.healers.PeaceMaker;
import warriorgame.healers.Restorer;
import warriorgame.potions.Potion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WaterWarrior implements Warrior {

    String displayString = "W";
    // Attributes
    int id;
    int age;
    float health;
    float offensePower;
    float defenseStrength;
    int inventorySize;
    List<Float> inventory = new ArrayList<>();
    LinkedList<String> movements;
    String type;
    int row;
    int column;
    int gridRows;
    int gridColumns;
    boolean abilityUsed = false;
    int abilityRemaining = 4;
    // Potions
    boolean tranceCause = false;
    boolean invisibility = false;
    int invisibilityIteration = 0;
    // Peace Maker
    boolean peace = false;

    public WaterWarrior(int gridRows, int gridColumns, int row, int column, int id, String type, int age, float health, float offensePower,
                        float defenseStrength, int inventorySize, LinkedList<String> movements) {
        this.id = id;
        this.type = type;
        this.inventorySize = inventorySize;
        this.age = age;
        this.health = health;
        this.offensePower = offensePower;
        this.defenseStrength = defenseStrength;
        this.row = row;
        this.column = column;
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
        this.movements = movements;
    }

    // 1
    public void moveWarrior() {
        String move = movements.removeFirst();
        setMovements(move);
        movements.addLast(move);
    }
    private void setMovements(String move) {
        if (tranceCause) {
            switch (move) {
                case "d":
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "a":
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "w":
                    row = (row + 1 + gridRows) % gridRows;
                    break;
                case "x":
                    row = (row - 1 + gridRows) % gridRows;
                    break;
                case "e":
                    row = (row + 1 + gridRows) % gridRows;
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "q":
                    row = (row + 1 + gridRows) % gridRows;
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "c":
                    row = (row - 1 + gridRows) % gridRows;
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "z":
                    row = (row - 1 + gridRows) % gridRows;
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "n":
                    break;
            }
        }
        else {
            switch (move) {
                case "d":
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "a":
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "w":
                    row = (row - 1 + gridRows) % gridRows;
                    break;
                case "x":
                    row = (row + 1 + gridRows) % gridRows;
                    break;
                case "e":
                    row = (row - 1 + gridRows) % gridRows;
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "q":
                    row = (row - 1 + gridRows) % gridRows;
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "c":
                    row = (row + 1 + gridRows) % gridRows;
                    column = (column + 1 + gridColumns) % gridColumns;
                    break;
                case "z":
                    row = (row + 1 + gridRows) % gridRows;
                    column = (column - 1 + gridColumns) % gridColumns;
                    break;
                case "n":
                    break;
            }
        }
    }

    // 2
    public void updateHealth(List<Restorer> restorers, WaterGrid waterGrid) {
        // Check for restorer pieces in the neighborhood
        if (restorers != null) {
            for (Restorer restorer: restorers) {
                if (Math.abs(restorer.getRow()-row) <= 1 && Math.abs(restorer.getColumn()-column) <= 1) {
                    if (restorer.getRow() != row || restorer.getColumn() != column) {
                        health += restorer.getRestoreValue();
                    }
                }
            }
        }

        // Check for water pieces in the neighborhood
        int neighborWaterPieces = 0;
        for (int x = row-1; x <= row+1; x ++) {
            for (int y = column-1; y <= column+1; y ++) {
                if (x != 0 || y != 0) {
                    if (waterGrid.getGrid()[(x+gridRows)%gridRows][(y+gridColumns)%gridColumns].equals("w")) {
                        neighborWaterPieces += 1;
                    }
                }
            }
        }
        if (neighborWaterPieces > 0) {
            health += 3;
        }
        else {
            health -= 0.5;
        }

        offensePower = Math.min(100, offensePower+neighborWaterPieces);
        health = Math.min(100, health);
    }

    // 3
    public void updateSpecialAbility() {
        if (!abilityUsed && health <= 10) {
            activateSpecialAbility();
        }
    }
    private void activateSpecialAbility() {
        abilityUsed = true;
        health += 20;
        System.out.println("Special ability performed by water warrior!");
    }

    // 4
    public void updateDefenseStrength(float newDefenseStrength) {
        defenseStrength = newDefenseStrength;
    }

    // 5
    public void updatePotionsAndPeace(List<List<Object>> neighbors) {
        int tranceCauseCount = 0;
        int tranceHealCount = 0;
        List<Integer> invisibilityIterations = new ArrayList<Integer>();
        peace = false;

        for (List<Object> neighbor : neighbors) {
            for (Object object : neighbor) {
                if (object.getClass().equals(Potion.class)) {
                    String potionType = ((Potion) object).getPotionType();
                    if (potionType.equals("TranceCause")) {
                        tranceCauseCount += 1;
                    }
                    else if (potionType.equals("TranceHeal")) {
                        tranceHealCount += 1;
                    }
                    else {
                        invisibilityIterations.add(((Potion) object).getIterations());
                    }
                }
                else if (object.getClass().equals(PeaceMaker.class)) {
                    peace = true;
                }
            }
        }

        if (tranceHealCount == 0 && tranceCauseCount > 0) {
            tranceCause = true;
        }
        else if (tranceHealCount > 0) {
            tranceCause = false;
        }

        if (invisibilityIterations.size() > 0) {
            invisibility = true;
            invisibilityIteration = Collections.max(invisibilityIterations);
            invisibilityIteration -= 1;
        }
        else {
            if (invisibilityIteration > 0) {
                invisibilityIteration -= 1;
            }
            else {
                invisibility = false;
            }
        }
    }

    // 6
    public void battle(List<List<Object>> neighbors) {
        for (List<Object> neighbor : neighbors) {
            if (neighbor.size() > 0) {
                for (Object object : neighbor) {
                    if (object.getClass().equals(StoneWarrior.class) || object.getClass().equals(FlameWarrior.class) ||
                            object.getClass().equals(WaterWarrior.class) || object.getClass().equals(AirWarrior.class)) {
                        Warrior warrior = (Warrior) object;
                        battleComparison(warrior);
                    }
                }
            }
        }
    }
    private void battleComparison(Warrior warrior) {
        if (!invisibility && !peace) {
            if (!warrior.getInvisibility() && !warrior.getPeace()) {
                if (getDefenseStrength() < warrior.getDefenseStrength()) {
                    health -= warrior.getOffensePower();
                }
            }
        }
    }

    // 7
    public void updateAttributes() {
        updateAge();
        if (age > 50) {
            defenseStrength = Math.min(30, defenseStrength);
        }
        else if (age > 25) {
            defenseStrength = Math.min(50, defenseStrength);
        }
        else if (age > 15) {
            defenseStrength = Math.min(70, defenseStrength);
        }
    }

    public boolean isDead() {
        boolean dead;
        if (health > 0) {
            dead = false;
        }
        else {
            dead = true;
            System.out.println("A warrior has left the game!");
        }

        return dead;
    }

    // Needs to be updated
    public float updateInventory(float offensePower) {
        float removedWeapon = 0;
        if (inventory.size() >= inventorySize) {
            removedWeapon = inventory.get(0);
            inventory.remove(0);
        }
        inventory.add(offensePower);

        return removedWeapon;
    }

    private void updateAge() {
        age += 1;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getInventorySize() {
        return inventory.size();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getHealth() {
        return health;
    }

    public float getOffensePower() {
        float totalOffense = inventory.stream().reduce((float) 0, Float::sum) + offensePower;
        return totalOffense;
    }

    public float getDefenseStrength() {
        return defenseStrength;
    }

    public boolean getInvisibility() {
        return invisibility;
    }

    public boolean getPeace() {
        return peace;
    }

    public String getType() {
        return type;
    }

    public String getDisplayString() {
        return displayString;
    }


    // Testing
    public boolean getAbilityState() {
        return abilityUsed;
    }

    public boolean getTranceCause() {
        return tranceCause;
    }
}
