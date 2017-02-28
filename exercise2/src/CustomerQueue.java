
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
	/**
	 * Creates a new customer queue. Make sure to save these variables in the class.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */

    private final Gui gui;
    private final int MAX_QUEUE_LENGTH;
    private ArrayList<Customer> customerQueue = new ArrayList<>();

    public CustomerQueue(int queueLength, Gui gui) {
        this.gui = gui;
        this.MAX_QUEUE_LENGTH = queueLength;
	}

	public synchronized void add(Customer customer) {
	    while(isFull()){
		try{
		    wait();}
		catch(Exeption e){}
	    }
        customerQueue.add(customer);
        gui.fillLoungeChair(customerQueue.size()-1, customer);
	notify();
	}

    public int size(){
        return customerQueue.size();
    }

	public synchronized Customer next() {
	    while(isEmpty()){
		try{
		    wait();}
		catch(Exeption e){}
	    }
        gui.emptyLoungeChair(customerQueue.size()-1);
        Customer customer = customerQueue.remove(0);
	notifyAll();
	return customer;
	}

    public boolean isEmpty(){
        return customerQueue.isEmpty();
    }

    public boolean isFull() {
        return customerQueue.size() == MAX_QUEUE_LENGTH;
    }
}
