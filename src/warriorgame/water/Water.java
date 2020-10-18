package warriorgame.water;

import java.util.List;

public class Water {

    String displayString = "w";
    int row;
    int column;

    public Water(int row, int column) {
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
