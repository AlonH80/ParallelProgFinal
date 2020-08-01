import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class Node {

    private int val;
    public AtomicMarkableReference<Node> next;


    public Node(int value) {
        val = value;
        next = new AtomicMarkableReference<>(null, false);
    }

    public int getVal() {
        return val;
    }
}
