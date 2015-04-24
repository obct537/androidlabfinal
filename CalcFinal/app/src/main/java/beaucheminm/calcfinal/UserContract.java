package beaucheminm.calcfinal;

import android.provider.BaseColumns;

/**
 * Created by schwas45 on 4/3/2015.
 */
public final class UserContract {
    //prevents someone from instantiating this class
    public UserContract(){}

    /* By implementing the BaseColumns interface, your inner class can inherit a primary key field
    called _ID that some Android classes such as cursor adaptors will expect it to have. It's not
    required, but this can help your database work harmoniously with the Android framework.
     This defines contents of 1 table. Makes it easy to change tables/columns
     without having to find where it is everywhere in your code.
     */
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";

    }
}
