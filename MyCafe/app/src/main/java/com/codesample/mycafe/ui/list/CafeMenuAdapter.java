package com.codesample.mycafe.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.codesample.mycafe.R;
import com.codesample.mycafe.data.CafeMenu;

import java.util.List;
import java.util.Locale;

public class CafeMenuAdapter extends RecyclerView.Adapter<CafeMenuAdapter.MenuViewHolder> {
    class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            price = itemView.findViewById(R.id.textViewPrice);
        }
    }
    public interface OnListItemListener {
        public void onItemClick(CafeMenu menu);
    }

    private OnListItemListener listener;
    private List<CafeMenu> menus;

    public CafeMenuAdapter(OnListItemListener listener) { this.listener = listener; }
    public void setMenus(List<CafeMenu> menus) { this.menus = menus; }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        CafeMenu menu = menus.get(position);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(menu));
        holder.name.setText(menu.name);
        holder.price.setText(String.format(Locale.KOREA, "%,d원", menu.price));
    }

    @Override
    public int getItemCount() {
        return menus == null ? 0 : menus.size();
    }
}
