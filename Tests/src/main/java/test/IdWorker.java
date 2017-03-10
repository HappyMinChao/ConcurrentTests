package test;
/**
 * ���Ĵ���Ϊ��IdWorker�����ʵ�֣���ԭ��ṹ���£��ҷֱ���һ��0��ʾһλ���á��ָ���ֵ����ã�
	1 	0---00000000000000000000000000000000000000000 --------- 00000 --------00000 ---000000000000
	         ����                                         ���뼶ʱ��                                                                    	  datacenter��ʶλ                    ����ID    ��ǰ�����ڵļ���
	��������ַ����У���һλΪδʹ�ã�ʵ����Ҳ����Ϊlong�ķ���λ����
	��������41λΪ���뼶ʱ�䣬Ȼ��5λdatacenter��ʶλ��
	5λ����ID���������ʶ����ʵ����Ϊ�̱߳�ʶ����
	Ȼ��12λ�ú����ڵĵ�ǰ�����ڵļ������������պ�64λ��Ϊһ��Long�͡�
	�����ĺô��ǣ������ϰ���ʱ���������򣬲��������ֲ�ʽϵͳ�ڲ������ID��ײ����datacenter�ͻ���ID�����֣�������Ч�ʽϸߣ������ԣ�snowflakeÿ���ܹ�����26��ID���ң���ȫ������Ҫ��
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