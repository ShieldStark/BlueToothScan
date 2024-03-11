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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    List<DataValue> list;
    OnItemClickListener onItemClickListener;
    SparseBooleanArray selectedItems;
    OnItemLongClickListener onItemLongClickListener;


    public DataAdapter(List<DataValue> list) {
        this.list = list;
        selectedItems = new SparseBooleanArray();
    }
    public DataAdapter(List<DataValue> list, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
        selectedItems = new SparseBooleanArray();
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
        holder.itemView.setActivated(selectedItems.get(position, false));
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
        void onDeleteClick(int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }
    public void toggleSelection(int position) {
        // Toggle the selection state of the item
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void clearSelection() {
        // Clear all selected items
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        // Return the count of selected items
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        // Return a list of selected item positions
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        ImageView delete;
        TextView time;
        TextView dataType;
        TextView serialNumber;
        OnItemClickListener listener;
        public static final int DELETE_BUTTON_ID = 123;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            delete=itemView.findViewById(R.id.delete);
            time=itemView.findViewById(R.id.time);
            dataType=itemView.findViewById(R.id.source);
            serialNumber=itemView.findViewById(R.id.serialNumber);
            delete.setId(DELETE_BUTTON_ID);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                // Toggle the selection state of the item
                toggleSelection(position);
                return true;
            }
            return false;
        }
        public void toggleSelection(int position) {
            // Toggle the selection state of the item
            if (selectedItems.get(position, false)) {
                selectedItems.delete(position);
            } else {
                selectedItems.put(position, true);
            }
            notifyItemChanged(position);
        }

        public boolean isSelected(int position) {
            // Check if the item at the given position is selected
            return selectedItems.get(position, false);
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
    }
}
