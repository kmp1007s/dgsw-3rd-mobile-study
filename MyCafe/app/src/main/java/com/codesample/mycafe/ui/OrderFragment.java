package com.codesample.mycafe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.codesample.mycafe.MainViewModel;
import com.codesample.mycafe.databinding.FragmentOrderBinding;

import java.util.Locale;

public class OrderFragment extends Fragment {
    private MainViewModel viewModel;
    private FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding = FragmentOrderBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getUserId().observe(getViewLifecycleOwner(), userid->{
            if(userid!=null)
                binding.textViewUserID.setText(userid);
        });
        viewModel.getSelected().observe(getViewLifecycleOwner(), menu->{
            if(menu.count > 0) {
                String str = String.format(Locale.KOREA, "%s(%,d원) - %d잔", menu.name, menu.price, menu.count);
                binding.textViewOrder.setText(str);
            }
        });
    }
}
