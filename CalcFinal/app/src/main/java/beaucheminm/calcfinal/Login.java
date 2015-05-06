package beaucheminm.calcfinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


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
        DBHelper n = new DBHelper(getApplicationContext());
        SQLiteDatabase s = n.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String uname = ((EditText)findViewById(R.id.emailText)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passwordText)).getText().toString();

        if( uname.equals("") || pass.equals("") )
        {
            //((TextView)findViewById(R.id.txtInfo)).setText("Username/Password cannot be empty.");
            s.close();
            return;
        }
        cv.put(UserContract.UserEntry.COLUMN_NAME_EMAIL, uname);
        cv.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, pass);

        s.insert(UserContract.UserEntry.TABLE_NAME, "null", cv);
        s.close();

        //((TextView)findViewById(R.id.txtInfo)).setText("Login created. Please log in now.");
    }

    public void attemptLogin(View v){
        DBHelper dbh = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String uname = ((EditText)findViewById(R.id.emailText)).getText().toString();
        String pass = ((EditText)findViewById(R.id.passwordText)).getText().toString();

        String[] projection = {
                UserContract.UserEntry._ID,
                UserContract.UserEntry.COLUMN_NAME_EMAIL,
                UserContract.UserEntry.COLUMN_NAME_PASSWORD
        };

        //SELECT * FROM numbers
        Cursor s = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                "password=? AND username=?",
                new String[] {pass, uname},
                null,
                null,
                null,
                null
        );

        s.moveToFirst();
        String display = "";
        if( s.getCount() == 0 )
        {
            //((TextView)findViewById(R.id.txtInfo)).setText("Invalid login info.");
        }
        else
        {
            Intent i = new Intent(this, MainActivity.class);

            i.putExtra("uname", uname);
            startActivity(i);
        }


    }
}
