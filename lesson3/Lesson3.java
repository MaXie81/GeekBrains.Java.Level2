import java.util.*;

public class Lesson3 {
    static List<String> lstWord = new ArrayList<>();
    static Set<String> setWord = new LinkedHashSet<>();                         // чтобы слова сохраняли последовательность при добавлении в список
    static HashMap<String, Integer> mapWord= new LinkedHashMap<>();            // чтобы слова сохраняли последовательность при добавлении в hash-таблицу
    static HashMap<String, HashSet<String>> mapNamePhone = new HashMap<>();

    public static void main(String[] args) {
        // Задание 1
        System.out.println("Задание 1");
        fillArrWord();
        System.out.println("массив слов: " + lstWord);

        setWord.addAll(lstWord);
        System.out.println("массив состоит из слов: " + setWord);

        getStat();
        System.out.println("статиска по искпользованию слов: " + mapWord);

        // Задание 2
        System.out.println("Задание 2");
        add("Иванов", "495 100-10-01");
        add("Петров", "495 100-20-55");
        add("Сидоров", "495 100-30-33");
        add("Иванов", "495 103-23-77");

        get("Петров");
        get("Иванов");
        get("Светлов");
    }
    static void fillArrWord() {
        final String[] ARR_WORD = {"ноль", "раз", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
        Random rnd = new Random();

        for (int i = 1; i <= 30; i++)
            lstWord.add(ARR_WORD[rnd.nextInt(10)]);
    }
    static void getStat() {
        for (String key : lstWord) {
            if (mapWord.containsKey(key))
                mapWord.put(key, mapWord.get(key) + 1);
            else
                mapWord.put(key, 1);
        }
    }
    static void add(String name, String numPhone) {
        if (!mapNamePhone.containsKey(name)) mapNamePhone.put(name, new HashSet<>());
        if (mapNamePhone.containsKey(name))  mapNamePhone.get(name).add(numPhone);
    }
    static void get(String name) {
        System.out.println(name + ": " + (mapNamePhone.containsKey(name) ? mapNamePhone.get(name) : "нет в справочнике"));
    }
}
