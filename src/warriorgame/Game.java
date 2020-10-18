package warriorgame;

import warriorgame.grid.Grid;
import warriorgame.grid.WaterGrid;
import warriorgame.healers.PeaceMaker;
import warriorgame.healers.Restorer;
import warriorgame.magiccrystal.MagicCrystal;
import warriorgame.potions.Potion;
import warriorgame.warriors.*;
import warriorgame.water.Water;
import warriorgame.weapon.Weapon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    // Initialize all the variables
    int gridRows; int gridColumns; int iterations;
    List<Warrior> warriors = null;
    List<Potion> potions = null;
    MagicCrystal magicCrystal = null;
    List<PeaceMaker> peaceMakers = null;
    List<Restorer> restorers = null;
    List<Weapon> weapons = null;
    List<int[]> waterPoints = null;
    Grid grid;
    WaterGrid waterGrid;
    List<Object>[][] tempGrid = new ArrayList[gridRows][gridColumns];

    boolean gameOver = false;
    boolean gameVisualization;

    public Game(String inputFile, boolean gameVisualization) {
        this.gameVisualization = gameVisualization;
        // Read input file
        File myObj = new File(inputFile);
        Scanner myReader = null;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert myReader != null;
        String[] firLine = myReader.nextLine().split(" ");
        gridRows = Integer.parseInt(firLine[0]); gridColumns = Integer.parseInt(firLine[1]);
        iterations = Integer.parseInt(firLine[2]);

        while (myReader.hasNextLine()) {
            String[] data = myReader.nextLine().split(": ");
            String object = data[0]; int amount = Integer.valueOf(data[1]);
            switch (object) {
                case "Warrior":
                    readWarriors(myReader, amount, gridRows, gridColumns);
                    break;
                case "Potion":
                    readPotions(myReader, amount);
                    break;
                case "Peacemaker":
                    readPeaceMakers(myReader, amount);
                    break;
                case "Restorer":
                    readRestorers(myReader, amount);
                    break;
                case "Magic Crystal":
                    String[] line = myReader.nextLine().split(" ");
                    magicCrystal = new MagicCrystal(gridRows, gridColumns, Integer.valueOf(line[0]),
                            Integer.valueOf(line[1]));
                    break;
                case "Weapon":
                    readWeapons(myReader, amount);
                    break;
                case "Water":
                    readWaterPoints(myReader, amount);
                    break;
            }
        }

        // Initialize water grid
        waterGrid = new WaterGrid(gridRows, gridColumns, waterPoints);

        // Initialize grid
        grid = new Grid(gridRows, gridColumns, potions, restorers, peaceMakers, weapons, magicCrystal);
        //Initialize temporary grid
        updateTempGrid();
    }

    private void readWarriors(Scanner myReader, int noOfWarriors, int griRows, int gridColumns) {
        warriors = new ArrayList<Warrior>();

        for (int i = 0; i < noOfWarriors; i ++) {
            String[] line = myReader.nextLine().split(" ");
            int row = Integer.parseInt(line[0]); int column = Integer.parseInt(line[1]);
            int id = Integer.parseInt(line[2]); String type = line[3];
            int age = Integer.parseInt(line[4]); float health = Float.parseFloat(line[5]);
            float offensePower = Float.parseFloat(line[6]); float defenseStrength = Float.parseFloat(line[7]);
            int inventorySize = Integer.parseInt(line[8]); String movementsString = line[9];
            LinkedList<String> movements = new LinkedList<>();
            for (int x = 0; x < movementsString.length(); x ++) {
                movements.add(String.valueOf(movementsString.charAt(x)));
            }
            Warrior warrior;
            switch (type) {
                case "Stone":
                    warrior = new StoneWarrior(griRows, gridColumns, row, column, id, type, age, health, offensePower,
                            defenseStrength, inventorySize, movements);
                    break;
                case "Flame":
                    warrior = new FlameWarrior(griRows, gridColumns, row, column, id, type, age, health, offensePower,
                            defenseStrength, inventorySize, movements);
                    break;
                case "Water":
                    warrior = new WaterWarrior(griRows, gridColumns, row, column, id, type, age, health, offensePower,
                            defenseStrength, inventorySize, movements);
                    break;
                default:
                    warrior = new AirWarrior(griRows, gridColumns, row, column, id, type, age, health, offensePower,
                            defenseStrength, inventorySize, movements);
                    break;
            }
            warriors.add(warrior);
        }
    }

    private void readPotions(Scanner myReader, int noOfPotions) {
        potions = new ArrayList<Potion>();

        for (int i = 0; i < noOfPotions; i ++) {
            String[] line = myReader.nextLine().split(" ");
            int row = Integer.parseInt(line[0]); int column = Integer.parseInt(line[1]);
            int potionNumber = Integer.parseInt(line[2]);

            Potion potion;
            if (line.length == 4) {
                potion = new Potion(row, column, potionNumber, Integer.parseInt(line[3]));
            }
            else {
                potion = new Potion(row, column, potionNumber);
            }
            potions.add(potion);
        }
    }

    private void readPeaceMakers(Scanner myReader, int noOfPeaceMakers) {
        peaceMakers = new ArrayList<PeaceMaker>();

        for (int i = 0; i < noOfPeaceMakers; i ++) {
            String[] line = myReader.nextLine().split(" ");
            PeaceMaker peaceMaker = new PeaceMaker(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            peaceMakers.add(peaceMaker);
        }
    }

    private void readRestorers(Scanner myReader, int noOfRestorers) {
        restorers = new ArrayList<Restorer>();

        for (int i = 0; i < noOfRestorers; i ++) {
            String[] line = myReader.nextLine().split(" ");
            Restorer restorer = new Restorer(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                    Float.parseFloat(line[2]));
            restorers.add(restorer);
        }
    }

    private void readWeapons(Scanner myReader, int noOfWeapons) {
        weapons = new ArrayList<Weapon>();

        for (int i = 0; i < noOfWeapons; i ++) {
            String[] line = myReader.nextLine().split(" ");
            Weapon weapon = new Weapon(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                    Float.parseFloat(line[2]));
            weapons.add(weapon);
        }
    }

    private void readWaterPoints(Scanner myReader, int noOfWaterPieces) {
        waterPoints = new ArrayList<int[]>();

        for (int i = 0; i < noOfWaterPieces; i ++) {
            String[] line = myReader.nextLine().split(" ");
            int[] waterPoint = new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1])};
            waterPoints.add(waterPoint);
        }
    }

    // 1
    private void updateWaterGrid() {
        waterGrid.updateGrid();
    }
    // 2
    private void updateWarriorMovements() {
        for (Warrior warrior : warriors) {
            warrior.moveWarrior();
        }
    }
    // 3
    private boolean checkInitialBoundConditions() {
        boolean errorState = false;
        if (warriors!= null && warriors.size() > 10) {
            for (Warrior warrior1 : warriors) {
                int warriorCount = 0;   // Warriors in the same cell
                for (Warrior warrior2 : warriors) {
                    if (warrior1.getRow() == warrior2.getRow() && warrior1.getColumn() == warrior2.getColumn()) {
                        warriorCount += 1;
                    }
                }

                if (warriorCount > 10) {
                    errorState = true;
                    System.out.println("Error: more than 10 warrior pieces were configured at the same position on " +
                            "the game grid.");
                    break;
                }
            }
        }
        if (!errorState && potions != null) {
            for (Potion potion1 : potions) {
                for (Potion potion2: potions) {
                    if (potion1 != potion2 && potion1.getRow() == potion2.getRow() &&
                            potion1.getColumn() == potion2.getColumn()) {
                        errorState = true;
                        System.out.println("Error: multiple potion pieces were configured at the same position on " +
                                "the game grid.");
                        break;
                    }
                }
            }
        }
        if (!errorState) {
            if (peaceMakers != null && restorers != null) {
                for (PeaceMaker peaceMaker1 : peaceMakers) {
                    for (PeaceMaker peaceMaker2: peaceMakers) {
                        if (peaceMaker1 != peaceMaker2 && peaceMaker1.getRow() == peaceMaker2.getRow() &&
                                peaceMaker1.getColumn() == peaceMaker2.getColumn()) {
                            errorState = true;
                            System.out.println("Error: multiple healer pieces were configured at the same position on " +
                                    "the game grid");
                            break;
                        }
                    }
                    if (errorState) {
                        break;
                    }
                    else {
                        for (Restorer restorer : restorers) {
                            if (peaceMaker1.getRow() == restorer.getRow() &&
                                    peaceMaker1.getColumn() == restorer.getColumn()) {
                                errorState = true;
                                System.out.println("Error: multiple healer pieces were configured at the same position on " +
                                        "the game grid");
                                break;
                            }
                        }
                        if (errorState) {
                            break;
                        }
                    }
                }
                for (Restorer restorer1 : restorers) {
                    for (Restorer restorer2 : restorers) {
                        if (restorer1 != restorer2 && restorer1.getRow() == restorer2.getRow() &&
                                restorer1.getColumn() == restorer2.getColumn()) {
                            errorState = true;
                            System.out.println("Error: multiple healer pieces were configured at the same position on " +
                                    "the game grid");
                            break;
                        }
                    }
                    if (errorState) {
                        break;
                    }
                }
            }
            else if (peaceMakers != null && restorers == null) {
                for (PeaceMaker peaceMaker1 : peaceMakers) {
                    for (PeaceMaker peaceMaker2 : peaceMakers) {
                        if (peaceMaker1 != peaceMaker2 && peaceMaker1.getRow() == peaceMaker2.getRow() &&
                                peaceMaker1.getColumn() == peaceMaker2.getColumn()) {
                            errorState = true;
                            System.out.println("Error: multiple healer pieces were configured at the same position on " +
                                    "the game grid");
                            break;
                        }
                    }
                    if (errorState) {
                        break;
                    }
                }
            }
            else if (peaceMakers == null && restorers != null) {
                for (Restorer restorer1 : restorers) {
                    for (Restorer restorer2 : restorers) {
                        if (restorer1 != restorer2 && restorer1.getRow() == restorer2.getRow() &&
                                restorer1.getColumn() == restorer2.getColumn()) {
                            errorState = true;
                            System.out.println("Error: multiple healer pieces were configured at the same position on " +
                                    "the game grid");
                            break;
                        }
                    }
                    if (errorState) {
                        break;
                    }
                }
            }
        }
        if (!errorState && weapons != null) {
            for (Weapon weapon1 : weapons) {
                for (Weapon weapon2 : weapons) {
                    if (weapon1 != weapon2 && weapon1.getRow() == weapon2.getRow() &&
                            weapon1.getColumn() == weapon2.getColumn()) {
                        errorState = true;
                        System.out.println("Error: multiple weapon pieces were configured at the same position on " +
                                "the game grid");
                        break;
                    }
                }
                if (errorState) {
                    break;
                }
            }
        }
        if (!errorState && waterPoints != null) {
            for (int a = 0; a < waterPoints.size(); a ++) {
                for (int b = a+1; b < waterPoints.size(); b ++) {
                    if (waterPoints.get(a)[0] == waterPoints.get(b)[0] &&
                            waterPoints.get(a)[1] == waterPoints.get(b)[1]) {
                        errorState = true;
                        System.out.println("Error: multiple water pieces were configured at the same position on " +
                                "the game grid");
                        break;
                    }
                }
                if (errorState) {
                    break;
                }
            }
        }

        return errorState;
    }

    private boolean checkWarriorBound() {
        boolean errorState = false;
        if (warriors!= null && warriors.size() > 10) {
            for (Warrior warrior1 : warriors) {
                int warriorCount = 0;   // Warriors in the same cell
                for (Warrior warrior2 : warriors) {
                    if (warrior1.getRow() == warrior2.getRow() && warrior1.getColumn() == warrior2.getColumn()) {
                        warriorCount += 1;
                    }
                }

                if (warriorCount > 10) {
                    errorState = true;
                    System.out.println("Error: warrior limit exceeded at cell "+
                            warrior1.getRow() + " " + warrior1.getColumn());
                    break;
                }
            }
        }

        return errorState;
    }
    // 4
    private void checkMagicCrystalState() {
        if (magicCrystal != null) {
            if (magicCrystal.checkActivity(warriors)) {
                List<Integer> remainWarriorIds = magicCrystal.getRemainWarriorIds();
                // Remove warriors except four warriors around the crystal
                warriors.removeIf(warrior -> !remainWarriorIds.contains(warrior.getId()));
                // Remove magic crystal from the grid
                removeMagicCrystal();
            }
        }
    }

    private void removeMagicCrystal() {
        // Remove Magic Crystal from the grid
        grid.updateCell(magicCrystal.getRow(), magicCrystal.getColumn(), magicCrystal, "Remove");
        magicCrystal = null;
    }
    // 5
    private void updateWarriorHealth() {
        List<Warrior> removedWarriors = new ArrayList<Warrior>();
        for (Warrior warrior : warriors) {
            warrior.updateHealth(restorers, waterGrid);
            // Check whether the warrior is dead
            if (warrior.isDead()) {
                removedWarriors.add(warrior);
            }
        }
        for (Warrior removedWarrior : removedWarriors) {
            warriors.remove(removedWarrior);
        }
    }
    // 6
    private void updateWarriorSpecialAbilities() {
        for (Warrior warrior : warriors) {
            warrior.updateSpecialAbility();
        }
    }
    // 7
    private void updateTempGrid() {
        tempGrid = new ArrayList[gridRows][gridColumns];

        // Add Water and other objects to the cells
        if (waterGrid.getWaterPieceCount() > 0) {
            for (int x = 0; x < gridRows; x ++) {
                for (int y = 0; y < gridColumns; y ++) {
                    tempGrid[x][y] = new ArrayList<>(grid.getCell(x, y));
                    if (waterGrid.getGrid()[x][y].equals("w")) {
                        tempGrid[x][y].add(new Water(x, y));
                    }
                }
            }
        }
        else {
            for (int x = 0; x < gridRows; x ++) {
                for (int y = 0; y < gridColumns; y ++) {
                    tempGrid[x][y] = new ArrayList<>(grid.getCell(x, y));
                }
            }
        }

        // Add warriors
        for (Warrior warrior : warriors) {
            tempGrid[warrior.getRow()][warrior.getColumn()].add(warrior);
        }
    }
    // 8
    private void updateWarriorDefenseStrength() {
        for (Warrior warrior : warriors) {
            int warriorCount = -1;

            List<Object> cell = tempGrid[warrior.getRow()][warrior.getColumn()];

            for (Object object : cell) {
                if (object.getClass().equals(warrior.getClass())) {
                    warriorCount += 1;
                }
            }

            if (warriorCount > 0) {
                float defenseStrength = Math.min(100, warrior.getDefenseStrength()+warriorCount*2);
                warrior.updateDefenseStrength(defenseStrength);
            }
        }
    }
    // 9
    private void updateWarriorPotionsAndPeace() {
        for (Warrior warrior : warriors) {
            List<List<Object>> neighbors = getNeighbors(warrior.getRow(), warrior.getColumn());
            warrior.updatePotionsAndPeace(neighbors);
        }
    }
    // 10
    private void updateWarriorBattles() {
        List<Warrior> removedWarriors = new ArrayList<Warrior>();

        for (Warrior warrior : warriors) {
            // Get neighbors around the warrior and battle
            List<List<Object>> neighbors = getNeighbors(warrior.getRow(), warrior.getColumn());
            warrior.battle(neighbors);
            // Check whether the warrior is dead or not
            if (warrior.isDead()) {
                removedWarriors.add(warrior);
            }
        }
        for (Warrior removedWarrior : removedWarriors) {
            tempGrid[removedWarrior.getRow()][removedWarrior.getColumn()].remove(removedWarrior);
            warriors.remove(removedWarrior);
        }
    }
    // 11
    private void updateWarriorAttributes() {
        for (Warrior warrior : warriors) {
            warrior.updateAttributes();
        }
    }
    // 12
    private void updateWeapons() {
        if (weapons != null) {
            List<Weapon> newWeapons = new ArrayList<Weapon>();
            List<Weapon> removedWeapons = new ArrayList<Weapon>();

            for (Weapon weapon : weapons) {
                List<Object> cell = tempGrid[weapon.getRow()][weapon.getColumn()];

                List<Warrior> neighborWarriors = new ArrayList<Warrior>();
                for (Object object : cell) {
                    if (object.getClass().equals(StoneWarrior.class) || object.getClass().equals(FlameWarrior.class)
                            || object.getClass().equals(WaterWarrior.class) || object.getClass().equals(AirWarrior.class)) {
                        neighborWarriors.add((Warrior) object);
                    }
                }

                if (neighborWarriors.size() > 0) {
                    // Remove the current weapon from from both temporary and initial grids
                    tempGrid[weapon.getRow()][weapon.getColumn()].remove(weapon);
                    grid.updateCell(weapon.getRow(), weapon.getColumn(), weapon, "Remove");
                    // Add the current weapon to the removed weapon list
                    removedWeapons.add(weapon);
                    // Find the warriors with maximum offense power
                    float max = neighborWarriors.stream().max(Comparator.comparing(Warrior::getOffensePower)).
                            get().getOffensePower();
                    List<Warrior> filtered = neighborWarriors.stream().
                            filter(warrior-> warrior.getOffensePower()== max).collect(Collectors.toList());

                    // Get the warrior with minimum id number
                    Warrior warrior = neighborWarriors.stream().min(Comparator.comparing(Warrior::getId)).get();
                    // Update inventory
                    float removed = warrior.updateInventory(weapon.getOffensePower());
                    // Check whether a weapon has been dropped or not
                    if (removed > 0) {
                        Weapon newWeapon = new Weapon(weapon.getRow(), weapon.getColumn(), removed);
                        // Add the new weapon from from both temporary and initial grids
                        tempGrid[weapon.getRow()][weapon.getColumn()].add(newWeapon);
                        grid.updateCell(weapon.getRow(), weapon.getColumn(), newWeapon, "Add");
                        // Add the new weapon to the new weapon list
                        newWeapons.add(newWeapon);
                    }
                }
            }

            weapons.addAll(newWeapons);
            weapons.removeAll(removedWeapons);
        }
    }
    // 13
    private boolean checkWinner() {
        boolean endGame = false;
        if (warriors.size() == 1) {
            System.out.println("A warrior has been proven victor!");
            endGame = true;
        }
        else if (warriors.size() == 0) {
            System.out.println("No warriors are left!");
            endGame = true;
        }

        return endGame;
    }

    private List<List<Object>> getNeighbors(int row, int column) {
        List<List<Object>> neighbors = new ArrayList<List<Object>>();

        for (int x = row-1; x <= row+1; x++) {
            for (int y = column-1; y <= column+1; y++) {
                if (x != row || y != column) {
                    neighbors.add(tempGrid[(x+gridRows)%gridRows][(y+gridColumns)%gridColumns]);
                }
            }
        }

        return neighbors;
    }

    public void displayTempGrid() {
        if (gameVisualization) {
            for (int x = 0; x < gridRows; x ++) {
                for (int y = 0; y < gridColumns; y ++) {
                    List<Object> cell = tempGrid[x][y];
                    String displayString = getDisplayString(cell);
                    System.out.print(displayString);
                }
                System.out.print("\n");
            }
        }
    }

    public void displayWarriors() {
        for (Warrior warrior : warriors) {
            System.out.println(warrior.getId() + ", " + warrior.getAge() + ", " + warrior.getHealth() + ", " +
                    warrior.getOffensePower() + ", " + warrior.getDefenseStrength() + ", " + warrior.getInventorySize()
                    + ", " + warrior.getType() + ", " + warrior.getRow() + ", " + warrior.getColumn());
        }
    }

    private String getDisplayString(List<Object> cell) {
        String displayString = ConsoleColours.EMPTY_CELL_COLOUR + ".";

        if (cell.size() > 0) {
            int[] warriorCounts = {0, 0, 0, 0};
            int magicCrystalCount = 0; int waterCount = 0; int weaponCount = 0; int healerCount = 0; int potionCount = 0;
            for (Object object : cell) {
                if (object.getClass().equals(StoneWarrior.class)) {
                    warriorCounts[0] += 1;
                }
                else if (object.getClass().equals(FlameWarrior.class)) {
                    warriorCounts[1] += 1;
                }
                else if (object.getClass().equals(WaterWarrior.class)) {
                    warriorCounts[2] += 1;
                }
                else if (object.getClass().equals(AirWarrior.class)) {
                    warriorCounts[3] += 1;
                }
                else if (object.getClass().equals(MagicCrystal.class)) {
                    magicCrystalCount += 1;
                }
                else if (object.getClass().equals(Water.class)) {
                    waterCount += 1;
                }
                else if (object.getClass().equals(Weapon.class)) {
                    weaponCount += 1;
                }
                else if (object.getClass().equals(Restorer.class) || object.getClass().equals(PeaceMaker.class)) {
                    healerCount += 1;
                }
                else {
                    potionCount += 1;
                }
            }

            int warriorCount = IntStream.of(warriorCounts).sum();
            if (warriorCount > 1) {
                displayString = ConsoleColours.WARRIOR_COLOUR + String.valueOf(warriorCount);
            }
            else if (warriorCount == 1) {
                if (warriorCounts[0] == 1) {
                    displayString = ConsoleColours.WARRIOR_COLOUR + "S";
                }
                else if (warriorCounts[1] == 1) {
                    displayString = ConsoleColours.WARRIOR_COLOUR + "F";
                }
                else if (warriorCounts[2] == 1) {
                    displayString = ConsoleColours.WARRIOR_COLOUR + "W";
                }
                else if (warriorCounts[3] == 1) {
                    displayString = ConsoleColours.WARRIOR_COLOUR + "A";
                }
            }
            else if (magicCrystalCount > 0) {
                displayString = ConsoleColours.OTHER_PIECES_COLOUR + "c";
            }
            else if (waterCount > 0) {
                displayString = ConsoleColours.WATER_COLOUR + "w";
            }
            else if (weaponCount > 0) {
                displayString = ConsoleColours.OTHER_PIECES_COLOUR + "x";
            }
            else if (healerCount > 0) {
                displayString = ConsoleColours.OTHER_PIECES_COLOUR + "h";
            }
            else if (potionCount > 0) {
                displayString = ConsoleColours.OTHER_PIECES_COLOUR + "p";
            }
        }
        displayString += ConsoleColours.RESET;

        return displayString;
    }

    private void nextIteration() {
        checkMagicCrystalState();

        updateWarriorHealth();
        updateWarriorSpecialAbilities();

        updateTempGrid();
        updateWarriorDefenseStrength();
        updateWarriorPotionsAndPeace();
        updateWarriorBattles();
        updateWarriorAttributes();
        updateWeapons();
        updateWaterGrid();
        updateWarriorMovements();
        updateTempGrid();

        if (checkWarriorBound()) {
            gameOver = true;
        }
        else {
            if (checkWinner()) {
                gameOver = true;
            }
            else {
                displayTempGrid();
                displayWarriors();
            }
        }
        System.out.println("");
    }

    public void runGame() {
        if (!checkInitialBoundConditions()) {
            displayTempGrid();
            displayWarriors();
            System.out.println("");
            for (int i = 0; i < iterations; i ++) {
                if (!gameOver) {
                    nextIteration();
                }
                else {
                    break;
                }
            }
        }
        else {
            gameOver = true;
        }

    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public int getIterations() {
        return iterations;
    }

    public List<Warrior> getWarriors() {
        return warriors;
    }

    public List<Potion> getPotions() {
        return potions;
    }

    public List<Restorer> getRestorers() {
        return restorers;
    }

    public List<PeaceMaker> getPeaceMakers() {
        return peaceMakers;
    }

    public List<Weapon> weapons() {
        return weapons;
    }

    public MagicCrystal getMagicCrystal() {
        return magicCrystal;
    }

    public String[][] getWaterGrid() {
        return waterGrid.getGrid();
    }
}
