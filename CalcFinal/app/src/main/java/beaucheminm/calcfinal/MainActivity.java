package beaucheminm.calcfinal;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        EditText et = (EditText)findViewById(R.id.editText);
        try {
            //loop to add variables to the parser


            Double result = parser.handleExpression(et.getText().toString());
            et.setText(result.toString());
        }catch(Exception ex){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(ex.getMessage());
            dlgAlert.setTitle("Variable Replacement Error");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void checkValidity(){
        Button sb = (Button)findViewById(R.id.buttonSolve);
        EditText et = (EditText)findViewById(R.id.editText);
        try {
            parser.handleExpression(et.getText().toString());
            sb.setBackgroundColor(Color.parseColor("#FFD6D6D6"));
            sb.setClickable(true);

        } catch(Exception ex){
            sb.setBackgroundColor(Color.parseColor("#ff000000"));
            sb.setClickable(false);
        }
    }

    public void viewFriendsActivity(View v){
        Intent i = new Intent(this, Friends.class);
        startActivity(i);
    }
}