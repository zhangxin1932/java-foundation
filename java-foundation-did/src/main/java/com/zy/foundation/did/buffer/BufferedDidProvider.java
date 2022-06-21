package com.zy.foundation.did.buffer;

import java.util.List;

public interface BufferedDidProvider {
    List<Long> provide(long did);
}
