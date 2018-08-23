package com.example.youse.financemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class financeAdapter extends ArrayAdapter<financeAdapter> {
    financeModule[] modules;
    Context myContext;
    public financeAdapter(@NonNull Context context, financeModule[] finances ) {
        super(context, R.layout.listview_item);
        this.modules = finances;
        this.myContext = context;
    }

    @Override
    public int getCount() {
        return modules.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) { //if we haven't loaded the list item before
            LayoutInflater mInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.listview_item, parent, false);

            mViewHolder.mPosNeg = (ImageView) convertView.findViewById(R.id.viewPlusMinus);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.nameText);
            mViewHolder.mValue = (TextView) convertView.findViewById(R.id.valueText);
            convertView.setTag(mViewHolder);

        }else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mPosNeg.setImageResource(modules[position].getPic());
        mViewHolder.mName.setText(modules[position].getFinanceName());
        mViewHolder.mValue.setText(modules[position].getValueText());
        return convertView;
    }

    static class ViewHolder{
        ImageView mPosNeg;
        TextView mName;
        TextView mValue;
    }

}
