package hackillinois.com.thoughts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.kairos.Kairos;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static RequestQueue queue;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    static Kairos myKairos;
    Map<Long, String> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // instantiate a new kairos instance

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        KairosUtils.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make a volley request here
                // Instantiate the RequestQueue.
                dispatchTakePictureIntent();

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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("MainActivity", "GotHere");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        //Log.d("MainActivity", "GotHere");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("MainActivity", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data ) {
        Log.d("MainActivity", mCurrentPhotoPath);

        Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
        int w = image.getWidth();
        int h = image.getHeight();
        Matrix mtx = new Matrix();
        mtx.preRotate(90);
        Bitmap rotatedBmp = Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        //ImageView imgview = (ImageView) findViewById(R.id.imageView);
        //imgview.setImageBitmap(rotatedBmp);
        Log.d("Image size", image.getWidth() + "");
        Map<String, Long> map = new HashMap<>();
        map.put("http://jdevanathan3.github.io/subway.jpeg", 9292L);
        KairosUtils.enrollUsers(map, rotatedBmp, "subwaypic", "gallerytest1");

    }

    /*public void makeRequest(String url, String imageURL, String imageName, String galleryName) {
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
                /*params.put("minHeadScale", ".125");
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
    }*/

    /*KairosListener listener = new KairosListener() {

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
    };*/
}
