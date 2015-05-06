package beaucheminm.calcfinal;

import android.app.Application;

import java.util.HashMap;
import java.util.Iterator;
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
       loadInformation();
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

    public void addVariable(Integer expressionID, String varString, Double value){
        expressions.get(expressionID).addVariable(varString, value);
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
        x.addVariable(new Variable(x.getExpressionID(), "y", 13.3));
        expressions.put(x.getExpressionID(), x);

        x = new Expression(3, "23 + 4 - z", "another@person.com");
        x.addVariable(new Variable(x.getExpressionID(), "z", 1.1));
        expressions.put(x.getExpressionID(), x);


        Friendship f = new Friendship(1, "person@place.com", "another@person.com", "Existing");
        friendships.put(f.getEmail_send() + " " + f.getEmail_receive(), f);


    }
}
