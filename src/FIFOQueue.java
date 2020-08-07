
import java.util.concurrent.atomic.AtomicMarkableReference;


public class FIFOQueue {

    private AtomicMarkableReference<Node> head;
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
            advanceTail();
            enqueued = tail.getReference().next.compareAndSet(null, newNode, false, false);
        }
    }

    public int dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            advanceTail();
            if (isEmpty()) {
                throw new EmptyQueueException("Queue is empty");
            }
        }
        int val = -1; // = head.getReference().getVal();
        boolean dequeuedSucceed = false;

        while (!dequeuedSucceed ) {
            AtomicMarkableReference<Node> nodeRef = find();
            Node nd = nodeRef.getReference();
            if (nd == null) {
                throw new EmptyQueueException("Queue is empty");
            }
            boolean logicRemove = nodeRef.compareAndSet(nd, nd, false, true);
            if (logicRemove) {
                val = nd.getVal();
                dequeuedSucceed = true;
            }
        }
        return val;
    }

    public boolean isEmpty() {
        return head.getReference().equals(tail.getReference());
    }

    public AtomicMarkableReference<Node> find() {
        AtomicMarkableReference<Node> prevNodeRef = head;
        AtomicMarkableReference<Node> nodeRef = head.getReference().next;
        while (nodeRef.getReference() !=null && nodeRef.isMarked()) {
            // Assist in remove marked nodes
            prevNodeRef = nodeRef;
            nodeRef = nodeRef.getReference().next;
            head.getReference().next.compareAndSet(prevNodeRef.getReference(), nodeRef.getReference(), true, true);
        }

        return nodeRef;
    }

    private void advanceTail() {
        Node tailNode = tail.getReference();
        Node tailNext = tailNode.next.getReference();
        while (tailNext != null) {
            // Assist in moving tail ref forward
            tail.compareAndSet(tailNode, tailNext, false, false);
            tailNode = tailNext;
            tailNext = tailNode.next.getReference();
        }
    }
}
