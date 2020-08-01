import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class FIFOQueue {

    public AtomicMarkableReference<Node> head;
    public AtomicMarkableReference<Node> tail;
    private final Object removeLock = new Object();
    private final Object addLock  = new Object();

    public FIFOQueue() {
        Node dummy = new Node(-1);
        head = new AtomicMarkableReference<>(dummy,false);
        tail = new AtomicMarkableReference<>(dummy,false);
    }

    public void enqueue(int value) {
        Node newNode = new Node(value);
        while(!tail.getReference().next.compareAndSet(null, newNode, false, false)) { }
        // tail.compareAndSet(null, newNode, false, false);
        tail = tail.getReference().next;
    }

    public int dequeue() {
        int val = head.getReference().getVal();
        boolean dequeuedSucceed = false;
        while (!isEmpty() && !dequeuedSucceed ) {
            Node nd = head.getReference().next.getReference();
            boolean logicRemove = head.getReference().next.compareAndSet(nd, nd, false, true);
            // Try to remove the node any way
            boolean physicalRemove = head.getReference().next.compareAndSet(nd, nd.next.getReference(), true, false);
            if (logicRemove) {
                if (tail.getReference().equals(nd)) {
                    tail.compareAndSet(nd, head.getReference(), false, false);
                }
                val = nd.getVal();
                dequeuedSucceed  = true;
            }
        }
        return val;
    }

    public boolean isEmpty() {
        boolean emptyList = head.getReference().equals(tail.getReference());
        return emptyList;
    }

}
