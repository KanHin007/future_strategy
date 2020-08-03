package com.hb.strategy.lawrence.outer.model;

import java.math.BigDecimal;

public class CrawlerResult {

    private String futureName;

    /**
     * 之前的收盘价格
     */
    private BigDecimal preClosePrice;

    /**
     * 当前价格
     */
    private BigDecimal nowPrice;

    /**
     * 两者之间的差值
     */
    private BigDecimal subtractPrice;

    public CrawlerResult(String futureName, BigDecimal preClosePrice, BigDecimal nowPrice) {
        this.futureName = futureName;
        this.preClosePrice = preClosePrice;
        this.nowPrice = nowPrice;
        if(preClosePrice!=null && nowPrice != null) {
            this.subtractPrice = nowPrice.subtract(preClosePrice);
        }
    }

    @Override
    public String toString() {
        return "CrawlerResult{" +
                "futureName='" + futureName + '\'' +
                ", preClosePrice=" + preClosePrice +
                ", nowPrice=" + nowPrice +
                ", subtractPrice=" + subtractPrice +
                '}';
    }

    public String getFutureName() {
        return futureName;
    }

    public void setFutureName(String futureName) {
        this.futureName = futureName;
    }

    public BigDecimal getPreClosePrice() {
        return preClosePrice;
    }

    public void setPreClosePrice(BigDecimal preClosePrice) {
        this.preClosePrice = preClosePrice;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    public BigDecimal getSubtractPrice() {
        return subtractPrice;
    }

    public void setSubtractPrice(BigDecimal subtractPrice) {
        this.subtractPrice = subtractPrice;
    }
}
