import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

public class FIFOQueue {

    public AtomicMarkableReference<Node> head;
    private AtomicMarkableReference<Node> tail;

    public FIFOQueue() {
        Node dummy = new Node(-1);
        head = new AtomicMarkableReference<>(dummy,false);
        tail = new AtomicMarkableReference<>(dummy,false);
    }

    public void enqueue(int value) {
        Node newNode = new Node(value);
        boolean enqueued = false;
        while(!enqueued) {
            Node tailNode = tail.getReference();
            enqueued = tailNode.next.compareAndSet(null, newNode, false, false);
            if (enqueued) {
                // Only 1 thread at a time can reach this line
                tail.compareAndSet(tailNode, newNode, false, false);
            }
        }
    }

    public int dequeue() {
        // TODO: try until dequeue succeed
        // Raise question: what to do when queue is empty on dequeue?
        int val = head.getReference().getVal();
        boolean dequeuedSucceed = false;
        while (!dequeuedSucceed ) {
            Node nd = find();
            if (nd != null) {
                boolean logicRemove = head.getReference().next.compareAndSet(nd, nd, false, true);
                if (logicRemove) {
                    val = nd.getVal();
                    dequeuedSucceed = true;
                }
            }
        }
        return val;
    }

    public boolean isEmpty() {
        boolean emptyList = head.getReference().equals(tail.getReference());
        return emptyList;
    }

    public Node find() {
        AtomicMarkableReference<Node> ndRef = head.getReference().next;
        while (ndRef.getReference()!=null && ndRef.isMarked()) {
            head.getReference().next.compareAndSet(ndRef.getReference(), ndRef.getReference().next.getReference(), true, true);
            if (head.getReference().next.getReference().next.getReference() != null) {
                ndRef = head.getReference().next;
            }
            else {
                break;
            }
        }
        if (ndRef.isMarked()) { // If we reached tail, we'll stop the loop, but the tail may also be marked
            head.getReference().next.compareAndSet(ndRef.getReference(), ndRef.getReference().next.getReference(), true, true);
            tail.compareAndSet(ndRef.getReference(), head.getReference(), true, false);
            ndRef = head.getReference().next;
        }

        return ndRef.getReference();
    }

}
