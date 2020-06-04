package com.codesample.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    public interface OnListItemClickListener {
        void onListItemClick(Memo memo);
        void onListItemLongClick(Memo memo);
    }

    private List<Memo> data;
    private OnListItemClickListener listener;

    public MemoAdapter(OnListItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Memo> data) {
        this.data=data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        Memo m = data.get(position);

        holder.time.setText(m.time);
        holder.title.setText(m.title);

        holder.itemView.setOnClickListener(v -> {
            listener.onListItemClick(m);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onListItemLongClick(m);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    class MemoViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView title;
        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textViewTime);
            title = itemView.findViewById(R.id.textViewTitle);
        }
    }
}
