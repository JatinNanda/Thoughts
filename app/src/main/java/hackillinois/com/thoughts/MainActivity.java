package hackillinois.com.thoughts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.kairos.Kairos;
import com.kairos.KairosListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // instantiate a new kairos instance
        final Kairos myKairos = new Kairos();
        // set authentication
        String app_id = "6354cb00";
        String api_key = "7323737bd141f7710b54fd3e8c71dbaf";
        myKairos.setAuthentication(this, app_id, api_key);

        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make a volley request here
                // Instantiate the RequestQueue.
                try {
                    myKairos.enroll("http://jdevanathan3.github.io/Home_Picture.jpg",
                            "jaysbeautifulface3", "gallerytest1", "FACE", "false", "0.125", listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //String url, String imageURL, String imageName, String galleryName
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeRequest(String url, String imageURL, String imageName, String galleryName) {
        // Request a string response from the provided URL.
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("image", imageURL);
            jsonBody.put("subject_id", imageName);
            jsonBody.put("gallery_name", galleryName);
            jsonBody.put("selector", "SETPOSE");
            jsonBody.put("symmetricFill", "true");
        } catch (Exception e) {
            Log.d("@MainActivity", "HELLA EXCEPTION");
        }
        JsonObjectRequest request = new JsonObjectRequest(
                url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("@MainActivity", "IT WORKS!!!!!");
                        Log.d("MainActivity", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("@MainActivity", "Doesn't work.");
                        Log.d("Volley Error", error.getStackTrace().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // something to do here ??
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("url", "http://jdevanathan3.github.io/Home_Picture.jpg");
                params.put("subject_id", "JayD");
                params.put("gallery_name", "MyGallery");*/
                params.put("minHeadScale", ".125");
                params.put("selector", "FACE");
                params.put("multiple_faces", "false");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // something to do here ??
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("app_id", "6354cb00");
                params.put("app_key", "7323737bd141f7710b54fd3e8c71dbaf");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(request);
    }

    KairosListener listener = new KairosListener() {

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
