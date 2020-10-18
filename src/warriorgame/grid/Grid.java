package warriorgame.grid;

import warriorgame.healers.PeaceMaker;
import warriorgame.healers.Restorer;
import warriorgame.magiccrystal.MagicCrystal;
import warriorgame.potions.Potion;
import warriorgame.warriors.AirWarrior;
import warriorgame.warriors.FlameWarrior;
import warriorgame.warriors.StoneWarrior;
import warriorgame.warriors.WaterWarrior;
import warriorgame.water.Water;
import warriorgame.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    int rows;
    int columns;
    List<Object>[][] grid;

    public Grid(int rows, int columns, List<Potion> potions, List<Restorer> restorers, List<PeaceMaker> peaceMakers,
                List<Weapon> weapons, MagicCrystal magicCrystal) {
        this.rows = rows;
        this.columns = columns;
        this.grid = new ArrayList[rows][columns];

        for (int x = 0; x < rows; x ++) {
            for (int y = 0; y < columns; y ++) {
                grid[x][y] = new ArrayList<Object>();
            }
        }

        // Update the grid with initial objects
        initializeGrid(potions, restorers, peaceMakers, weapons, magicCrystal);
    }

    public List<Object> getCell(int row, int column) {
        return grid[row][column];
    }

    public List<Object>[][] getGrid() {
        return grid;
    }

    public void updateCell(int row, int column, Object object, String operation) {
        if (operation.equals("Add")) {
            grid[row][column].add(object);
        }
        else if (operation.equals("Remove")) {
            grid[row][column].remove(object);
        }
    }

    public void displayGrid() {
        for (int x = 0; x < rows; x ++) {
            for (int y = 0; y < columns; y ++) {
                if (grid[x][y].size() == 0) {
                    System.out.print(".");
                }
                else {
                    System.out.print(checkObjectClass(grid[x][y].get(0)));
                }
            }
            System.out.print("\n");
        }
    }

    private String checkObjectClass(Object object) {
        String objectClass;

        if (object.getClass() == StoneWarrior.class) {
            objectClass = "S";
        }
        else if (object.getClass() == FlameWarrior.class) {
            objectClass = "F";
        }
        else if (object.getClass() == WaterWarrior.class) {
            objectClass = "W";
        }
        else if (object.getClass() == AirWarrior.class) {
            objectClass = "A";
        }
        else if (object.getClass() == Potion.class) {
            objectClass = "p";
        }
        else if (object.getClass() == Restorer.class) {
            objectClass = "h";
        }
        else if (object.getClass() == PeaceMaker.class) {
            objectClass = "h";
        }
        else if (object.getClass() == MagicCrystal.class) {
            objectClass = "c";
        }
        else if (object.getClass() == Weapon.class) {
            objectClass = "x";
        }
        else {
            objectClass = "w";
        }

        return objectClass;
    }

    private void initializeGrid(List<Potion> potions, List<Restorer> restorers, List<PeaceMaker> peaceMakers,
                                List<Weapon> weapons, MagicCrystal magicCrystal) {
        if (potions != null) {
            for (Potion potion: potions) {
                grid[potion.getRow()][potion.getColumn()].add(potion);
            }
        }
        if (restorers != null) {
            for (Restorer restorer: restorers) {
                grid[restorer.getRow()][restorer.getColumn()].add(restorer);
            }
        }
        if (peaceMakers != null) {
            for (PeaceMaker peaceMaker: peaceMakers) {
                grid[peaceMaker.getRow()][peaceMaker.getColumn()].add(peaceMaker);
            }
        }
        if (weapons != null) {
            for (Weapon weapon: weapons) {
                grid[weapon.getRow()][weapon.getColumn()].add(weapon);
            }
        }
        if (magicCrystal != null) {
            grid[magicCrystal.getRow()][magicCrystal.getColumn()].add(magicCrystal);
        }
    }
}
