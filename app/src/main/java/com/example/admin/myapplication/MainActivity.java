package com.example.admin.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    Button enable, lock, disable;
    static public final int RESULT_ENABLE=1;
    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;
    ActivityManager activityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enable = (Button) findViewById(R.id.b_enable);
        lock = (Button) findViewById(R.id.b_lock);
        disable = (Button) findViewById(R.id.b_disable);
        devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        componentName = new ComponentName(this,Controller.class);

        boolean active = devicePolicyManager.isAdminActive(componentName);
        if(active){
            enable.setText("Disable");
            lock.setVisibility(View.VISIBLE);
        }
        else{
            enable.setText("Enable");
            lock.setVisibility(View.GONE);
        }

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if(active){
                    devicePolicyManager.removeActiveAdmin(componentName);
                    enable.setText("Enable");
                    lock.setVisibility(View.GONE);
                }else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"You should enable the app!");
                    startActivityForResult(intent,RESULT_ENABLE);
                }
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean active = devicePolicyManager.isAdminActive(componentName);
                if (active) {
                    devicePolicyManager.lockNow();
                } else {
                    Toast.makeText(MainActivity.this, "You need to enable Admin Device Features", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Succeeded", Toast.LENGTH_SHORT).show();
                    enable.setText("Disable");
                    lock.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
