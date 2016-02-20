package hackillinois.com.thoughts;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Jay on 2/20/16.
 */
public class KairosUtils {
    private static Kairos myKairos;
    private static KairosListener listener;
    private static KairosListener recognizeListener;
    private static Map<String, Long> users;

    public static void init(Context c) {
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

    public static void enrollUsers(Map<String, Long> map, Bitmap bmp, String pic, String gallery) {
        for (String s : map.keySet()) {
            try {
                myKairos.enroll(s, map.get(s) + "", gallery, "FACE", "false", "0.125", listener);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            myKairos.recognize(bmp, gallery, "FACE", "0.63", "0.125", "2", recognizeListener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
