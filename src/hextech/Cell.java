package hextech;

public class Cell {
    private String name;
    private int id;
    private int value = 0;
    private int type;
    private int TYPE_BOMB = 1;
    private int TYPE_OPENED = 2;
    private int TYPE_HIDDEN = 3;
    private int x;
    private int y;
    
    public Cell(String name, int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.name = name;
        this.type = TYPE_HIDDEN;
        this.value = 0;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean isHidden() {
        return this.type == TYPE_HIDDEN;
    }
    
    public boolean isBomb() {
        return this.type == TYPE_BOMB;
    }
    
    public boolean isOpened() {
        return this.type == TYPE_OPENED;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setOpen(int value) {
        if (value < 0 || value > 8) {
            return;
        }
        this.value = value;
        this.type = TYPE_OPENED;
    }
    
    public void setBomb() {
        this.value = 0;
        this.type = TYPE_BOMB;
    }
    
    public String toString() {
        if (isOpened()) {
            return "[" + value + "]";
        } else if (isBomb()) {
            return "[x]";
        } else {
            return "[-]";
        }
    }
    
}
