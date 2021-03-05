public class Lesson5 {
    static final int SIZE = 50000000;
    static float[] arr = new float[SIZE];
    static float[] arrDst = new float[SIZE];

    public static void main(String[] args) {
        System.out.println("Запуск без использования многопоточности...");
        resetArr(arr);
        calcArr(arr, "обычный запуск");
        System.out.println("\n" + "Запуск в 2 потока...");
        resetArr(arr);
        calcArr(arr, 2);
        System.out.println("\n" + "Запуск в 4 потока...");
        resetArr(arr);
        calcArr(arr, 4);
        System.out.println("\n" + "Запуск в 10 потоков...");
        resetArr(arr);
        calcArr(arr, 10);
    }
    static void resetArr(float[] arr) {
        for (float val : arr) {
            val = 1.0f;
        }
    }
    static void calcArr(float[] arr, int numPart) {
        if (arr.length % numPart != 0) {
            System.out.println("Попытка разбить массив на не краткое количество частей!");
            return;
        }

        int size = arr.length / numPart;
        float[][] arrDst = new float[numPart][size];
        Thread[] arrThread = new Thread[numPart];
        long dtS, dtE;

        dtS = System.currentTimeMillis();
        for (int i = 0; i < arrDst.length; i++)
            System.arraycopy(arr, size * i, arrDst[i], 0, size);
        dtE = System.currentTimeMillis();
        System.out.println("Время выполнения разбивки массива(мСек): " + (dtE - dtS));

        dtS = System.currentTimeMillis();
        for (int i = 0; i < arrDst.length; i++) {
            int idx = i;
            arrThread[idx] = new Thread(() -> calcArr(arrDst[idx], "поток " + idx));
            arrThread[idx].start();
        }
        try {
            for (int i = 0; i < arrThread.length; i++) arrThread[i].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dtE = System.currentTimeMillis();
        System.out.println("Время выполнения расчета всех массивов(мСек): " + (dtE - dtS));

        dtS = System.currentTimeMillis();
        for (int i = 0; i < arrDst.length; i++)
            System.arraycopy(arrDst[i], 0, arr, size * i, size);

        dtE = System.currentTimeMillis();
        System.out.println("Время выполнения склейки массива(мСек): " + (dtE - dtS));
    }
    static void calcArr(float[] arr, String description) {
        long dtS, dtE;

        dtS = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        dtE = System.currentTimeMillis();
        System.out.println(description + ", время расчета(мС): " + (dtE - dtS));
    }
}
