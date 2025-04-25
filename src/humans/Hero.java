package humans;


public class Hero extends Human {

    public static final int TYPES_COUNT = 5;
    public static final int UNITS_COUNT = 6;
    private int gold;


    private Unit[] units;
    private Unit[] computerUnits;

    public Hero(int posY, int posX) {
        super(posY, posX, 8);
        gold = 5000;

        units = new Unit[UNITS_COUNT];
        computerUnits = new Unit[UNITS_COUNT];

        for (int i = 1; i <= TYPES_COUNT; i++) {
            units[i] = new Unit(i, 0);
            computerUnits[i] = new Unit(i, 0);
        }
        int count = 6;
        units[1] = new Unit(1, count);
        computerUnits[1] = new Unit(1, count);

    }

    public int getGold() {
        return gold;
    }

    public void addGold(int newGold) {
        gold += newGold;
    }

    public Unit[] getUnits() {
        return units;
    }

    public void getInfAboutHero() {
        System.out.println("Количество золота: " + gold + "\n");
        for (int i = 1; i <= TYPES_COUNT; i++) {
            if (units[i].getCount() > 0) {
                units[i].getInfAboutUnit(2);
            }
        }
        System.out.println();
    }


    public Unit[] makeEnemyUnits(int number) {
        Unit[] enemyUnits = new Unit[UNITS_COUNT];
        for (int i = 0; i < UNITS_COUNT; i++) {
            enemyUnits[i] = new Unit(i, 0);
        }
        if (number == 10) {
            enemyUnits[5].addCount(5);                                                  //ректор
            return enemyUnits;
        }
        enemyUnits[number].addCount(10);
        if (number == 2) {
            enemyUnits[number - 1].addCount(3);
        }
        if (number == 3) {
            enemyUnits[number - 2].addCount(10);
            enemyUnits[number - 1].addCount(8);                                         //полиция
            enemyUnits[number].addCount(5);
        }
        if (number == 4) {
            enemyUnits[number - 2].addCount(10);
            enemyUnits[number - 1].addCount(4);

        }
        if (number == 5) {
            enemyUnits[number - 3].addCount(10);
            enemyUnits[number - 2].addCount(6);
            enemyUnits[number - 1].addCount(2);
        }
        if (number == 6) {
            enemyUnits[number - 4].addCount(10);
            enemyUnits[number - 3].addCount(5);
            enemyUnits[number - 2].addCount(5);
        }
        return enemyUnits;
    }
}
