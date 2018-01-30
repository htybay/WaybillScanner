package com.coder.zzq.waybillscannerlib.adpter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.coder.zzq.waybillscannerlib.R;
import com.coder.zzq.waybillscannerlib.bean.BillEntity;

import java.util.List;

/**
 * Created by 小海豹 on 2017-02-23.
 */
public class BillInfoAdapter extends BaseQuickAdapter<BillEntity> {


    public BillInfoAdapter(List<BillEntity> data) {
        super(R.layout.item_bill, data);
    }
    public BillInfoAdapter(List<BillEntity> data, String strItem) {
        super(R.layout.item_bill, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, BillEntity billEntity) {
        baseViewHolder.setText(R.id.item_billno, "运单号: " + billEntity.getBillNo())
                .setText(R.id.item_address, "目的地: " + billEntity.getDestinationBranchName())
                .setText(R.id.item_goods_total, "总件数: " + billEntity.getGoodsTotal())
                .setText(R.id.item_goods_order,"第 " +billEntity.getGoodsOrder() + " 件");
    }
}
