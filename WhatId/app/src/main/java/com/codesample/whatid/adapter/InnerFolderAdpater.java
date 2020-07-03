package com.codesample.whatid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codesample.whatid.R;
import com.codesample.whatid.data.Account;
import com.codesample.whatid.data.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class InnerFolderAdpater extends RecyclerView.Adapter<InnerFolderAdpater.ViewHolder> {

    public interface OnListItemClickListener {
        void onListItemClick(Account account);
        void onListItemLongClick(Account account);
        void onStarClick(Account account, View v);
    }

    private List<Account> data;
    private List<Integer> accountsBookmarked;

    private OnListItemClickListener listener;

    public InnerFolderAdpater(OnListItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Account> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void updateBookmark(List<Integer> accountsBookmarked) {
        this.accountsBookmarked = accountsBookmarked;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = data.get(position);

        holder.url.setText(account.url);
        holder.memo.setText(account.memo);

        // 북마크된 계정이라면
        if(accountsBookmarked != null && accountsBookmarked.contains(account.id))
            holder.buttonStar.setImageResource(R.drawable.ic_star);
        else
            holder.buttonStar.setImageResource(R.drawable.ic_star_empty);

        holder.itemView.setOnClickListener(v -> {
            listener.onListItemClick(account);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onListItemLongClick(account);
            return true;
        });

        holder.buttonStar.setOnClickListener(v -> {
            listener.onStarClick(account, v);
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView url;
        TextView memo;
        ImageView buttonStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            url = itemView.findViewById(R.id.textViewPreviewURL);
            memo = itemView.findViewById(R.id.textViewPreviewMemo);
            buttonStar = itemView.findViewById(R.id.itemAccountButtonStar);
        }
    }
}
