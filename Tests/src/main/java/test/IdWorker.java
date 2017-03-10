package test;
/**
 * 核心代码为其IdWorker这个类实现，其原理结构如下，我分别用一个0表示一位，用—分割开部分的作用：
	1 	0---00000000000000000000000000000000000000000 --------- 00000 --------00000 ---000000000000
	         符号                                         毫秒级时间                                                                    	  datacenter标识位                    机器ID    当前毫秒内的计数
	在上面的字符串中，第一位为未使用（实际上也可作为long的符号位），
	接下来的41位为毫秒级时间，然后5位datacenter标识位，
	5位机器ID（并不算标识符，实际是为线程标识），
	然后12位该毫秒内的当前毫秒内的计数，加起来刚好64位，为一个Long型。
	这样的好处是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞（由datacenter和机器ID作区分），并且效率较高，经测试，snowflake每秒能够产生26万ID左右，完全满足需要。
 * @author Administrator
 *
 */

public class IdWorker {
	private final long workerId;
	private final static long twepoch = 1361753741828L;
	private long sequence = 0L;
	private final static long sequenceBits = 10L;
	private final static long workerIdShift = sequenceBits;
	private final static long workerIdBits = 4L;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	public final static long sequenceMask = -1L ^ -1L << sequenceBits;
	private long lastTimestamp = -1L;

	public IdWorker(final long workerId) {
		super();
		if (workerId > this.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & this.sequenceMask;
			if (this.sequence == 0) {
				System.out.println("###########" + sequenceMask);
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}

		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << this.workerIdShift) | (this.sequence);
		// System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
		// + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
		// + workerId + ",sequence:" + sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		IdWorker worker2 = new IdWorker(2);
		while(true){
			System.out.println(worker2.nextId());
		}
	}
}