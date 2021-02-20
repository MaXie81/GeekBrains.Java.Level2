package challenge;

public class Barrier extends Challenge {
    final private float HEIGHT;
    public Barrier(int sn, float h) {
        super("препятствие", sn);
        this.HEIGHT = h;
    }
    public float getHeight() {
        return HEIGHT;
    }
    @Override
    public String getInfo() {
        return super.getInfo() + " " + getHeight();
    }
}
