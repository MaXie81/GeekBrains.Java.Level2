package runner;

import challenge.Barrier;
import challenge.Distance;

abstract class Runner implements IRunner {
    final private String TYPE;
    final private String NAME;
    final private float LENGTH;
    final private float HEIGTH;

    public Runner(String type, String name, float l, float h) {
        this.TYPE = type;
        this.NAME = name;
        this.LENGTH = l;
        this.HEIGTH = h;
    }

    @Override
    public String  getInfo() {
        return NAME + "(" + TYPE + "/" + LENGTH + "/" + HEIGTH + ")";
    }

    @Override
    public boolean run(Distance distance) {
        if (distance.getLength() <= LENGTH) {
            System.out.println(distance.getInfo() + " пробежал");
            return true;
        } else {
            System.out.println(distance.getInfo() + " не пробежал");
            return false;
        }
    }

    @Override
    public boolean jump(Barrier barrier) {
        if (barrier.getHeight() <= HEIGTH) {
            System.out.println(barrier.getInfo() + " перепрыгнул");
            return true;
        } else {
            System.out.println(barrier.getInfo() + " не перепрыгнул");
            return false;
        }
    }
}
