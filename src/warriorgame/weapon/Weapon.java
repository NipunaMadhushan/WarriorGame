package warriorgame.weapon;

public class Weapon {

    String displayString = "x";
    int row;
    int column;
    float offensePower;

    public Weapon(int row, int column, float offensePower) {
        this.row = row;
        this.column = column;
        this.offensePower = offensePower;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getOffensePower() {
        return offensePower;
    }

    public String getDisplayString() {
        return displayString;
    }
}
