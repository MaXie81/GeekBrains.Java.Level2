import java.util.Random;

public class Util {
    private static Random rnd = new Random();

    public static int getRnd(int max) {
        return rnd.nextInt(max + 1);
    }
    public static int[] rndArr(int[] arr) {
        int[] arrDst = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int j;
            do {
                j = rnd.nextInt(arr.length);
            } while (chkArr(arrDst, arr[j]));

            arrDst[i] = arr[j];
        }
        return arrDst;
    }
    public static String[] rndArr(String[] arr) {
        String[] arrDst = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int j;
            do {
                j = rnd.nextInt(arr.length);
            } while (chkArr(arrDst, arr[j]));

            arrDst[i] = arr[j];
        }
        return arrDst;
    }
    public static boolean chkArr(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == arr[i]) return true;
        }
        return false;
    }
    public static boolean chkArr(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == arr[i]) return true;
        }
        return false;
    }
    public static float getRndVal(int val, int pct) {
        int valPct = val * pct;
        int valS = val * 100 - valPct;

        return (float) (valS + rnd.nextInt((valPct * 2) +1)) / 100;
    }
}
