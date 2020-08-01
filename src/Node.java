import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class Node {

    int val;
    public AtomicMarkableReference<Node> next;


    public Node(int value) {
        val = value;
        next = new AtomicMarkableReference<>(null, false);
    }

    public int getVal() {
        return val;
    }

//    public boolean setNext(Node succ) {
//        boolean setSucceed = next.compareAndSet(expectedNext, succ, false, false);
//        if (setSucceed) {
//            expectedNext = succ;
//        }
//
//        return setSucceed;
//    }

//    public boolean hasNext() {
//        return next.get() != null;
//    }
}
