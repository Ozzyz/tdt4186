package round_robin;

import java.util.LinkedList;

/**
 * This class implements functionality associated with
 * the CPU unit of the simulated system.
 */
public class Cpu {
    /**
     * Creates a new CPU with the given parameters.
     * @param cpuQueue		The CPU queue to be used.
     * @param maxCpuTime	The Round Robin time quant to be used.
     * @param statistics	A reference to the statistics collector.
     */

    private final LinkedList<Process> cpuQueue;
    private long maxCpuTime;
    private Statistics statistics;
    private Process activeProcess = null;

    public Cpu(LinkedList<Process> cpuQueue, long maxCpuTime, Statistics statistics) {
        this.cpuQueue = cpuQueue;
        this.maxCpuTime = maxCpuTime;
        this.statistics = statistics;

    }

    /**
     * Adds a process to the CPU queue, and activates (switches in) the first process
     * in the CPU queue if the CPU is idle.
     * @param p		The process to be added to the CPU queue.
     * @param clock	The global time.
     * @return		The event causing the process that was activated to leave the CPU,
     *				or null	if no process was activated.
     */
    public Event insertProcess(Process p, long clock) {
        //System.out.printf("Inserting %d in cpuqueue at time %d\n", p.getProcessId(), clock);
        cpuQueue.add(p);
        if(isIdle()){
            return switchProcess(clock);
        }
        return null;
    }

    /**
     * Activates (switches in) the first process in the CPU queue, if the queue is non-empty.
     * The process that was using the CPU, if any, is switched out and added to the back of
     * the CPU queue, in accordance with the Round Robin algorithm.
     * @param clock	The global time.
     * @return		The event causing the process that was activated to leave the CPU,
     *				or null	if no process was activated.
     */
    public Event switchProcess(long clock) {
        if(cpuQueue.isEmpty()){
            activeProcess = null;
            return null;
        }
        activeProcess = cpuQueue.remove();
        activeProcess.timeSpentInReadyQueue(clock);
        // If the process is done after this cycle, and no io is needed, end the process after processing
        if(activeProcess.getCpuTimeNeeded() <= maxCpuTime && activeProcess.getCpuTimeNeeded() <= activeProcess.getTimeToNextIoOperation()){
            return new Event(Event.END_PROCESS, clock+activeProcess.getCpuTimeNeeded());
        }
        // If there is a io request in this cycle, send an io request
        else if(activeProcess.getTimeToNextIoOperation() <= maxCpuTime){
            return new Event(Event.IO_REQUEST, clock+activeProcess.getTimeToNextIoOperation());
        }

        else{
            // Let the process use its time quantum
            return new Event(Event.SWITCH_PROCESS, clock+maxCpuTime);
        }
    }

    /**
     * Called when the active process left the CPU (for example to perform I/O),
     * and a new process needs to be switched in.
     * @return	The event generated by the process switch, or null if no new
     *			process was switched in.
     */
    public Event activeProcessLeft(long clock) {
        if(activeProcess != null) activeProcess.timeSpentInCpu(clock);
        return switchProcess(clock);
    }

    /**
     * Returns the process currently using the CPU.
     * @return	The process currently using the CPU.
     */
    public Process getActiveProcess() {
        return activeProcess;
    }


    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        if(activeProcess != null) statistics.totalBusyCpuTime += timePassed;
        statistics.cpuQueueLengthTime += cpuQueue.size()*timePassed;
        if (cpuQueue.size() > statistics.cpuQueueLargestLength) {
            statistics.cpuQueueLargestLength = cpuQueue.size();
        }

    }

    public boolean isIdle(){
        return activeProcess == null;
    }

}
