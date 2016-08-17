package me.terge.sdkcompare;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.terge.sdkcompare.hook.remoteconfig.HookConfig;
import me.terge.sdkcompare.hook.remoteconfig.HookConfigStore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "获取HookConfig", Toast.LENGTH_SHORT).show();
                findHookConfig();
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100);


    }

    private void findHookConfig() {

        AVQuery<HookConfig> query = AVQuery.getQuery(HookConfig.class);
        query.findInBackground(new FindCallback<HookConfig>() {
            @Override
            public void done(List<HookConfig> list, AVException e) {
                if(list == null){
                    list = new ArrayList<HookConfig>();
                    Log.e("terge","hookParamList is null");
                }
                HashMap<String,HookConfig> hookParamMap=new HashMap<>();
                for(HookConfig hparam:list){
                    Log.d("terge","hook param:"+hparam.toString());
                    hookParamMap.put(hparam.getPkgName()+hparam.getPlatform(),hparam);
                }
                HookConfigStore.save(hookParamMap);
            }
        });

    }



}
