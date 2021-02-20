import runner.*;
import challenge.Barrier;
import challenge.Challenge;
import challenge.Distance;

public class Competitions {
    final private static int PCT_TEST = 20;                     // процент разброса для этапа созтязания
    final private static int PCT_RUNNER = 30;                   // процент разброса для участника
    final private static int LENGTH = 100;                      // базовая величина для забега/бега
    final private static int HEIGTH = 1;                        // базовая величина для препятствия/прыжка

    private static Challenge[] arrChall = setArrChall(10);      // программа соревнования(список этапов)
    private static IRunner[] arrRunner = setArrRunner(10);      // список участников

    public static void main(String[] args) {
        boolean flgRun;
        boolean flg;

        getChallInfo();
        System.out.println();

        getRunnerInfo();
        System.out.println();

        for (IRunner runner : arrRunner) {
            System.out.println("Участник: " + runner.getInfo());
            flgRun = true;
            for (Challenge chall: arrChall) {
                if (chall instanceof Distance) {
                    if (!runner.run((Distance) chall)) {
                        flgRun = false;
                        break;
                    }
                }
                else if (chall instanceof Barrier) {
                    if (!runner.jump((Barrier) chall)) {
                        flgRun = false;
                        break;
                    }
                }
                else {
                    flgRun = false;
                    System.out.println("Ошибка типа!");
                    break;
                }
            }
            if (flgRun)
                System.out.println("Участник успешно прошел все этапы соревнования!" + "\n");
            else {
                System.out.println("Соревнование для участиника закончено!" + "\n");
            }
        }
    }
    private static void getChallInfo() {                                    // получить информацию о программе соревнования
        System.out.println("Программа соревнования(список этапов):");
        for (int i = 0; i < arrChall.length; i++) {
            if (arrChall[i] instanceof Distance)
                System.out.println(((Distance)arrChall[i]).getInfo());
            else if (arrChall[i] instanceof Barrier)
                System.out.println(((Barrier) arrChall[i]).getInfo());
            else
                System.out.println("Ошибка типа!");
        }
    }
    private static Challenge[] setArrChall(int cnt) {                       // задать программу соревнвания(список этапов)
        Challenge[] arr = new Challenge[cnt];
        for (int i = 0; i < arr.length; i++) {
            if (Util.getRnd(1) == 0)
                arr[i] = new Distance(i + 1, Util.getRndVal(LENGTH, PCT_TEST));
            else
                arr[i] = new Barrier(i + 1, Util.getRndVal(HEIGTH, PCT_TEST));
        }
        return arr;
    }
    private static void getRunnerInfo() {                                   // получить информацию об участниках соревнования
        System.out.println("Список участников:");
        for (int i = 0; i < arrRunner.length; i++) {
            System.out.println(i + 1 + ". " + arrRunner[i].getInfo());
        }
    }
    private static IRunner[] setArrRunner(int cnt) {                        // задать массив участников соревнования
        String[] arrNameCat = Util.rndArr(Cat.ARR_NAME);
        String[] arrNameHum = Util.rndArr(Human.ARR_NAME);
        String[] arrNameRob = Util.rndArr(Robot.ARR_NAME);

        int iCat = 0;
        int iHum = 0;
        int iRob = 0;

        IRunner[] arr = new IRunner[cnt];
        for (int i = 0; i < arr.length; i++) {
            try {
                switch (Util.getRnd(2)) {
                    case 0:
                        arr[i] = new Cat(arrNameCat[iCat++], Util.getRndVal(LENGTH, PCT_RUNNER), Util.getRndVal(HEIGTH, PCT_RUNNER));
                        break;
                    case 1:
                        arr[i] = new Human(arrNameHum[iHum++], Util.getRndVal(LENGTH, PCT_RUNNER), Util.getRndVal(HEIGTH, PCT_RUNNER));
                        break;
                    case 2:
                        arr[i] = new Robot(arrNameRob[iRob++], Util.getRndVal(LENGTH, PCT_RUNNER), Util.getRndVal(HEIGTH, PCT_RUNNER));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return arr;
    }
}
