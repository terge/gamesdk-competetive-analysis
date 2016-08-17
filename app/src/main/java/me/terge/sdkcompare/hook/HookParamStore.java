package me.terge.sdkcompare.hook;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by terge on 16-8-17.
 */
public class HookParamStore {
    private final static String FOLDER_PATH = Environment.getExternalStorageDirectory()+File.separator+"SdkCompare";
    private final static String FILE_NAME = "hookConfig";
    public static void save(Map<String,HookConfig> paramMap){
        if(paramMap == null || paramMap.size() == 0){
            return;
        }
        ensureFile();
        File configFile = new File(FOLDER_PATH,FILE_NAME);
        FileOutputStream fos = null;
        try {
            fos= new FileOutputStream(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(fos == null)return;
        Iterator<String> iterator = paramMap.keySet().iterator();
        JSONObject json = new JSONObject();
        String key;
        while (iterator.hasNext()){
            key = iterator.next();
            try {
                json.put(key,paramMap.get(key).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            fos.write(json.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos !=null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Map<String,HookConfig> read(){
        HashMap<String,HookConfig> configMap = new HashMap<>();
        File configFile = new File(FOLDER_PATH,FILE_NAME);
        if(configFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(configFile));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject json = null;
                json = new JSONObject(sb.toString());
                Iterator<String> iterator = json.keys();
                String key = null;
                while(iterator.hasNext()){
                    key = iterator.next();
                    configMap.put(key,(HookConfig) HookConfig.parseAVObject(json.optString(key,null)));
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
        return configMap;
    }

    private static void ensureFile() {
        File folder = new File(FOLDER_PATH);
        Log.e("terge","folder:"+FOLDER_PATH);
        if(!folder.exists())folder.mkdir();
        File file = new File(FOLDER_PATH,FILE_NAME);
        Log.e("terge","file:"+file.getAbsolutePath());
        if(file.exists())file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
