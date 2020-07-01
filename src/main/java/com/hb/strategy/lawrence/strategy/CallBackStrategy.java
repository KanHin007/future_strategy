package com.hb.strategy.lawrence.strategy;

import com.hb.strategy.lawrence.model.KLineModel;
import com.hb.strategy.lawrence.model.SinaPhpResponseModel;
import com.hb.strategy.lawrence.model.enums.KLineType;
import com.hb.strategy.lawrence.util.DingDingSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 回调策略
 */
@Service("callBackStrategy")
public class CallBackStrategy implements Strategy {

    private static final BigDecimal DOUBLE_MULTI = new BigDecimal("2");

    private static Logger logger = LoggerFactory.getLogger(CallBackStrategy.class);

    @Override
    public void checkAndRemind(String futureType, List<? extends KLineModel> kLines) {
        int size = kLines.size();
        // k线数量大于10
        if (size > 10) {
            Date currentTime = new Date();
            // 选取最近的两根k线进行吞没判断
            SinaPhpResponseModel preModel = (SinaPhpResponseModel) kLines.get(size - 3);
            SinaPhpResponseModel lastModel = (SinaPhpResponseModel) kLines.get(size - 2);
            // 首先确认是否吞没
            //   int result = checkEmbezzle(preModel, lastModel);
            int result = checkEmbezzleWithHatch(preModel, lastModel);
            // 确定有了吞没
            if (result != 0) {
                String description = result == -1 ? "阴包阳" : "阳包阴";
                description += "吞没";
                //  logger.info("时间{},,,当前品种：{}   出现了{}", currentTime, futureType, description);
                //  DingDingSender.sendMessage("当前时间 " + currentTime + " | 当前品种 " + futureType + " | 出现了 " + description);
                // 接着判断趋势,选取前几根k线，进行判断，
                BigDecimal compareLength = lastModel.getOpenCloseDValue().abs().multiply(DOUBLE_MULTI);
                for (int i = size - 4; i >= size - 10; i--) {
                    SinaPhpResponseModel temp = (SinaPhpResponseModel) kLines.get(i);
                    // 如果当前的最高价-最低价的长度 大于 最后一根k的两杯,
                    // 则判断前方有过相同方向的暴涨暴跌
                    if (temp.getkLineType() == lastModel.getkLineType() &&
                            temp.getHighLowDValue().abs().compareTo(compareLength) >= 0 &&
                            ((temp.getkLineType() == KLineType.POSITIVE_LINE && temp.getOpen().compareTo(lastModel.getOpen()) < 0)
                                    || (temp.getkLineType() == KLineType.NEGATIVE_LINE && temp.getOpen().compareTo(lastModel.getOpen()) > 0))) {
                        //打印日志，发送钉钉，直接返回
                        logger.info("时间{},,,当前品种：{}   出现了{} ，，， + 同方向的回调", currentTime, futureType, description);
                        DingDingSender.sendMessage("当前时间 " + currentTime + " | 当前品种 " + futureType + " | 出现了 " + description + " +同方向的回调");
                        return;
                    }
                }

            }
        }
    }

    /**
     * 检查当前前一根k线和前前一根K线是否存在吞没或者十字形线
     * <p>
     * 拓展性不好，以后再改
     *
     * @return
     */
    private int checkEmbezzle(SinaPhpResponseModel pre, SinaPhpResponseModel last) {
        // 前阳后阴 吞没
        if (pre.getkLineType() == KLineType.POSITIVE_LINE &&
                last.getkLineType() == KLineType.NEGATIVE_LINE &&
                pre.getClose().compareTo(last.getOpen()) <= 0 &&
                pre.getOpen().compareTo(last.getClose()) >= 0) {
            return -1;
        }
        // 前阴后阳 吞没
        else if (pre.getkLineType() == KLineType.NEGATIVE_LINE &&
                last.getkLineType() == KLineType.POSITIVE_LINE &&
                pre.getClose().compareTo(last.getOpen()) >= 0 &&
                pre.getOpen().compareTo(last.getClose()) <= 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int checkEmbezzleWithHatch(SinaPhpResponseModel pre, SinaPhpResponseModel last) {
        // 前阳后阴 吞没 并且阳线的上影线很长 是实体的两倍
        if (pre.getkLineType() == KLineType.POSITIVE_LINE &&
                last.getkLineType() == KLineType.NEGATIVE_LINE &&
                pre.getClose().compareTo(last.getOpen()) <= 0 &&
                pre.getOpen().compareTo(last.getClose()) >= 0 &&
                pre.getHigh().subtract(pre.getClose()).compareTo(pre.getOpenCloseDValue().abs().multiply(DOUBLE_MULTI)) >= 0) {
            return -1;
        }
        // 前阴后阳 吞没 并且阴线的下影线很长 是实体的两倍
        else if (pre.getkLineType() == KLineType.NEGATIVE_LINE &&
                last.getkLineType() == KLineType.POSITIVE_LINE &&
                pre.getClose().compareTo(last.getOpen()) >= 0 &&
                pre.getOpen().compareTo(last.getClose()) <= 0 &&
                pre.getClose().subtract(pre.getLow()).compareTo(pre.getOpenCloseDValue().multiply(DOUBLE_MULTI)) >= 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
