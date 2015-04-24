package beaucheminm.calcfinal;

import java.util.HashMap;

/**
 * Created by Max on 4/20/2015.
 */
public class Expression {
    private Integer expressionID;
    private String expressionString;
    private String creatorEmail;
    private HashMap<String, Variable> vars;

    public Expression(int id, String txt, String creator){
        expressionID = new Integer(id);
        expressionString = txt;
        creatorEmail = creator;
        vars = new HashMap<>();
    }

    public Integer getExpressionID(){
        return expressionID;
    }

    public void setExpressionID(Integer newID){
        expressionID = newID;
    }

    public String getExpressionString(){
        return expressionString;
    }

    //updates the expression string
    //used to edit and save the expression
    public void setExpressionString(String newString){
        expressionString = newString;
    }

    public String getCreatorEmail(){
        return creatorEmail;
    }

    //insert existing variable object
    public void addVariable(Variable v){
        vars.put(v.getVarString(), v);
    }

    //insert new variable object
    public void addVariable(String str, Double val){
        vars.put(str, new Variable(expressionID, str, val));
    }

    //returns a variable object for a given key
    public Variable getVariable(String key){
        return vars.get(key);
    }

    //returns the value of a variable for a given key
    public Double getVariableValue(String key){
        return vars.get(key).getVarValue();
    }

    //updates the Double value of a variable for a given key
    public void updateVariableValue(String key, Double newValue){
        vars.get(key).setVarValue(newValue);
    }

    //removes the variable with the given key
    public void removeVariable(String key){
        vars.remove(key);
    }

}
