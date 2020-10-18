package warriorgame.healers;

public class PeaceMaker {

    String displayString = "h";
    int row;
    int column;

    public PeaceMaker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getDisplayString() {
        return displayString;
    }
}
