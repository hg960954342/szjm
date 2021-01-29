package com.prolog.eis.scheduler.async;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 异步控制类
 */
@Component
public class AsyncConfiguration {
    private final Set<String> asyncSet = new ConcurrentSkipListSet<String>();

    public Set<String> getAsyncSet() {
        return asyncSet;
    }


}
