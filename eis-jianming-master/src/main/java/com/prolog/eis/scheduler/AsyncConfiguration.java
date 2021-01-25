package com.prolog.eis.scheduler;

import org.springframework.stereotype.Component;


import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


@Component
public class AsyncConfiguration {
    private  final Set<String> asyncSet=new ConcurrentSkipListSet<String>();

    public  Set<String> getAsyncSet() {
        return asyncSet;
    }
}
