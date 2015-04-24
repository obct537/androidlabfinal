package beaucheminm.calcfinal;

/**
 * Created by Max on 4/20/2015.
 */
public class Variable {
    private Integer parentExpressionID;
    private String varString;
    private Double varValue;

    public Variable(Integer exprID, String str, Double val){
        parentExpressionID = exprID;
        varString = str;
        varValue = val;
    }

    public Integer getParentExpressionID(){
        return parentExpressionID;
    }

    public String getVarString(){
        return varString;
    }

    public void setVarString(String str){
        varString = str;
    }

    public Double getVarValue(){
        return varValue;
    }

    public void setVarValue(Double newVal){
        varValue = newVal;
    }

}