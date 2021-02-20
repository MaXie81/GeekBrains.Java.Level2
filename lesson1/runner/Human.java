package runner;

public class Human extends Runner {
    final public static String[] ARR_NAME = {"Петр", "Марк", "Лютый", "Семен Петрович", "Star"};
    public Human(String name, float l, float h) {
        super("Человек", name, l, h);
    }
}
