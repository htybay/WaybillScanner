package com.coder.zzq.waybillscannerlib.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.coder.zzq.waybillscannerlib.R;

import java.util.List;

/**
 * Created by jetict on 2017/5/16.
 */
public class LoadAdapter extends BaseAdapter {
    private Context context;
    private List<TableRow> table;

    public LoadAdapter(Context context, List<TableRow> table) {
        this.context = context;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TableRow getItem(int position) {
        return table.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TableRow tableRow = table.get(position);
        return new TableRowView(this.context, tableRow);
    }

    /**
     * TableRowView 实现表格行的样式
     */
    class TableRowView extends LinearLayout {
        public TableRowView(Context context, TableRow tableRow) {
            super(context);

            this.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 0; i < tableRow.getSize(); i++) {//逐个单元格添加到行
                TableCell tableCell = tableRow.getCellValue(i);
                LayoutParams layoutParams = new LayoutParams(
                        tableCell.width, tableCell.height);//单元格大小
                layoutParams.setMargins(1, 0, 0, 1);//预留空隙制造边框

                TextView textCell = new TextView(context);
                textCell.setGravity(Gravity.CENTER);

                if (tableCell.type == TableCell.HEADER) {//表格头
                    textCell.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    textCell.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
                    textCell.setTextColor(Color.WHITE);
                } else if (tableCell.type == TableCell.BODY) {//表格内容
                    textCell.setBackgroundColor(Color.WHITE);
                    if (0 == tableCell.calculate) {
                        textCell.setTextColor(ContextCompat.getColor(context, R.color.grey_333333));
                    } else if (0 <= tableCell.calculate) {
                        textCell.setTextColor(ContextCompat.getColor(context, R.color.red_ff5858));
                    }
                }

                textCell.setText(String.valueOf(tableCell.value));
                addView(textCell, layoutParams);
            }
            this.setBackgroundColor(ContextCompat.getColor(context, R.color.text_grey_lightCC));//背景，利用空隙来实现边框
        }
    }

    /**
     * TableRow 实现表格的行
     */
    static public class TableRow {
        private TableCell[] cell;

        public TableRow(TableCell[] cell) {
            this.cell = cell;
        }

        private int getSize() {
            return cell.length;
        }

        public TableCell getCellValue(int index) {
            if (index >= cell.length)
                return null;
            return cell[index];
        }
    }

    /**
     * TableCell 实现表格的格单元
     */
    static public class
    TableCell {
        static public final int HEADER = 0;
        static public final int BODY = 1;
        private Object value;
        private int width;
        private int height;
        private int type;
        private int calculate;

        public TableCell(Object value, int width, int height, int type, int calculate) {
            this.value = value;
            this.width = width;
            this.height = height;
            this.type = type;
            this.calculate = calculate;
        }
        public String getValue() {
            return value.toString();
        }
    }
}