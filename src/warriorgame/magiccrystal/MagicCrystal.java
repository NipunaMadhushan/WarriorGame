package warriorgame.magiccrystal;

import warriorgame.warriors.Warrior;

import java.util.ArrayList;
import java.util.List;

public class MagicCrystal {

    String displayString = "c";
    int row;
    int column;
    int gridRows;
    int gridColumns;
    List<Integer> remainWarriorIds = new ArrayList<Integer>();

    public MagicCrystal(int gridRows, int gridColumns, int row, int column) {
        this.row = row;
        this.column = column;
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
    }

    public boolean checkActivity(List<Warrior> warriors) {
        boolean activityState;

        List<Integer> stoneWarriorIds = new ArrayList<Integer>();
        List<Integer> flameWarriorIds = new ArrayList<Integer>();
        List<Integer> waterWarriorIds = new ArrayList<Integer>();
        List<Integer> airWarriorIds = new ArrayList<Integer>();

        for (Warrior warrior : warriors) {
            if (Math.abs(warrior.getRow() - row) == 1 && Math.abs(warrior.getColumn() - column) == 1) {
                String type = warrior.getType();

                switch (type) {
                    case "Stone":
                        stoneWarriorIds.add(warrior.getId());
                        break;
                    case "Flame":
                        flameWarriorIds.add(warrior.getId());
                        break;
                    case "Water":
                        waterWarriorIds.add(warrior.getId());
                        break;
                    default:
                        airWarriorIds.add(warrior.getId());
                        break;
                }
            }
        }

        if (stoneWarriorIds.size() == 1 && flameWarriorIds.size() == 1 && waterWarriorIds.size() == 1 &&
                airWarriorIds.size() == 1) {
            remainWarriorIds.add(stoneWarriorIds.get(0));
            remainWarriorIds.add(flameWarriorIds.get(0));
            remainWarriorIds.add(waterWarriorIds.get(0));
            remainWarriorIds.add(airWarriorIds.get(0));
            activityState = true;
            System.out.println("The Magic Crystal has been activated! Four warriors remain...");
        }
        else {
            activityState = false;
        }

        return activityState;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public List<Integer> getRemainWarriorIds() {
        return remainWarriorIds;
    }

    public String getDisplayString() {
        return displayString;
    }
}
