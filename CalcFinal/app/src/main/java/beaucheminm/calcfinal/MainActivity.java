package beaucheminm.calcfinal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    ArrayList<Expression> myExpressions;
    ArrayList<Variable> myVariables;
    int expressionSelectedIndex;
    int variableSelectedIndex;
    String tempString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myExpressions = new ArrayList<>();
        myVariables = new ArrayList<>();
        expressionSelectedIndex = -1;
        variableSelectedIndex = -1;
        loadExpressions();

        EditText edit = (EditText) findViewById(R.id.editText);

        edit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                loadVariables();
                checkValidity();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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

    public void solveEquation(View v){
        EditText et = (EditText)findViewById(R.id.editText);
        try {
            //loop to add variables to the parser
            ((CustomApplication)this.getApplicationContext()).resetParser();
            InfixParser parser = ((CustomApplication)this.getApplicationContext()).getInfixParser();

            for(int i = 0; i < myVariables.size(); i++){
                Variable temp = myVariables.get(i);
                parser.setVariable(temp.getVarString() + "=" + temp.getVarValue().toString());
            }

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
        EditText et = (EditText)findViewById(R.id.editText);
        Button sb = (Button)findViewById(R.id.buttonSolve);
        try {

            //loop to add variables to the parser
            ((CustomApplication)this.getApplicationContext()).resetParser();
            InfixParser parser = ((CustomApplication)this.getApplicationContext()).getInfixParser();


            for(int i = 0; i < myVariables.size(); i++){
                Variable temp = myVariables.get(i);
                parser.setVariable(temp.getVarString() + "=" + temp.getVarValue().toString());
            }


            parser.handleExpression(et.getText().toString());


            parser.handleExpression(et.getText().toString());
            sb.setBackgroundColor(Color.parseColor("#FFD6D6D6"));
            sb.setClickable(true);

        }catch(Exception ex){
            sb.setBackgroundColor(Color.parseColor("#ff000000"));
            sb.setClickable(false);
            Log.i("CheckValidity Failed", "");
        }
    }

    public void viewFriendsActivity(View v){
        Intent i = new Intent(this, Friends.class);
        startActivity(i);
    }

    public void loadExpressions(){
        expressionSelectedIndex = -1;
        variableSelectedIndex = -1;

        myExpressions.clear();

        CustomApplication custom = ((CustomApplication)this.getApplicationContext());
        HashMap<Integer, Expression> allExpressions = custom.getAllExpressions();
        Iterator exprIt = allExpressions.entrySet().iterator();

        EditText e = (EditText)findViewById(R.id.editText);
        e.setText("");


        while(exprIt.hasNext()){
            Map.Entry pair = (Map.Entry)exprIt.next();
            Expression x = (Expression)pair.getValue();
            if(x.getCreatorEmail().equals(custom.getUserEmail())){
                myExpressions.add(x);
            }
        }

        Object[] objArr = myExpressions.toArray();


        String[] strArr = new String[objArr.length];

        for(int i = 0; i<objArr.length; i++) {
            Expression x = ((Expression) objArr[i]);
            strArr[i] = x.getExpressionString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strArr);
        ListView lv = (ListView) MainActivity.this.findViewById(R.id.expressionListView);
        lv.setAdapter(adapter);
        lv.setSelection(-1);


        lv.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener()
                        {
                            public void onItemClick(AdapterView adapterView, View view,int arg2, long arg3)
                            {
                                expressionSelectedIndex = arg2;
                                loadExpressionString();
                                loadVariables();
                            }
                        }
                );

    }

    public void clearVariables(){
        variableSelectedIndex = -1;
        myVariables.clear();

        String[] strArr = new String[1];

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strArr);
        ListView lv = (ListView) MainActivity.this.findViewById(R.id.variableListView);
        lv.setAdapter(adapter);
        lv.setSelection(-1);
    }

    public void loadVariables(){
        variableSelectedIndex = -1;
        myVariables.clear();

        if(expressionSelectedIndex != -1) {
            CustomApplication custom = ((CustomApplication) this.getApplicationContext());

            Expression selectedExpression = custom.getSingleExpression(myExpressions.get(expressionSelectedIndex).getExpressionID());
            HashMap<String, Variable> exprVariables = selectedExpression.getAllVariables();
            Iterator varIt = exprVariables.entrySet().iterator();


            while (varIt.hasNext()) {
                Map.Entry pair = (Map.Entry) varIt.next();
                Variable v = (Variable) pair.getValue();
                myVariables.add(v);
            }

            Object[] objArr = myVariables.toArray();


            String[] strArr = new String[objArr.length];

            for (int i = 0; i < objArr.length; i++) {
                Variable v = ((Variable) objArr[i]);
                strArr[i] = v.toString();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, strArr);
            ListView lv = (ListView) MainActivity.this.findViewById(R.id.variableListView);
            lv.setAdapter(adapter);
            lv.setSelection(-1);


            lv.setOnItemClickListener
                    (
                            new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView adapterView, View view, int arg2, long arg3) {
                                    variableSelectedIndex = arg2;
                                }
                            }
                    );

        }

    }

    public void loadExpressionString(){
        CustomApplication custom = ((CustomApplication)this.getApplicationContext());
        EditText exprEditText = (EditText)findViewById(R.id.editText);

        Expression selectedExpression = custom.getSingleExpression(myExpressions.get(expressionSelectedIndex).getExpressionID());
        exprEditText.setText(selectedExpression.getExpressionString());
    }

    public void newExpression(View v){
        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("New Expression")
                .setMessage("Enter the new expression string")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        addNewExpression(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
    }

    public void addNewExpression(String expr){
        CustomApplication custom = ((CustomApplication)this.getApplicationContext());

        custom.addExpression(expr);

        loadExpressions();
        loadVariables();
        checkValidity();
    }

    public void newVariable(View v){
        if(expressionSelectedIndex!=-1) {

            // Set an EditText view to get user input
            final EditText input = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("New Variable")
                    .setMessage("Enter the new variable (ex: x = 1 )")
                    .setView(input)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            addNewVariable(input.getText().toString());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select an expression before adding a variable");
            dlgAlert.setTitle("Error: No Expression Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void addNewVariable(String str){
        str = str.replaceAll(" ", "");

        //prompt user twice for input
        String varString = str.substring(0, str.indexOf("=")).replace(" ", "");
        Double val = Double.parseDouble(str.substring(str.indexOf("=") + 1).replace(" ", ""));

        CustomApplication custom = ((CustomApplication)this.getApplicationContext());

        custom.getSingleExpression(myExpressions.get(expressionSelectedIndex).getExpressionID()).addVariable(varString, val);
        loadVariables();
        checkValidity();
    }

    public void updateExpression(View v){
        if(expressionSelectedIndex!=-1) {
            // Set an EditText view to get user input
            final EditText input = new EditText(this);

            new AlertDialog.Builder(this)
                    .setTitle("Update Expression")
                    .setMessage("Enter the new expression string")
                    .setView(input)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            updateExpression(input.getText().toString());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select an expression before attempting to update it");
            dlgAlert.setTitle("Error: No Expression Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void updateExpression(String str){
        CustomApplication custom = ((CustomApplication)this.getApplicationContext());
        custom.getAllExpressions().get(myExpressions.get(expressionSelectedIndex).getExpressionID()).setExpressionString(str);

        loadExpressions();
        checkValidity();
    }

    public void updateVariable(View v){
        if(variableSelectedIndex!= -1) {
        // Set an EditText view to get user input
        final EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Update Variable")
                .setMessage("Enter the new variable value")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        updateVariable(input.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing.
            }
        }).show();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a variable before attempting to update it");
            dlgAlert.setTitle("Error: No Variable Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void updateVariable(String str){
        try {
            CustomApplication custom = ((CustomApplication) this.getApplicationContext());
            custom.getAllExpressions().get(myExpressions.get(expressionSelectedIndex).getExpressionID()).getVariable(myVariables.get(variableSelectedIndex).getVarString()).setVarValue(Double.parseDouble(str));

            loadVariables();
            checkValidity();
        } catch (Exception ex){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(ex.getMessage());
            dlgAlert.setTitle(ex.getCause().toString());
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void deleteExpression(View v){
        if(expressionSelectedIndex!= -1) {
            CustomApplication custom = ((CustomApplication) this.getApplicationContext());
            custom.getAllExpressions().remove(myExpressions.get(expressionSelectedIndex).getExpressionID());
            loadExpressions();
            clearVariables();
            checkValidity();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select an expression before attempting to delete it");
            dlgAlert.setTitle("Error: No Expression Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    public void deleteVariable(View v){
        if(variableSelectedIndex!= -1) {
            CustomApplication custom = ((CustomApplication) this.getApplicationContext());
            custom.getAllExpressions().get(myExpressions.get(expressionSelectedIndex).getExpressionID()).removeVariable(myVariables.get(variableSelectedIndex).getVarString());
            loadVariables();
            checkValidity();
        } else {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Please select a variable before attempting to delete it");
            dlgAlert.setTitle("Error: No Variable Selected");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }
}