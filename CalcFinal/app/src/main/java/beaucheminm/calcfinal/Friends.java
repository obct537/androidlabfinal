package beaucheminm.calcfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Friends extends ActionBarActivity {
    ArrayList<Friendship> friendshipArrayList;
    ArrayList<Expression> expressionArrayList;
    int selectedFriendshipIndex;
    int selectedExpressionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friendshipArrayList = new ArrayList<Friendship>();
        expressionArrayList = new ArrayList<Expression>();
        selectedFriendshipIndex = -1;
        selectedExpressionIndex = -1;
        loadFriends();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        HashMap exps = ((CustomApplication)this.getApplicationContext()).getAllExpressions();
        HashMap friends = ((CustomApplication)this.getApplicationContext()).getAllFriendships();
        expressionManager e = new expressionManager(exps, friends);

        e.save();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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

    public void loadFriends(){
        friendshipArrayList.clear();

        CustomApplication custom = ((CustomApplication)this.getApplicationContext());

        HashMap<String, Friendship> allFriendships = custom.getAllFriendships();
        Iterator frIt = allFriendships.entrySet().iterator();


        while(frIt.hasNext()){
            Map.Entry pair = (Map.Entry)frIt.next();
            Friendship f = (Friendship)pair.getValue();

            if(f.getStatus().equals("Existing")){
                if(f.getEmail_receive().equals(custom.getUserEmail())){
                    friendshipArrayList.add(f);
                }
                else if(f.getEmail_send().equals(custom.getUserEmail())){
                    friendshipArrayList.add(f);
                }
            }
            //if the user status is pending, only the user receiving the request can see the friendship
            else
            {
                if(f.getEmail_receive().equals(custom.getUserEmail())){
                    friendshipArrayList.add(f);
                }
            }
        }

        Object[] objArr = friendshipArrayList.toArray();

        String[] strArr = new String[objArr.length];

        for(int i = 0; i<objArr.length; i++) {
            Friendship f = ((Friendship) objArr[i]);
            if(f.getStatus().equals("Existing")){
                if(f.getEmail_receive().equals(custom.getUserEmail())){
                    strArr[i] = f.getEmail_send();
                }
                else if(f.getEmail_send().equals(custom.getUserEmail())){
                    strArr[i] = f.getEmail_receive();
                }
            }
            else
            {
                if(f.getEmail_receive().equals(custom.getUserEmail())){
                    strArr[i] = "[Friend Request] \n" + f.getEmail_send();
                }
                else if(f.getEmail_send().equals(custom.getUserEmail())){
                    strArr[i] = "[Friend Request] \n" + f.getEmail_receive();
                }
            }


        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArr);
        ListView lv = (ListView)findViewById(R.id.friendsListView);
        lv.setAdapter(adapter);
        lv.setSelection(-1);

        lv.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener()
                        {
                            public void onItemClick(AdapterView adapterView, View view,int arg2, long arg3)
                            {
                                selectedFriendshipIndex = arg2;
                                loadExpressions();
                            }
                        }
                );
    }

    public void loadExpressions(){
        expressionArrayList.clear();

        CustomApplication custom = ((CustomApplication)this.getApplicationContext());

        HashMap<Integer, Expression> allExpressions = custom.getAllExpressions();
        Iterator exprIt = allExpressions.entrySet().iterator();

        String friendEmail;
        Friendship f = friendshipArrayList.get(selectedFriendshipIndex);

        if(f.getStatus().equals("Existing")) {
            if (f.getEmail_send().equals(custom.getUserEmail())) {
                friendEmail = f.getEmail_receive();
            } else {
                friendEmail = f.getEmail_send();
            }


            while (exprIt.hasNext()) {
                Map.Entry pair = (Map.Entry) exprIt.next();
                Expression e = (Expression) pair.getValue();
                if (e.getCreatorEmail().equals(friendEmail)) {
                    expressionArrayList.add(e);
                }
            }

            Object[] objArr = expressionArrayList.toArray();

            String[] strArr = new String[objArr.length];

            for (int i = 0; i < objArr.length; i++) {
                Expression e = ((Expression) objArr[i]);
                strArr[i] = e.getExpressionString();
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strArr);
            ListView lv = (ListView) findViewById(R.id.friendsExpressionListView);
            lv.setAdapter(adapter);
            lv.setSelection(-1);


            lv.setOnItemClickListener
                    (
                            new AdapterView.OnItemClickListener()
                            {
                                public void onItemClick(AdapterView adapterView, View view,int arg2, long arg3)
                                {
                                    selectedExpressionIndex = arg2;
                                }
                            }
                    );
        }
    }

    public void returnToMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void addFriend(View v){
        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Friend")
                .setMessage("Enter the email of the person you would like to send a friend request to")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addNewFriend(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void addNewFriend(String str){
        //This is the code to send a friend request when you close the application
        //this code does NOT do a live database hit
        //str is the name of the friend that should be sent a request to
        CustomApplication custom = ((CustomApplication)this.getApplicationContext());
        custom.sendFriendRequest(str);
    }

    public void removeFriend(View v){
        removeFriend();
    }

    public void removeFriend(){
        if(selectedFriendshipIndex!=-1) {
            CustomApplication custom = ((CustomApplication)this.getApplicationContext());
            Friendship f = friendshipArrayList.get(selectedFriendshipIndex);
            custom.getAllFriendships().remove(f.getEmail_send() + " " + f.getEmail_receive());
            loadFriends();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a friend");
            dlgAlert.setTitle("Error: No Friend Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void saveExpression(View v){
        CustomApplication custom = ((CustomApplication)this.getApplicationContext());

        Expression temp = expressionArrayList.get(selectedExpressionIndex);

        HashMap<String, Variable> variables = temp.getAllVariables();
        custom.addExpression(temp.getExpressionString(), variables);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Save Successful");

        // set dialog message
        alertDialogBuilder.setMessage("Expression has been saved to your profile successfully");

        alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void acceptFriendRequest(View v){
        if(selectedFriendshipIndex!=-1) {
            CustomApplication custom = ((CustomApplication)this.getApplicationContext());
            Friendship f = friendshipArrayList.get(selectedFriendshipIndex);
            if(f.getStatus().equals("Pending")){
                custom.getAllFriendships().get(f.getEmail_send() + " " + f.getEmail_receive()).setStatus("Existing");
                loadFriends();
            }
            else
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("This user is already your friend");
                dlgAlert.setTitle("Error: No Request Selected");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a friend");
            dlgAlert.setTitle("Error: No Friend Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

    }

    public void declineFriendRequest(View v){
        if(selectedFriendshipIndex!=-1) {
            CustomApplication custom = ((CustomApplication)this.getApplicationContext());
            Friendship f = friendshipArrayList.get(selectedFriendshipIndex);
            if(f.getStatus().equals("Pending")){
                custom.getAllFriendships().remove(f.getEmail_send() + " " + f.getEmail_receive());
                loadFriends();
            }
            else
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("To remove existing friend, please click Remove Friend");
                dlgAlert.setTitle("Error: No Request Selected");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a friend");
            dlgAlert.setTitle("Error: No Friend Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

}
