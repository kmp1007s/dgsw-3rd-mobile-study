package com.codesample.mycafe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codesample.mycafe.MainViewModel;
import com.codesample.mycafe.R;
import com.codesample.mycafe.data.CafeMenu;
import com.codesample.mycafe.data.Dummy;
import com.codesample.mycafe.databinding.FragmentMenuBinding;
import com.codesample.mycafe.ui.list.CafeMenuAdapter;

import java.util.List;

public class MenuFragment extends Fragment implements CafeMenuAdapter.OnListItemListener {
    private MainViewModel viewModel;
    private FragmentMenuBinding binding;
    private NavController controller;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding=FragmentMenuBinding.inflate(inflater);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller=Navigation.findNavController(view);
        List<CafeMenu> menus = Dummy.getInstance().getMenus();
        CafeMenuAdapter adapter= new CafeMenuAdapter(this);
        adapter.setMenus(menus);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
    @Override
    public void onItemClick(CafeMenu menu) {
        viewModel.setSelected(menu);
        controller.navigate(R.id.action_navigation_menu_to_navigation_menu_detail);
    }
}
