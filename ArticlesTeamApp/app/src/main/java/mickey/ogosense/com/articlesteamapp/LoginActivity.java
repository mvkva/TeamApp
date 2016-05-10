package mickey.ogosense.com.articlesteamapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    public String username = "";
    public String password = "";
    public int uid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){
        EditText editUsername = (EditText)findViewById(R.id.editText);
        EditText editPassword = (EditText)findViewById(R.id.editText2);

        username = editUsername.getText().toString();
        if(username.equals("")){
            Toast.makeText(this, "You have to enter an username!", Toast.LENGTH_LONG).show();
            return;
        }
        password = editPassword.getText().toString();
        if(password.equals("")){
            Toast.makeText(this, "You have to enter a password!", Toast.LENGTH_LONG).show();
            return;
        }
        new MyTask().execute();

    }

    public class MyTask extends AsyncTask<Void, Void, String> {

        protected void onPostExecute(String result){

            try {
                System.out.println(result);
                JSONObject json = new JSONObject(result);
                uid = json.getInt("uid");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(LoginActivity.this, SelectActivity.class);
            intent.putExtra("uid", uid);
            System.out.println(uid);
            startActivity(intent);
        }
        @Override
        protected String doInBackground(Void... g) {
            String responseData = "";
            try {
                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);
                URL url = new URL("http://android.ogosense.net/interns/ace/login.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(json.toString());
                dos.flush();
                dos.close();

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

}
