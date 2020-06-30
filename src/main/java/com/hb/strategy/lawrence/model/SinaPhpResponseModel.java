package com.hb.strategy.lawrence.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.hb.strategy.lawrence.model.enums.KLineType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 新浪的价格实体
 */
public class SinaPhpResponseModel extends KLineModel {


    /**
     * {"d":"2020-06-04 14:05:00","o":"7381.000","h":"7391.000","l":"7376.000","c":"7386.000","v":"8258","p":"474758"}
     */

    Date datetime;

    BigDecimal open;

    BigDecimal high;

    BigDecimal low;

    BigDecimal close;

    BigDecimal volume;

    BigDecimal p;

    KLineType kLineType;

    BigDecimal highLowDValue;

    BigDecimal openCloseDValue;


    @JSONCreator
    public SinaPhpResponseModel(@JSONField(name = "d") Date datetime,
                                @JSONField(name = "o") BigDecimal open,
                                @JSONField(name = "h") BigDecimal high,
                                @JSONField(name = "l") BigDecimal low,
                                @JSONField(name = "c") BigDecimal close,
                                @JSONField(name = "v") BigDecimal volume,
                                @JSONField(name = "p") BigDecimal p) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.p = p;
        this.openCloseDValue = open.subtract(close);
        this.highLowDValue = high.subtract(low);
        this.kLineType = openCloseDValue.compareTo(BigDecimal.ZERO) <= 0 ?
                (openCloseDValue.compareTo(BigDecimal.ZERO) < 0 ?
                        KLineType.POSITIVE_LINE : KLineType.CROSS_LINE)
                : KLineType.NEGATIVE_LINE;
    }


    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    public BigDecimal getHighLowDValue() {
        return highLowDValue;
    }

    public void setHighLowDValue(BigDecimal highLowDValue) {
        this.highLowDValue = highLowDValue;
    }

    public BigDecimal getOpenCloseDValue() {
        return openCloseDValue;
    }

    public void setOpenCloseDValue(BigDecimal openCloseDValue) {
        this.openCloseDValue = openCloseDValue;
    }

    public KLineType getkLineType() {
        return kLineType;
    }

    public void setkLineType(KLineType kLineType) {
        this.kLineType = kLineType;
    }

    @Override
    public String toString() {
        return "SinaPhpResponseModel{" +
                "datetime=" + datetime +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", p=" + p +
                ", kLineType=" + kLineType +
                ", highLowDValue=" + highLowDValue +
                ", openCloseDValue=" + openCloseDValue +
                '}';
    }

    public static void main(String[] args) {
        String text = "[{\"d\":\"2020-06-04 14:05:00\",\"o\":\"7381.000\",\"h\":\"7391.000\",\"l\":\"7376.000\",\"c\":\"7381.000\",\"v\":\"8258\",\"p\":\"474758\"}]";

        List<SinaPhpResponseModel> list = JSONArray.parseArray(text, SinaPhpResponseModel.class);

        System.out.println(list.get(0));
    }


}
