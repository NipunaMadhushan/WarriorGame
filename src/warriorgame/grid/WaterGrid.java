package warriorgame.grid;

import java.util.List;

public class WaterGrid {

    private int rowsInGrid;
    private int columnsInGrid;
    private List<int[]> waterPoints;
    private String[][] grid;
    private int waterPieceCount;

    public WaterGrid(int rowsInGrid, int columnsInGrid, List<int[]> waterPoints) {
        this.rowsInGrid = rowsInGrid;
        this.columnsInGrid = columnsInGrid;
        this.waterPoints = waterPoints;
        this.grid = new String[rowsInGrid][columnsInGrid];

        // Initialize the 2D grid with "." characters (no water in each cell)
        for (int x = 0; x < rowsInGrid; x ++) {
            for (int y = 0; y < columnsInGrid; y ++) {
                grid[x][y] = ".";
            }
        }
        // Fill the water to the points
        if (waterPoints != null) {
            this.waterPieceCount = waterPoints.size();
            for (int[] waterPoint : waterPoints) {
                grid[waterPoint[0]][waterPoint[1]] = "w";
            }
        }
        else {
            this.waterPieceCount = 0;
        }
    }

    private boolean checkWater(int row, int column) {
        boolean state = false;
        int count = -1;
        for (int x = row-1+rowsInGrid; x <= row+1+rowsInGrid; x ++) {
            for (int y = column-1+columnsInGrid; y <= column+1+columnsInGrid; y ++) {
                if (grid[x % rowsInGrid][y % columnsInGrid].equals("w")) {
                    count += 1;
                }
            }
        }

        if (grid[row][column].equals("w")) {
            state = 2 == count || count == 3;
        }
        else {
            if (count == 2) {
                state = true;
            }
        }

        return state;
    }

    public void updateGrid() {
        if (waterPieceCount > 0) {
            int newWaterPieceCount = 0;
            String[][] newGrid = new String[rowsInGrid][columnsInGrid];
            for (int x = 0; x < rowsInGrid; x ++) {
                for (int y = 0; y < columnsInGrid; y ++) {
                    if (checkWater(x, y)) {
                        newGrid[x][y] = "w";
                        newWaterPieceCount += 1;
                    }
                    else {
                        newGrid[x][y] = ".";
                    }
                }
            }
            grid = newGrid;
            waterPieceCount = newWaterPieceCount;
        }
    }

    public void displayGrid() {
        for (int x = 0; x < rowsInGrid; x ++) {
            for (int y = 0; y < columnsInGrid; y ++) {
                System.out.print(grid[x][y]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public String[][] getGrid() {
        return grid;
    }

    public String getCell(int row, int column) {
        return grid[row][column];
    }

    public boolean checkNeighborhood(int row, int column) {
        boolean waterPieceState = false;
        for (int x = row-1; x <= row+1; x ++) {
            for (int y = column-1; y <= column+1; y ++) {
                if (x != row || y != column) {
                    if (grid[x][y].equals("w")) {
                        waterPieceState = true;
                        break;
                    }
                }
            }
        }

        return waterPieceState;
    }

    public int getWaterPieceCount() {
        return waterPieceCount;
    }
}
