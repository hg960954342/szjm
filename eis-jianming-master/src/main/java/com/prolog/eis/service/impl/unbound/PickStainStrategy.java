package com.prolog.eis.service.impl.unbound;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * @description 获取拣选站编号
 * @date 2020/8/28 14:04
 */
@Component
@Scope(SCOPE_SINGLETON)
public class PickStainStrategy {

    /**
     * 随机获取
     * @param bound
     * @return
     */
    public int getIndexPickStain(int bound){
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
       return threadLocalRandom.nextInt(bound);
    }

}
