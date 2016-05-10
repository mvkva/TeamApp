package mickey.ogosense.com.articlesteamapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    public String titleOfArticle = "";
    public String idOfArticle = "";
    public String articleText = "";
    public String creationDate = "";
    public String image = "";
    public String imagePath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle extras = getIntent().getExtras();
        titleOfArticle = extras.getString("title");
        idOfArticle = extras.getString("id");
        new MyTask().execute();

    }

    public class MyTask extends AsyncTask<Void, Void, String> {
        String day = "";
        String month = "";
        String year = "";

        protected void onPostExecute(String result){
            TextView titleTextView = (TextView)findViewById(R.id.textViewTitle);

            TextView articleTextView = (TextView)findViewById(R.id.textViewArticle);
            try {

                JSONObject json = new JSONObject(result);

                articleText = json.getString("introtext");
                creationDate = json.getString("created");
                image = json.getString("images");
                JSONObject jsonImage = new JSONObject(image);
                imagePath = jsonImage.getString("image_intro");
                System.out.println(imagePath);

                String date = creationDate.split(" ")[0];
                year = date.split("-")[0];
                month = date.split("-")[1];
                day = date.split("-")[2];


            } catch (JSONException e) {
                e.printStackTrace();
            }
            titleTextView.setText((titleOfArticle + "\n" + day + "." + month + "." + year + "\n").toString());

            new SecondTask().execute();

            articleTextView.setText(Html.fromHtml("\n"+articleText).toString());

        }

        @Override
        protected String doInBackground(Void... g) {


            String responseData = "";
            try {
                URL url = new URL("http://android.ogosense.net/interns/ace/article.php?id="+idOfArticle);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String data = "";
                while((data = reader.readLine()) != null){

                    responseData = responseData + data;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseData;
        }
    }

    public class SecondTask extends AsyncTask<Void, Void, Bitmap> {

        protected void onPostExecute(Bitmap bitmap){

            ImageView imageView = (ImageView)findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

        }

        @Override
        protected Bitmap doInBackground(Void... g) {

            Bitmap bitmap = null;
            try {

                bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://android.ogosense.net/"+imagePath).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }


}
