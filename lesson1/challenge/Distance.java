package challenge;

public class Distance extends Challenge{
    final private float LENGTH;
    public Distance(int sn, float l) {
        super("забег", sn);
        this.LENGTH = l;
    }
    public float getLength() {
        return LENGTH;
    }
    @Override
    public String getInfo() {
        return super.getInfo() + " " + getLength();
    }
}
