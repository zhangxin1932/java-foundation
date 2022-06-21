package com.zy.foundation.did.buffer;

public interface RejectedPutBufferHandler {
    void rejectPutBuffer(RingBuffer ringBuffer, long did);
}
