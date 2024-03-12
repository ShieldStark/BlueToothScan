package com.example.bluetoothscan;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    List<DataValue> list;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    List<Integer> selectedItems;
    private int selectedItemIndex = -1;


    public DataAdapter(List<DataValue> list) {
        this.list = list;
        //selectedItems = new SparseBooleanArray();
    }
    public DataAdapter(List<DataValue> list, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.selectedItems=new ArrayList<>();
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
        if (selectedItems != null &&selectedItems.contains(position)) {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selectedItemColor));
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(android.R.color.transparent));
        }
        holder.itemView.setOnLongClickListener(view -> {
            // Toggle selection on long press
            toggleSelection(position);
            return true; // consume the long click
        });

        holder.itemView.setOnClickListener(view -> {
            if (selectedItems.isEmpty()) {
                // Launch other activity if no item is selected
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            } else {
                // Toggle selection on click if an item is selected
                toggleSelection(position);
            }
        });
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    public void setSelectedItem(List<Integer> selectedItems) {
        this.selectedItems=selectedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        ImageView delete;
        TextView time;
        TextView dataType;
        TextView serialNumber;
        OnItemClickListener listener;
        CardView cardView;
        public static final int DELETE_BUTTON_ID = 123;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete);
            time=itemView.findViewById(R.id.time);
            dataType=itemView.findViewById(R.id.source);
            serialNumber=itemView.findViewById(R.id.serialNumber);
            cardView=itemView.findViewById(R.id.cardView);
            delete.setId(DELETE_BUTTON_ID);
            delete.setOnClickListener(this);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                switch (view.getId()) {
                    case DELETE_BUTTON_ID:
                        if (onItemClickListener != null) {
                            onItemClickListener.onDeleteClick(position);
                        }
                        break;
                    default:
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                        break;
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(position);
                    return true; // consume the long click
                }
            }
            return false;
        }
    }
    private void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(Integer.valueOf(position));
        } else {
            selectedItems.add(position);
        }
        notifyDataSetChanged();
    }
}
