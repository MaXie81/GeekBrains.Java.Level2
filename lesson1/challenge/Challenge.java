package challenge;

abstract public class Challenge {
    final private String TYPE;              // тип испытания
    final private int SN;                   // порядковый номер испытания

    Challenge(String type, int sn) {
        this.TYPE = type;
        this.SN = sn;
    }

    protected String getInfo() {
        return SN + ". " + TYPE;
    }
}
