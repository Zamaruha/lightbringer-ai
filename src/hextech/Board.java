package hextech;

import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Cell>> cells;
    
    public Board(int rows, int cols) {
        cells = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < cols; j++) {
                String cellName = j + "-" + i;
                int cellId = (j + 2) + i * cols;
                row.add(new Cell(cellName, cellId, j, i));
            }
            cells.add(row);
        }
    }
    
    public ArrayList<Cell> getAllCells() {
        ArrayList<Cell> returnCells = new ArrayList<Cell>();
        for (ArrayList<Cell> row : cells) {
            returnCells.addAll(row);
        }
        return returnCells;
    }
    
    public Cell getCell(int x, int y) {
        return cells.get(y).get(x);
    }
    
    public void setCell(int x, int y, Cell cell) {
        cells.get(y).set(x, cell);
    }
    
    public Cell findCellByName(String name) {
        for (ArrayList<Cell> cellRow : cells) {
            for (Cell cell : cellRow) {
                if (cell.getName() == name) {
                    return cell;
                }
            }
        }
        return null;
    }
    
    public Cell findCellById(int id) {
        for (ArrayList<Cell> cellRow : cells) {
            for (Cell cell : cellRow) {
                if (cell.getId() == id) {
                    return cell;
                }
            }
        }
        return null;
    }
    
    public String toString() {
        String res = "";
        for (int i = 0; i < cells.size(); i++) {
            String strRow = "";
            for (int j = 0; j < cells.get(0).size(); j++) {
                strRow += " " + getCell(j, i) + " ";
            }
            strRow += "\n";
            res += strRow;
        }
        return res;
    }
    
    public ArrayList<Cell> getNeighbours(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        ArrayList<Cell> list = new ArrayList<Cell>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx >= cells.get(0).size()) continue;
                if (y + dy < 0 || y + dy >= cells.size()) continue;
                list.add(getCell(x + dx, y + dy));
            }
        }
        return list;
    }
    
    public static void main(String[] args) {
        Board b = new Board(6, 6);
        b.getCell(3, 0).setOpen(4);
        b.getCell(0, 1).setBomb();
        System.out.println(b);
        ArrayList<Cell> n = b.getNeighbours(b.getCell(3,  1));
        System.out.println(n);
        for (Cell cell : n) {
            System.out.println(cell.getName());
        }
    }
}
