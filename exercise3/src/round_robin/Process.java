package round_robin;

import java.util.Random;

/**
 * This class contains data associated with processes,
 * and methods for manipulating this data as well as
 * methods for displaying a process in the GUI.
 *
 * You will probably want to add more methods to this class.
 */
public class Process {
	/** The ID of the next process to be created */
	private static long nextProcessId = 1;
	/** The ID of this process */
	private long processId;
	/** The amount of memory needed by this process */
    private long memoryNeeded;
	/** The amount of cpu time still needed by this process */
    private long cpuTimeNeeded;
	/** The average time between the need for I/O operations for this process */
    private long avgIoInterval;
	/** The time left until the next time this process needs I/O */
    private long timeToNextIoOperation = 0;

	/** The time that this process has spent waiting in the memory queue */
	private long timeSpentWaitingForMemory = 0;
	/** The time that this process has spent waiting in the CPU queue */
	private long timeSpentInReadyQueue = 0;
	/** The time that this process has spent processing */
    private long timeSpentInCpu = 0;
	/** The time that this process has spent waiting in the I/O queue */
    private long timeSpentWaitingForIo = 0;
	/** The time that this process has spent performing I/O */
	private long timeSpentInIo = 0;

	/** The number of times that this process has been placed in the CPU queue */
	private long nofTimesInReadyQueue = 0;
	/** The number of times that this process has been placed in the I/O queue */
	private long nofTimesInIoQueue = 0;

	/** The global time of the last event involving this process */
	private long timeOfLastEvent;

	/**
	 * Creates a new process with given parameters. Other parameters are randomly
	 * determined.
	 * @param memorySize	The size of the memory unit.
	 * @param creationTime	The global time when this process is created.
	 */
	public Process(long memorySize, long creationTime) {
		// Memory need varies from 100 kB to 25% of memory size
		memoryNeeded = 100 + (long)(Math.random()*(memorySize/4-100));
		// CPU time needed varies from 100 to 10000 milliseconds
		cpuTimeNeeded = 100 + (long)(Math.random()*9900);
		// Average interval between I/O requests varies from 1% to 25% of CPU time needed
		avgIoInterval = (1 + (long)(Math.random()*25))*cpuTimeNeeded/100;
		// The first and latest event involving this process is its creation
		timeOfLastEvent = creationTime;
		// Assign a process ID
		processId = nextProcessId++;

		timeToNextIoOperation = getTimeToNextIoOperation();
	}

	/**
	 * This method is called when the process leaves the memory queue (and
	 * enters the cpu queue).
     * @param clock The time when the process leaves the memory queue.
     */
    public void leftMemoryQueue(long clock) {
		  timeSpentWaitingForMemory += clock - timeOfLastEvent;
		  timeOfLastEvent = clock;
    }

	public void timeSpentInReadyQueue(long clock){
		nofTimesInReadyQueue++;
        timeSpentInReadyQueue += clock - timeOfLastEvent;
		timeOfLastEvent = clock;
	}

	public void timeSpentInCpu(long clock){
		long time = clock - timeOfLastEvent;

        timeSpentInCpu += time;
		setCpuTimeNeeded(cpuTimeNeeded-time);
		setTimeToNextIoOperation(timeToNextIoOperation-time);
		timeOfLastEvent = clock;
	}



	public void timeSpentInIOQueue(long clock){
		nofTimesInIoQueue++;
		timeSpentWaitingForIo += clock - timeOfLastEvent;
		timeOfLastEvent = clock;
	}

	public void timeSpentInIO(long clock){
		timeSpentInIo += clock - timeOfLastEvent;
		timeToNextIoOperation = avgIoInterval;
		timeOfLastEvent = clock;
		timeToNextIoOperation = getTimeToNextIoOperation();
	}
    /**
	 * Returns the amount of memory needed by this process.
     * @return	The amount of memory needed by this process.
     */
	public long getMemoryNeeded() {
		return memoryNeeded;
	}

    /**
	 * Updates the statistics collected by the given Statistic object, adding
	 * data collected by this process. This method is called when the process
	 * leaves the system.
     * @param statistics	The Statistics object to be updated.
     */
	public void updateStatistics(Statistics statistics) {
		statistics.totalTimeSpentWaitingForMemory += timeSpentWaitingForMemory;
        statistics.totalTimeSpentInReadyQueue += timeSpentInReadyQueue;
        statistics.totalTimeSpentInCpu += timeSpentInCpu;
        statistics.totalTimeSpentWaitingForIo += timeSpentWaitingForIo;
        statistics.totalTimeSpentInIo += timeSpentInIo;

        statistics.totalNofTimesInReadyQueue += nofTimesInReadyQueue;
        statistics.totalNofTimesInIoQueue += nofTimesInIoQueue;
}

	public long getProcessId() {
		return processId;
	}


	public long getCpuTimeNeeded(){
		return cpuTimeNeeded;
	}

	public long getTimeToNextIoOperation(){
		if(timeToNextIoOperation <= 0){
			// generate new value for each time the process is in the cpu
			// The time to next io is random within certain limits of the io interval
			Random random = new Random();
			int max = (int) (1.2*avgIoInterval);
			int min = (int) (0.8*avgIoInterval);
			timeToNextIoOperation = (long) random.nextInt(max+1-min)+min;
		}
		return timeToNextIoOperation;
	}

	public void setTimeToNextIoOperation(long time){
		if(time < 0){
            timeToNextIoOperation = 0;
		}else{
			timeToNextIoOperation = time;
		}
	}

	public void setCpuTimeNeeded(long time){
		if(time < 0){
			cpuTimeNeeded = 0;
		}else{
			cpuTimeNeeded = time;
		}
	}


}