package beaucheminm.calcfinal;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by Max on 4/24/2015.
 */

public class CustomApplication extends Application {
    private String userEmail;
    private HashMap<Integer, Expression> expressions;
    private HashMap<Integer, Friendship> friendships;

    @Override
    public void onCreate(){
       userEmail = "";
        expressions = new HashMap<Integer, Expression>();
        friendships = new HashMap<Integer, Friendship>();
        loadInformation();
       super.onCreate();
    }


    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String e){
        userEmail = e;
    }

    public void loadInformation(){
        //Here is where you have all the information loading from the database


        //test data
        userEmail = "person@place.com";

        Expression x = new Expression(1, "3+4*x", "person@place.com");
        x.addVariable(new Variable(x.getExpressionID(), "x", 2.0));
        expressions.put(x.getExpressionID(), x);

        x = new Expression(2, "x*y", "person@place.com");
        x.addVariable(new Variable(x.getExpressionID(), "x", 1.9));
        x.addVariable(new Variable(x.getExpressionID(), "y", 3.3));
        expressions.put(x.getExpressionID(), x);

        x = new Expression(3, "23 + 4 - var", "another@person.com");
        x.addVariable(new Variable(x.getExpressionID(), "var", 12.3));
        expressions.put(x.getExpressionID(), x);


        Friendship f = new Friendship(1, "person@place.com", "another@person.com", "Existing");
        friendships.put(f.getID(), f);


    }
}
