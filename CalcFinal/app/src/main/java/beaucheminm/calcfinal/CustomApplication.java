package beaucheminm.calcfinal;

import android.app.Application;

/**
 * Created by Max on 4/24/2015.
 */
public class CustomApplication extends Application {
    private String userEmail;

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String e){
        userEmail = e;
    }
}
