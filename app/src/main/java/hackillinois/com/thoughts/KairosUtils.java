package hackillinois.com.thoughts;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jay on 2/20/16.
 */
public class KairosUtils {
    private static Kairos myKairos;
    private static KairosListener listener;
    private static KairosListener recognizeListener;
    private static Map<String, Long> users;
    private static List<String> galleries;
    private static Long uID;

    public static void init(Context c) {
        final int DEFAULT_TIMEOUT = 20 * 1000;
        AsyncHttpClient aClient = new AsyncHttpClient();
        aClient.setTimeout(DEFAULT_TIMEOUT);
        galleries = new LinkedList<String>();
        myKairos = new Kairos();
        // set authentication
        String app_id = "6354cb00";
        String api_key = "7323737bd141f7710b54fd3e8c71dbaf";
        myKairos.setAuthentication(c, app_id, api_key);
        listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };

        recognizeListener = new KairosListener() {

            @Override
            public void onSuccess(String s) {
                try {
                    Log.d("KAIROSUTILS", "REACHED HERE");
                    Log.d("KAIROSUTILS", s);
                    JSONObject obj = new JSONObject(s);
                    JSONArray imagesArr = (JSONArray) obj.get("images");
                    JSONObject highestConf = (JSONObject) imagesArr.get(0);
                    JSONObject jobj = (JSONObject) highestConf.get("transaction");
                    String subj = (String) jobj.get("subject");
                    Log.d("KAIROSUTILS", subj);
                    uID = Long.parseLong(subj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String s) {
                Log.d("KAIROSUTILS", s);
            }
        };
    }

    public static void enrollUsers(Map<String, Long> map, Bitmap bmp, String pic, double lat, double lng) {
        String gallery = (int)lat + "D" + (int)((lat - (int)lat) * 10000000) + " " + (int)lng + "D" + (int)((lng - (int)lng) * 10000000);
        Log.d("AYYO", gallery);
        boolean galleryExists = false;
        for (String s : galleries) {
            String[] arr = s.split(" ");
            String[] temp = arr[0].split("D");
            String[] temp2 = arr[1].split("D");
            if(Math.abs(Double.parseDouble(temp[0] + temp[1]) - lat) < 0.05
                    && Math.abs(Double.parseDouble(temp2[0] + temp2[1]) - lng) < 0.05) {
                    galleryExists = true;
                    gallery = Integer.parseInt(arr[0]) + " " + Integer.parseInt(arr[1]);
            }
        }
        if(!galleryExists) {
            galleries.add(gallery);
            for (String s : map.keySet()) {
                try {
                    myKairos.enroll(s, map.get(s) + "", gallery, "FACE", "false", "0.125", listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            myKairos.recognize(bmp, gallery, "FACE", "0.1", "0.125", "5", recognizeListener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*while(uID == null) {
            //wait for task to finish
        }*/
        Log.d("LONG ID", uID + "");
    }

    public static void enroll(Bitmap rotatedBmp, String pic, String gallery) {
        try {
            myKairos.enroll(rotatedBmp, pic, gallery, "FACE", "false", "0.125", listener);
        } catch(JSONException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}

//KairosUtils.enroll
//
