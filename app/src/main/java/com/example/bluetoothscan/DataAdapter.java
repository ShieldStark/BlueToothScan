package com.example.bluetoothscan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    List<DataValue> list;
    OnItemClickListener onItemClickListener;

    public DataAdapter(List<DataValue> list) {
        this.list = list;
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
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
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }
    public void addAll(List<DataValue> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CheckBox checkBox;
        TextView time;
        TextView dataType;
        TextView serialNumber;
        OnItemClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.selectCheckBox);
            time=itemView.findViewById(R.id.time);
            dataType=itemView.findViewById(R.id.dataType);
            serialNumber=itemView.findViewById(R.id.serialNumber);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            }
        }

    }
}
