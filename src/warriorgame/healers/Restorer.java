package warriorgame.healers;

public class Restorer {

    String displayString = "h";
    int row;
    int column;
    float restoreValue;

    public Restorer(int row, int column, float restoreValue) {
        this.row = row;
        this.column = column;
        this.restoreValue = restoreValue;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getRestoreValue() {
        return restoreValue;
    }

    public String getDisplayString() {
        return displayString;
    }
}
