package field;


public class PartField {
    private int value; //int todo  (cделано)
    private int own;
    private boolean treasure;
    private boolean castle;



    private boolean isSwitchStand;
    private int switchesCount;
    private int mainSwitchIndex;
    private boolean isActive;
    private int turnsLeft;

    public PartField(int value) {
        this.value = value;
        this.own = 0;
        this.treasure = false;
        this.castle = false;

        this.isSwitchStand = false;
        this.switchesCount = 0;
        this.mainSwitchIndex = 0;
        this.isActive = false;
        this.turnsLeft = 0;

    }

    public void setState(int value, int ownage, boolean treasure, boolean castle) { //на буд заделку, м б удобнее
        this.value = value;
        this.own = ownage;
        this.treasure = treasure;
        this.castle = castle;
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


    public boolean isSwitchStand() { return isSwitchStand; }
    public void setSwitchStand(boolean switchStand) { this.isSwitchStand = switchStand; }
    public int getSwitchesCount() { return switchesCount; }
    public void setSwitchesCount(int count) { this.switchesCount = count; }
    public int getMainSwitchIndex() { return mainSwitchIndex; }
    public void setMainSwitchIndex(int index) { this.mainSwitchIndex = index; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public int getTurnsLeft() { return turnsLeft; }
    public void setTurnsLeft(int turns) { this.turnsLeft = turns; }


    public void displayPartField() {

        if (isActive) {
            value = 1;
            System.out.print("с\t");
            return;
        }
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