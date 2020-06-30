package com.hb.strategy.lawrence.strategy;

import com.hb.strategy.lawrence.model.KLineModel;

import java.util.List;

/**
 * 策略接口
 */
public interface Strategy {


    /**
     * 根据策略检查并提醒
     *
     * @param kLines
     */
    void checkAndRemind(String futureType, List<? extends KLineModel> kLines);


}
