public class Lesson2 {
    static class MyArrayNoRegException extends RuntimeException {
        MyArrayNoRegException() {
            System.out.println("Исключение! Передан нерегулярный массив.");
        };
    }
    static class MyArraySizeException extends RuntimeException {
        MyArraySizeException(String[][] arr) {
            System.out.println("Исключение! Передан массив размером: " + arr.length + "x" + arr[0].length + ".");
        };
    }
    static class MyArrayDataException extends RuntimeException {
        MyArrayDataException(int y, int x) {
            System.out.println("Исключение! Значение ячейки [" + y + "][" + x + "] содержит текст, который не может быть приведен к цифровому значению.");
        };
    }
    public static void main(String[] args) {
        final String[][] ARR_EXC_1 = {{"1", "2", "3", "4"}, {"2", "3", "5"}, {"3", "4", "5", "6"}, {"4", "5", "6", "7"}};           // нерегулярный массив
        final String[][] ARR_EXC_2 = {{"1", "2", "3"}, {"2", "3", "4"}, {"3", "4", "5"}, {"4", "5", "6"}};                          // массив не 4х3
        final String[][] ARR_EXC_3 = {{"1", "2", "3", "4"}, {"2", "3", "A", "5"}, {"3", "4", "5", "6"}, {"4", "5", "6", "7"}};      // массив содержит текстовый элемент
        final String[][] ARR = {{"1", "2", "3", "4"}, {"2", "3", "4", "5"}, {"3", "4", "5", "6"}, {"4", "5", "6", "7"}};            // "правильный" массив

        String[][][] arrTestArr = {ARR_EXC_1, ARR_EXC_2, ARR_EXC_3, ARR};
        for (int i = 0; i < arrTestArr.length; i++) {
            try {
                System.out.print("массив " + i + ": ");
                System.out.println("сумма элементов массива составляет " + sumArr4X4(arrTestArr[i]));
            } catch (MyArrayNoRegException | MyArraySizeException | MyArrayDataException e) {
                e.getMessage();
            }
        }
    }
    static int sumArr4X4(String[][] arr) throws MyArrayNoRegException, MyArraySizeException, MyArrayDataException {
        final int SIZE = 4;                         // фиксированный размер регулярного массива
        int[] arrSize = new int[arr.length];        // вспомогательный массив для определения размеров переданного массива
        int sum = 0;                                // итоговая сумма

        for (int i = 0; i < arr.length; i++) {
            arrSize[i] = arr[i].length;
        }

        for (int i = 1; i < arrSize.length; i++) {                                              // проверка, является ли переданный массив регулярным
            if (arrSize[i - 1] != arrSize[i]) throw new MyArrayNoRegException();
        }
        if (arrSize.length != SIZE || arrSize[0] != SIZE) throw new MyArraySizeException(arr);  // проверка, соответствует ли размер переданного массива требуемому

        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[i].length; j++)
                try {
                    sum += Integer.parseInt(arr[i][j]);                                         // попытка посчитать сумму элементов переданного массива
                } catch (RuntimeException e) {
                    throw new MyArrayDataException(i, j);
                }

        return sum;
    }
}
