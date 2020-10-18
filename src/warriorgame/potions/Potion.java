package warriorgame.potions;

public class Potion {

    String displayString = "p";
    int row;
    int column;
    int potionNumber;
    int iterations;

    public Potion(int row, int column, int potionNumber) {
        this.row = row;
        this.column = column;
        this.potionNumber = potionNumber;
    }

    public Potion(int row, int column, int potionNumber, int iterations) {
        this.row = row;
        this.column = column;
        this.potionNumber = potionNumber;
        this.iterations = iterations;
    }

    public String getPotionType() {
        String type;
        if (potionNumber == 0) {
            type = "TranceCause";
        }
        else if (potionNumber == 1) {
            type = "TranceHeal";
        }
        else {
            type = "Invisible";
        }

        return type;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPotionNumber() {
        return potionNumber;
    }

    public int getIterations() {
        return iterations;
    }

    public String getDisplayString() {
        return displayString;
    }
}
