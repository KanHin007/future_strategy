package com.hb.strategy.lawrence.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新浪的价格实体
 */
public class SinaPhpResponseModel {


    /**
     * {"d":"2020-06-04 14:05:00","o":"7381.000","h":"7391.000","l":"7376.000","c":"7386.000","v":"8258","p":"474758"}
     */
    @JSONField(name = "d")
    Date datetime;

    @JSONField(name = "o")
    BigDecimal open;

    @JSONField(name = "h")
    BigDecimal high;

    @JSONField(name = "l")
    BigDecimal low;

    @JSONField(name = "c")
    BigDecimal close;

    @JSONField(name = "v")
    BigDecimal volume;

    @JSONField(name = "p")
    BigDecimal p;


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
                '}';
    }
}
