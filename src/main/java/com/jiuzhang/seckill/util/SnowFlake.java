package com.jiuzhang.seckill.util;

public class SnowFlake {

    private static final long START_STAMP = 1223394858349L;

    private static final long DATACENTER_BIT = 5;
    private static final long MACHINE_BIT = 5;
    private static final long SEQUENCE_BIT = 12;

    private static final long MAX_DATACENTER_NUM = -1L ^ (-1L<<DATACENTER_BIT);
    private static final long MAX_MACHINE_NUM = -1L ^ (-1L<<MACHINE_BIT);
    private static final long MAX_SEQUENCE = -1L ^ (-1L<<SEQUENCE_BIT);

    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long dataCenterId;
    private long machineId;
    private long sequence=0L;
    private long lastStamp=-1L;

    public SnowFlake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long currStamp = getNewStamp();

        if (currStamp == lastStamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L){
                currStamp = getNextMills();
            }
        }else {
            sequence = 0;
        }
        lastStamp = currStamp;
        return (currStamp - START_STAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMills() {
        long mills = getNewStamp();
        while (mills <= lastStamp) {
            mills = getNewStamp();
        }
        return mills;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }
    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(30, 31);
        long startStamp = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            long stamp = snowFlake.nextId();
            System.out.println(stamp);
        }
        System.out.println("总耗时"+(System.currentTimeMillis() - startStamp));
    }

}
