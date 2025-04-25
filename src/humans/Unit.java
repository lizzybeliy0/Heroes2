package humans;

public class Unit extends Human {
    private int type;
    private int health;
    private int count;
    private int attack;
    private int distanceAttack;
    private int cost;


    public Unit(int type, int count) {
        super(-1, -1, -1);

        final int BASIC_HEALTH = 10;
        final int BASIC_ATTACK = 8;
        final int BASIC_DISTANCE_ATTACK = 1;
        final int BASIC_MOVEMENT = 1;


        this.type = type;
        this.count = count;

        switch (type) {
            case 1:
                health = BASIC_HEALTH;
                attack = BASIC_ATTACK;
                distanceAttack = BASIC_DISTANCE_ATTACK;
                cost = 100;
                this.setStep(BASIC_MOVEMENT);
                break;
            case 2:
                health = BASIC_HEALTH + 5;
                attack = BASIC_ATTACK + 2;
                distanceAttack = BASIC_DISTANCE_ATTACK * 2;
                cost = 150;
                this.setStep(BASIC_MOVEMENT * 2);
                break;
            case 3:
                health = BASIC_HEALTH * 2;
                attack = BASIC_ATTACK * 2;
                distanceAttack = BASIC_DISTANCE_ATTACK * 2;
                cost = 200;
                this.setStep(BASIC_MOVEMENT * 2);
                break;
            case 4:
                health = BASIC_HEALTH * 3;
                attack = BASIC_ATTACK * 3;
                distanceAttack = BASIC_DISTANCE_ATTACK * 3;
                cost = 400;
                this.setStep(BASIC_MOVEMENT * 3);
                break;
            case 5:
                health = BASIC_HEALTH * 10;
                attack = BASIC_ATTACK * 10;
                distanceAttack = BASIC_DISTANCE_ATTACK * 5;
                cost = 500;
                this.setStep(BASIC_MOVEMENT * 10);
                break;

        }

    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public int getAttack() {
        return attack;
    }

    public int getDistanceAttack() {
        return distanceAttack;
    }

    public int getCost() {
        return cost;
    }

    public int getHealth() {
        return health;
    }

    public void getInfAboutUnit(int randomValue) {
        if (randomValue == 0) {
            System.out.println("Обозначение группы полицейских на поле: " + type);
            System.out.println("Количество полицейских в " + type + "-ой группе: " + count);
            System.out.println("Расстояние нанесения атаки: " + distanceAttack);
            System.out.println("Шаг полицейского " + type + "-ой группы: " + getStep() + " ячеек");
            System.out.println("Суммарное здоровье полицейских " + type + "-ой группы: " + (health * count));
            System.out.println("Суммарная атака полицейских " + type + "-ой группы: " + (attack * count) + "\n");
        } else if (randomValue == 1) {
            System.out.println("Обозначение ректора на поле: " + type);
            System.out.println("Расстояние нанесения атаки: " + distanceAttack);
            System.out.println("Шаг ректора: " + getStep() + " ячеек");
            System.out.println("Здоровье ректора: " + (health * count));
            System.out.println("Атака ректора: " + (attack * count) + "\n");
        } else {
            System.out.println("Тип студента (курс обучения): " + type);
            System.out.println("Количество студентов с " + type + "-го курса: " + count);
            System.out.println("Расстояние нанесения атаки: " + distanceAttack);
            System.out.println("Шаг студента " + type + "-го курса: " + getStep() + " ячеек");
            System.out.println("Суммарное здоровье студентов " + type + "-го курса: " + (health * count));
            System.out.println("Суммарная атака студентов " + type + "-го курса: " + (attack * count) + "\n");
        }
    }


    public int attack(int sumAttack) {
        int killedUnits = sumAttack / health;
        if (count < killedUnits) {
            killedUnits = count;
            count = 0;
            return killedUnits;
        }
        count -= killedUnits;
        return killedUnits;
    }
}
