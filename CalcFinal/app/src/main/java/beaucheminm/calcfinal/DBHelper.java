package beaucheminm.calcfinal;

/**
 * Created by schwas45 on 4/3/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NumberReader.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //All you need to do is call getWritableDatabase() or getReadableDatabase().
    //Note: Because they can be long-running, be sure that you call getWritableDatabase() or getReadableDatabase() in a background thread, such as with AsyncTask or IntentService.
    //To use SQLiteOpenHelper, create a subclass that overrides the onCreate(), onUpgrade() and onOpen() callback methods. You may also want to implement onDowngrade(), but it's not required.
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " (" +
                UserContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserContract.UserEntry.COLUMN_NAME_EMAIL + " TEXT," +
                UserContract.UserEntry.COLUMN_NAME_PASSWORD + " TEXT" + " )");

        db.execSQL("CREATE TABLE " + UserContract.ExpressionEntry.TABLE_NAME + " (" +
                UserContract.ExpressionEntry._ID + " INTEGER PRIMARY KEY," +
                UserContract.ExpressionEntry.COLUMN_NAME_EMAIL + " TEXT," +
                UserContract.ExpressionEntry.COLUMN_NAME_EXPRESSIONSTRING + " TEXT" + " )");

        db.execSQL("CREATE TABLE " + UserContract.VariableEntry.TABLE_NAME + " (" +
                UserContract.VariableEntry._ID + " INTEGER PRIMARY KEY," +
                UserContract.VariableEntry.COLUMN_NAME_EXPRESSIONID + " TEXT," +
                UserContract.VariableEntry.COLUMN_NAME_VARSTRING + " TEXT," +
                UserContract.VariableEntry.COLUMN_NAME_VARVALUE + " DOUBLE" + " )");

        db.execSQL("CREATE TABLE " + UserContract.FriendshipEntry.TABLE_NAME + " (" +
                UserContract.FriendshipEntry._ID + " INTEGER PRIMARY KEY," +
                UserContract.FriendshipEntry.COLUMN_NAME_EMAILSEND + " TEXT," +
                UserContract.FriendshipEntry.COLUMN_NAME_EMAILRECEIVE + " TEXT," +
                UserContract.FriendshipEntry.COLUMN_NAME_STATUS + " TEXT" + " )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.ExpressionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.VariableEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.FriendshipEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
