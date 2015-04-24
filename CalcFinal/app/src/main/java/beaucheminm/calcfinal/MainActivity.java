package beaucheminm.calcfinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {
    ListView expressionList;
    ListView variableList;
    InfixParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expressionList = (ListView) findViewById(R.id.expressionListView);
        expressionList = (ListView) findViewById(R.id.variableListView);
        parser = new InfixParser();

        setContentView(R.layout.activity_main);
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

    public void solveEquation(View v){
        //pass in all variables related to this expression
            //instance.setVariable("name = value")

        //pass in expression to solve

        ((CustomApplication)this.getApplication()).setUserEmail("hello");
    }

    public void equationIsValid(String expression){

    }

    public void viewFriendsActivity(View v){
        Intent i = new Intent(this, Friends.class);
        startActivity(i);
    }
}