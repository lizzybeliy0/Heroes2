package field;


public class PartField {
    private int value;                       //int todo  (cделано)
    private int own;
    private boolean treasure;
    private boolean castle;


    public PartField(int value) {
        this.value = value;
        this.own = 0;
        this.treasure = false;
        this.castle = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOwnage() {
        return own;
    }

    public void setOwnage(int ownage) {
        this.own = ownage;
    }

    public boolean isTreasure() {
        return treasure;
    }

    public void setTreasure(boolean treasure) {
        this.treasure = treasure;
    }

    public boolean isCastle() {
        return castle;
    }

    public void setCastle(boolean castle) {
        this.castle = castle;
    }

    public void displayPartField() {

        if (treasure) {
            value = 1;
            System.out.print("T\t");
            return;
        }
        if (value == 1) {
            //value = 1;
            System.out.print("+\t");
            return;
        }
        if (value == 3) {
            //value = 3;
            System.out.print("*\t");
            return;
        }
        if (value == 8) {
            System.out.print("^\t");
            return;
        }

        switch (own) {
            case 1:
                if (castle) {
                    value = 0;
                    System.out.print("И\t");
                } else {
                    value = 2;
                    System.out.print("#\t");
                }
                break;
            case 2:
                if (castle) {
                    value = 0;
                    System.out.print("К\t");
                } else {
                    value = 2;
                    System.out.print("&\t");
                }
                break;
            default:
                if (castle) {
                    value = 1;
                    System.out.print("З\t");
                } else {
                    value = 2;
                    System.out.print("?\t");
                }
                break;
        }
    }
}