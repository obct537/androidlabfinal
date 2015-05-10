package beaucheminm.calcfinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void attemptSignup(View v){
        String uname = ((EditText)findViewById(R.id.emailText)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passwordText)).getText().toString();

        new login(this, uname, pass, "signup").execute();
    }

    public void attemptLogin(View v){
        String uname = ((EditText)findViewById(R.id.emailText)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passwordText)).getText().toString();

        new login(this, uname, pass, "login").execute();
    }

    private class login extends AsyncTask<Long, Integer, JSONObject> {
        Activity a;
        String email, pass, action;

        public login(Activity a, String email, String pass, String action) {
            this.a = a;
            this.email = email;
            this.pass = pass;
            this.action = action;
        }

        protected JSONObject doInBackground(Long... v) {
            JSONObject j = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-52-24-173-231.us-west-2.compute.amazonaws.com/index.php");
                // Add your data via a POST command. insert.php accesses variable by using $_POST['variable']
                // See attached php page to see the whole example.
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("action", this.action));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("pass", pass));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String r = EntityUtils.toString(entity);
                j = new JSONObject(r);

            } catch (ClientProtocolException e) {
                Log.i("main", "bad thing happened");
            } catch (IOException e) {
                Log.i("main", "bad thing happened");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //If your method returns nothing, you can state the return type is Void.
            //However, Void is an Object, it is different from void. Therefore, you
            //must return something. This is why I return null.
            return j;
        }

        protected void onPostExecute(JSONObject j) {
            try {

                ((CustomApplication) getApplicationContext()).setUserEmail(j.getString("email"));
                Log.i("email ---- ", j.getString("email"));
                Intent i = new Intent(this.a, MainActivity.class);

                startActivity(i);
            } catch (Exception e) {
                ((TextView)a.findViewById(R.id.errorText)).setText("Invalid login");
            }
        }
    }

}
