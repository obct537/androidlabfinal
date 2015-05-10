package beaucheminm.calcfinal;

import android.app.Activity;
import android.app.Application;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 4/24/2015.
 */

public class CustomApplication extends Application {
    private String userEmail;
    private HashMap<Integer, Expression> expressions;
    private HashMap<String, Friendship> friendships;
    InfixParser parser;

    @Override
    public void onCreate(){
       userEmail = "";
       expressions = new HashMap<Integer, Expression>();
       friendships = new HashMap<String, Friendship>();
       parser = new InfixParser();
       //loadInformation();
       super.onCreate();
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String e){
        userEmail = e;
    }

    public HashMap<Integer, Expression> getAllExpressions(){
        return expressions;
    }

    public Expression getSingleExpression(Integer exprID){
        return expressions.get(exprID);
    }

    public HashMap<String, Friendship> getAllFriendships(){
        return friendships;
    }

    public InfixParser getInfixParser(){
        return parser;
    }

    public void resetParser(){
        parser = new InfixParser();
    }

    public void addExpression(String str){
        //first find what expression ID can be used for a new expression
        int newID = 0;
        for(int i = 0; newID == 0; i++){
            if(!expressions.containsKey(i)){
                newID = i;
            }
        }

        expressions.put(newID, new Expression(newID, str, userEmail));
    }

    public void addExpression(String str, HashMap<String, Variable> variables){
        //first find what expression ID can be used for a new expression
        int newID = 0;
        for(int i = 0; newID == 0; i++){
            if(!expressions.containsKey(i)){
                newID = i;
            }
        }

        Expression e = new Expression(newID, str, userEmail);

        Iterator varIt = variables.entrySet().iterator();
        while(varIt.hasNext()){
            Map.Entry pair = (Map.Entry)varIt.next();
            Variable a = (Variable)pair.getValue();
            e.addVariable(a.getVarString(), a.getVarValue());
        }

        expressions.put(newID, e);
    }

    public void sendFriendRequest(String str){
        //first find what friendship ID can be used for a new expression
        int newID = 0;
        for(int i = 0; newID == 0; i++){
            if(!friendships.containsKey(i)){
                newID = i;
            }
        }

        friendships.put((userEmail + " " + str), new Friendship(newID, userEmail, str, "Pending"));
    }

    public void addVariable(Integer expressionID, String varString, Double value){
        expressions.get(expressionID).addVariable(varString, value);
    }

    public void loadInformation(MainActivity o){
        //Here is where you have all the information loading from the database

        getData g =  new getData(o);
        g.execute();
    }

    private class getData extends AsyncTask<Long, Integer, JSONArray> {

        MainActivity a;
        public getData(MainActivity a)
        {
            this.a = a;
        }

        protected JSONArray doInBackground(Long... v) {
            JSONArray j = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://ec2-52-24-173-231.us-west-2.compute.amazonaws.com/index.php");
                // Add your data via a POST command. insert.php accesses variable by using $_POST['variable']
                // See attached php page to see the whole example.
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("action", "getInitial"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                String r = EntityUtils.toString(entity);

                if( r.equals("") )
                {
                    ((TextView)a.findViewById(R.id.errorText)).setText("Invalid Login");
                    j = new JSONArray("");
                }
                else {
                    j = new JSONArray(r);
                }

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

        // This is called when doInBackground() is finished
        protected void onPostExecute(JSONArray j) {
            String t = j.toString();

            if( t.equals("") )
            {
                return;
            }
            try {
                JSONArray vars = j.getJSONArray(0);
                JSONArray exps = j.getJSONArray(1);
                JSONArray f = j.getJSONArray(2);

                for(int i = 0; i < exps.length(); i++ )
                {
                    JSONObject o = exps.getJSONObject(i);
                    Expression e = new Expression(o.getInt("id"), o.getString("expression"), o.getString("email"));

                    expressions.put(o.getInt("id"), e);
                }

                for(int i = 0; i < vars.length(); i++ )
                {
                    JSONObject o = vars.getJSONObject(i);
                    Variable v = new Variable(o.getInt("expressionID"), o.getString("name"), o.getDouble("value"));

                    expressions.get(o.getInt("expressionID")).addVariable(v);
                }

                for(int i = 0; i < f.length(); i++ )
                {
                    JSONObject o = f.getJSONObject(i);
                    Friendship e = new Friendship(o.getInt("id"), o.getString("email_send"), o.getString("email_receive"), o.getString("status"));

                    friendships.put(o.getString("id"), e);
                }

                a.loadExpressions();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
