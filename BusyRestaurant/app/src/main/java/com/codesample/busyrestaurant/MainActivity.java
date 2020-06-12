package com.codesample.busyrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.codesample.busyrestaurant.databinding.ActivityMainBinding;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final int ORDER=1;
    private static final int COOKING=2;
    private static final int COOK=3;
    private static final int PACKING=4;
    private static final int PACK=5;

    private long orderDuration=1000;
    private long cookTime=1000;
    private long packTime=1000;
    private int orderCount=0;
    private boolean isStarted=false;
    private boolean isFinish=false;

    private ExecutorService service=null;
    private Handler handler;
    private Queue<String> orders = new LinkedList<>();
    private Queue<String> foods = new LinkedList<>();
    private Queue<String> packs = new LinkedList<>();

    private static final int COUNT=3;

    private Runnable order = () -> {
        Log.i("main", "order started");
        while(!isFinish) {
            orderCount++;
            String order = "order" + orderCount;
            orders.add(order);
            handler.sendMessage(Message.obtain(handler, ORDER, order));
            takeSleep(orderDuration);
        }
    };

    private Runnable cook = () -> {
        Log.i("main", "cook started");
        while(!isFinish) {
            if(!orders.isEmpty()) {
                String order = orders.remove();
                handler.sendMessage(Message.obtain(handler, COOKING, order));
                takeSleep(cookTime);
                foods.add(order);
                handler.sendMessage(Message.obtain(handler, COOK, order));
            } else {
                takeSleep(1000);
            }
        }
    };

    private Runnable pack = () -> {
        Log.i("main", "pack started");
        while(!isFinish) {
            if(!foods.isEmpty()) {
                String f = foods.remove();
                handler.sendMessage(Message.obtain(handler, PACKING, f));
                takeSleep(packTime);
                packs.add(f);
                handler.sendMessage(Message.obtain(handler, PACK, f));
            } else {
                takeSleep(1000);
            }
        }
    };
    private Runnable[] runnables = {order, cook, pack};

    private void takeSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        if(!isStarted) {
            isStarted = true;
            isFinish = false;
            binding.textViewOrderList.setText("");
            binding.textViewFoodList.setText("");
            binding.textViewPackList.setText("");
            binding.buttonStart.setText("Stop");
            orderDuration = Math.round(Double.parseDouble(binding.editTextOrder.getText().toString()) * 1000);
            cookTime = Math.round(Double.parseDouble(binding.editTextCook.getText().toString()) * 1000);
            packTime = Math.round(Double.parseDouble(binding.editTextPack.getText().toString()) * 1000);

            service = Executors.newFixedThreadPool(COUNT);
            for(int i = 0; i < COUNT; i++)
                service.execute(runnables[i]);
        } else {
            isStarted = false;
            isFinish = true;
            orders.clear();
            foods.clear();
            packs.clear();
            orderCount = 0;
            binding.buttonStart.setText("Start");
            if(service != null) service.shutdownNow();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handler = new Handler(msg -> {
            Queue<String> list = null;
            TextView view = null;
            switch(msg.what) {
                default:
                case ORDER:
                    list = orders;
                    view = binding.textViewOrderList;
                    break;
                case COOK:
                    binding.textViewCooking.setText("");
                    list = foods;
                    view = binding.textViewFoodList;
                    break;
                case PACK: binding.textViewPacking.setText("");
                    list = packs;
                    view = binding.textViewPackList;
                    break;
                case COOKING:
                    String str = "요리중\n" + msg.obj;
                    binding.textViewPacking.setText(str);
                    break;
                case PACKING:
                    str="포장중\n" + msg.obj;
                    binding.textViewPacking.setText(str);
                    break;
            }
            if(view != null && list != null) {
                view.setText("");
                for(String s : list) {
                    view.append(s + "\n");
                }
            }
            return false;
        });

        binding.buttonStart.setOnClickListener(v -> {
            start();
        });
    }
}
