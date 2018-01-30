
package com.coder.zzq.waybillscannerlib.bean;

import java.io.Serializable;

/**
 * Created by jetict on 2017-02-23.
 */
public class BillEntity implements Serializable {

    //xxxxxxxxxxxxxxx-xx-xx
    //第一部份是运单号
    //第二部份是总件数
    //第三部份是第几件
    private String billNo;
    private String receiveAddress;
    private String goodsTotal;
    private String goodsOrder;
    private String comment;

    private String DestinationBranchName;

    private float mFreight;
    private float mCollection;
    private int mPieces;


    public String getDestinationBranchName() {
        return DestinationBranchName;
    }

    public void setDestinationBranchName(String destinationBranchName) {
        DestinationBranchName = destinationBranchName;
    }

    public float getFreight() {
        return mFreight;
    }

    public void setFreight(float freight) {
        mFreight = freight;
    }

    public float getCollection() {
        return mCollection;
    }

    public void setCollection(float collection) {
        mCollection = collection;
    }

    public int getPieces() {
        return mPieces;
    }

    public void setPieces(int pieces) {
        mPieces = pieces;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(String goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public String getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(String goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
