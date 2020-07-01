package com.codesample.whatid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codesample.whatid.R;
import com.codesample.whatid.data.Folder;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    public interface OnListItemClickListener {
        void onListItemClick(Folder folder);
        void onListItemLongClick(Folder folder);
    }

    private List<Folder> data;
    private OnListItemClickListener listener;

    public FolderAdapter(OnListItemClickListener listener) { this.listener = listener; }

    public void updateData(List<Folder> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder f = data.get(position);

        holder.name.setText(f.name);

        holder.itemView.setOnClickListener(v -> {
            listener.onListItemClick(f);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onListItemLongClick(f);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
        }
    }
}
