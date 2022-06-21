package com.zy.foundation.did.business.service;

public interface WorkerIdAllocate {
    long allocateWorkerId(int workerIdBits, String tableName, String dbType);
}
