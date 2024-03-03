package com.example.bluetoothscan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    List<DataValue> list;

    public DataAdapter(List<DataValue> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public DataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycle_view_item,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {
        holder.time.setText(list.get(position).getTime());
        holder.dataType.setText(list.get(position).getDataType());
        holder.serialNumber.setText(list.get(position).getSerialNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView time;
        TextView dataType;
        TextView serialNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.selectCheckBox);
            time=itemView.findViewById(R.id.time);
            dataType=itemView.findViewById(R.id.dataType);
            serialNumber=itemView.findViewById(R.id.serialNumber);
        }
    }
}
