package hackillinois.com.thoughts;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Jay on 2/20/16.
 */
public class KairosUtils {
    private static Kairos myKairos;
    private static KairosListener listener;

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
