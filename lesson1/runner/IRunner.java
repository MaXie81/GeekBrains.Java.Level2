package runner;

import challenge.Barrier;
import challenge.Distance;

public interface IRunner {
    String  getInfo();
    boolean run(Distance distance);
    boolean jump(Barrier barrier);
}
