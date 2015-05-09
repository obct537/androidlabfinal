package beaucheminm.calcfinal;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by obct537 on 5/8/15.
 */
public class expressionManager {

    HashMap<String, Expression> exps;
    String expressionJSON;
    String variableJSON;

    public expressionManager(HashMap<String, Expression> exps)
    {
        this.exps = exps;
    }

    public void setOut()
    {
        Iterator iterator = exps.keySet().iterator();

        JSONArray expressions = new JSONArray();
        JSONArray variables = new JSONArray();

        int count = 0;

        while( iterator.hasNext() )
        {
            count++;
            JSONObject obj = new JSONObject();

            Expression e = exps.get(iterator.next());

            try {
                obj.put("expression", e.getExpressionString());
                obj.put("email", e.getCreatorEmail());

                expressions.put(obj);

                HashMap<String, Variable> varMap = e.getAllVariables();

                Iterator i = varMap.keySet().iterator();

                while( i.hasNext() )
                {
                    JSONObject var = new JSONObject();
                    Variable v = varMap.get(i.next());

                    var.put("expressionID", count);
                    var.put("name", v.getVarString());
                    var.put("value", v.getVarValue());

                    variables.put(var);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }


        expressionJSON = expressions.toString();
        variableJSON = variables.toString();
    }

    public void save()
    {
        setOut();

        new setData().execute();
    }

    private class setData extends AsyncTask<Void, Integer, Void> {

        protected Void doInBackground(Void... v) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-52-24-173-231.us-west-2.compute.amazonaws.com/index.php");
                // Add your data via a POST command. insert.php accesses variable by using $_POST['variable']
                // See attached php page to see the whole example.
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "setNewData"));
                nameValuePairs.add(new BasicNameValuePair("exps", expressionJSON));
                nameValuePairs.add(new BasicNameValuePair("vars", variableJSON));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                Log.i("main", "bad thing happened");
            } catch (IOException e) {
                Log.i("main", "bad thing happened");
            }
            //If your method returns nothing, you can state the return type is Void.
            //However, Void is an Object, it is different from void. Therefore, you
            //must return something. This is why I return null.

            return null;
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(Void j) {

        }
    }
}
