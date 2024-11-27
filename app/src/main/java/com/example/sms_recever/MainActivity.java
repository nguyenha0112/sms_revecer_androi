package com.example.sms_recever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView messageListView;
    private ArrayAdapter<String> messageAdapter;
    private ArrayList<String> messageList;
    private int selectedPosition = -1; // Lưu trữ vị trí mục được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageList = new ArrayList<>();

        messageAdapter = new ArrayAdapter<String>(this, R.layout.list_item_layout, messageList);
        messageListView = findViewById(R.id.messageListView);
        messageListView.setAdapter(messageAdapter);

        // Kiểm tra và yêu cầu quyền truy cập đọc tin nhắn
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 1);
            return;
        }

        // Sự kiện khi chọn một mục trong ListView
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Xử lý khi chọn một mục trong ListView
                selectedPosition = position; // Lưu trữ vị trí mục được chọn
                String selectedItem = messageList.get(position);
                Toast.makeText(MainActivity.this, "Bạn đã chọn: " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                updateMessageList(message);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("newSmsReceived"));

        // Sự kiện khi nhấn nút "Xóa"
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa mục tại vị trí được chọn từ ListView
                if (selectedPosition != -1) {
                    messageList.remove(selectedPosition);
                    messageAdapter.notifyDataSetChanged();
                    selectedPosition = -1; // Reset vị trí được chọn
                }
            }
        });
    }

    public void updateMessageList(String message) {
        messageList.add(message);
        messageAdapter.notifyDataSetChanged();
    }
}