package beaucheminm.calcfinal;

import android.util.Log;

import java.util.*;
import java.util.regex.*;
/**
 *
 * @author obct537
 *
 * This class is recycled from my Data Structures class....
 *
 * Hence some of the methods being useless in this context.
 */
public class InfixParser {

    public InfixParser() {
        varz = new HashMap();
        operators = new HashMap();
        operators.put("+", 1);
        operators.put("-", 1);
        operators.put("*", 2);
        operators.put("/", 2);
        operators.put("%", 2);
        operators.put("~", 2);

        steps = new ArrayList<String>();
    }

    //The following two are final because NetBeans wouldn't stop whining
    //until they were.
    private final HashMap varz;

    //This regex matches something similar to: a = 3.6
    private static final Pattern varRegex =
            Pattern.compile("\\s*([A-Za-z]+)\\s*=\\s*([0-9]*\\.?[0-9]*)");
    private static String newline = System.getProperty("line.separator");
    private static HashMap<String, Integer> operators;

    private ArrayList<String> inTokens;
    private ArrayList<String> steps;

    public class ExpressionFormatException extends Exception {
        public ExpressionFormatException() {
            super();
        }
    }

    /**
     * Method to prompt for user input, and handle it accordingly.
     * This method will run indefinitely until the user types in "quit" or the
     * process is terminated.
     *
     */
    public void handleInput() throws Exception {

        Scanner in = new Scanner(System.in);
        in.useDelimiter(newline);
        System.out.println("To use:");
        System.out.println("Enter in a syntactically correct expression.");
        System.out.println("To assign a variable, simply type [name]=[value]");
        System.out.println("This will return 1.0 for success, or 0.0 for a failure");
        System.out.println("");
        System.out.println("You can then use the variable in a follow expression");
        System.out.println("Ex:");
        System.out.println("");
        System.out.println("A=3");
        System.out.println("1.0");
        System.out.println("A+3");
        System.out.println("6.0");
        System.out.println("");
        System.out.println("To exit, type quit");
        System.out.println("");
        System.out.println("Enter an expression:");

        String exp = in.next();


        while( !exp.equals("quit") ) {

            try {
                double out = parseInput(exp);
                System.out.println(out);
            } catch (ExpressionFormatException e) {
                exp = "";
                continue; //errors are printed down below.
            }
            exp = in.next();
        }

    }

    /**
     * This method decides how to treat the user input.
     * It will choose to treat it as an expression, assignment, or junk.
     *
     * @param exp           input from user
     * @return              outcome of expression
     */
    public double parseInput(String exp) throws ExpressionFormatException, Exception {

        int cat = getCategory(exp);

        if( cat == 1 ) {
            boolean itWorked = setVariable(exp);
            if( itWorked ) {
                return 1.0;
            }else{
                return 0.0;
            }
        }else if( cat == 2 ) {
            return handleExpression(exp);
        } else {
            System.out.println("Invalid input, please try again.");
            throw new ExpressionFormatException();
        }
    }

    /**
     * method that converts an expression and computes the total
     * @param exp           expression to be evaluated
     * @return              N/A
     */
    public double handleExpression(String exp) throws Exception {

        ArrayList<String> tokens = tokenizeExpression(exp);

        try {
            tokens = convertToPostifx(tokens);
            if( tokens.size() == 1 ) {
                return Double.parseDouble(tokens.get(0));
            }else{
                double total = computeTotal(tokens);
                return total;
            }
        }catch( ExpressionFormatException e) {
            System.out.println(e);
        }catch( EmptyStackException e) {
            throw new Exception();
        }
        return 0.0;
    }

    /**
     * Method that computes the total of a postfix expression
     * @param tokens            pieces of the postfix expression
     * @return                  value of the expression
     */
    public double computeTotal(ArrayList<String> tokens) {

        Stack temp = new Stack();

        for( String s : tokens ) {
            if( matchNumbers(s) ) {
                temp.push(s);
            }else{
                String first =temp.pop().toString();
                String second = temp.pop().toString();

                double one = Double.parseDouble(first);
                double two = Double.parseDouble(second);

                double out = handleOperation(one, two, s);
                temp.push(out);
            }
        }
        return (double)temp.pop();
    }

    private String tokensToString(ArrayList tokens)
    {
        Iterator i = tokens.iterator();
        String out = "";
        while( i.hasNext() )
        {
            out += " " + i.next();
        }
        return out;
    }

    /**
     * Method that applies a simple two part arithmetic operation.
     * It is assumed that both elements are valid by this point.
     *
     * @param first                 first operand
     * @param second                second operand
     * @param op                    operator
     * @return                      total
     */
    private double handleOperation(double first, double second, String op) {

        boolean update = false;
        int index = 1;
        for( int i = 1; i < inTokens.size() - 1; i++ )
        {
            boolean one = false, two = false;
            if( inTokens.get(i+1).equals(Double.toString(first)) || inTokens.get(i+1).equals(Integer.toString((int)first)) )
            {
                one = true;
            }

            if( inTokens.get(i-1).equals(Double.toString(second)) || inTokens.get(i-1).equals(Integer.toString((int)second)) )
            {
                two = true;
            }

            if( one && two && inTokens.get(i).equals(op))
            {
                update = true;
                index = i;
                break;
            }
        }

        double out;
        if( op.equals("+") ) {
            out= first + second;
        }else if( op.equals("-") ) {
            out = second - first;
        }else if( op.equals("/") ) {
            out = second / first;
        }else if( op.equals("~") ) {
            int a = (int)second/(int)first;
            out = (double)a;
        }else if( op.equals("%")) {
            out = second%first;
        }else{
            out = first*second;
        }

        if( update )
        {
            inTokens.set(index, Double.toString(out));
            inTokens.remove(index-1);
            inTokens.remove(index);

            if( index >= 2 && inTokens.get( index - 2 ).equals("(") && inTokens.get(index).equals(")"))
            {
                inTokens.remove(index - 2);
                inTokens.remove(index - 1);
            }

            Iterator i = inTokens.iterator();
            String exp = "";
            while( i.hasNext() )
            {
                exp += i.next();
            }

            steps.add(exp);
        }

        return out;
    }

    /**
     * Checks if the first operator is of higher precedence than the second
     *
     * @param first                 first operator
     * @param second                second operator
     * @return                      whether or not the first is of higher precendence than the second
     */
    private boolean checkOpOrder(String first, String second) {
        if( first.equals("(") || second.equals("(")  )
            return false;

        return operators.get(first) >= operators.get(second);
    }

    /**
     * Method to convert from infix notation to postfix notation.
     * If the expression is invalid, an exception is thrown.
     *
     * @param tokens                    the expression, in a tokenized list
     * @return                               an array with the tokens correctly re-ordered
     * @throws InfixParser.ExpressionFormatException
     */
    public ArrayList<String> convertToPostifx(ArrayList<String> tokens) throws ExpressionFormatException {
        ArrayList<String> out = new ArrayList();
        Stack<String> temp = new Stack();

        inTokens = tokens;

        //This next huge loop implements the infix-to-postfix conversion algorithm
        for( String key: tokens ) {
            if( matchParens(key) ) {
                if( key.equals("(")  ) {
                    temp.push(key);
                }else{
                    String op = temp.peek();
                    while( !op.equals("(") ) {
                        temp.pop();
                        out.add(op);

                        if( temp.empty() ) {
                            System.out.println("missing parentheses.");
                            throw new ExpressionFormatException();
                        }
                        op = temp.peek();
                    }
                    //removes the final ( character
                    temp.pop();
                }
            }else if(matchOps(key) ) {

                if( temp.empty() ) {
                    temp.push(key);
                }else{
                    String top = temp.peek();
                    while( !temp.empty() ) {
                        if( checkOpOrder(top, key) ) {
                            String pop = temp.pop();
                            out.add(pop);
                            if( temp.empty() ) {
                                temp.push(key);
                                break;
                            }
                            top = temp.peek();
                        }else{
                            temp.push(key);
                            break;
                        }
                    }
                }
            }else if(matchNumbers(key) ) {
                String num = getVariable(key);
                out.add(num);
            }
        }

        //If there's anything left on the stack, add it
        while( !temp.empty() ) {
            String s = temp.pop();
            out.add(s);
        }

        return out;
    }

    /**
     * Method that replaces variable names with values.
     * If the variable doesn't exist, an exception is thrown.
     * @param name              variable name
     * @return                  value of variable
     */
    private String getVariable(String name) throws ExpressionFormatException {

        if( varz.containsKey(name) ) {
            return varz.get(name).toString();
        }
        if( name.contains("[A-Za-Z]") ) {
            System.out.println("Symbol not found: " + name);
            throw new ExpressionFormatException();
        }
        return name;
    }

    /**
     * Determines if string is a proper number.
     *
     * @param exp           string to be evaluated
     * @return                  if the string is a proper operand.
     */
    private boolean matchNumbers(String exp) {
        String numbers = buildExpRegex(false);

        return exp.matches(numbers);
    }

    /**
     * checks if string is a paren
     * @param exp           string to be evaluated
     * @return                  if the string is a paren
     */
    private boolean matchParens(String exp) {
        String parens = "[(|)]";

        return exp.matches(parens);
    }

    /**
     * checks if string is an operator
     * @param exp           string to be evaluated
     * @return                  if the string is an operator
     */
    private boolean matchOps(String exp) {
        //for whatever reason, the - character needs to be the last one, or this wont detect it
        String operators = "[+|/|*|%|~|-]";

        return exp.matches(operators);
    }

    /**
     * Breaks the expression into chunks, based upon the rules of infix-to-postfix conversion
     *
     * @param exp                   string to be tokenized
     * @return                          Stack of tokens
     */
    public ArrayList<String> tokenizeExpression(String exp) {

        int index = 0;
        int max = exp.length();
        ArrayList<String> tokens = new ArrayList();

        String temp = "";

        for( int i = 0; i < max; i++ ) {
            String current = Character.toString(exp.charAt(i) );

            if( current.equals(" ") )
                continue;

            if( matchOps(current) ) {

                if( !temp.equals("") )
                    tokens.add(temp);

                temp = "";

                tokens.add(current);
            }else if( matchParens(current) ) {

                if( !temp.equals("") )
                    tokens.add(temp);

                temp = "";

                tokens.add(current);
            }else if( matchNumbers(current) ) {

                temp += current;
            }else{
                //Something went very wrong.
                //This should have been caught already.
                return null;
            }
        }
        if( !temp.equals("")  ) {
            tokens.add(temp);
        }

        return tokens;
    }

    /**
     * sets the variable passed by the user
     *
     * @param exp               User assignment string
     * @return boolean         whether or not the variable was set properly
     */
    public boolean setVariable(String exp) {

        try {
            Matcher matcher = getVariableMatcher(exp);
            boolean arg = matcher.matches();
            String name = matcher.group(1);
            double val = Double.parseDouble(matcher.group(2));

            varz.put(name,val);
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    /**
     * Gets a pattern matcher for variable assignments
     *
     * @param exp           input from the user
     * @return Matcher      matcher to test variable assignment
     */
    private Matcher getVariableMatcher(String exp) {
        Matcher varMatcher = varRegex.matcher(exp);
        return varMatcher;
    }

    /**
     * Evaluates whether or not the user input is a variable assignment
     *
     * @param exp           User-inputted string
     * @return boolean      true if user input is a valid variable assignment;
     */
    private boolean checkIfVariable(String exp) {
        Matcher varMatcher = getVariableMatcher(exp);

        return varMatcher.matches();
    }

    /**
     * Builds a regex string that includes all declared variables as a possible value
     * The user can opt whether or not to include operators in the regex.
     *
     * @param  includeOps        whether or not to check for operators.
     * @return                          regex string;
     */
    private String buildExpRegex(boolean includeOps) {
        StringBuilder s = new StringBuilder();
        Set<String> keys = varz.keySet();

        //We need to check for previously set variables,
        //as well as numeric values.
        Iterator i = keys.iterator();
        while( i.hasNext() ) {
            s.append(i.next());
            s.append("|");
        }

        String variableNames = s.toString();

        //This allows the method to be a bit more flexible.
        //Admittedly, it's not the most elegant approach, but....eh
        String ops = "";
        if( includeOps ) {
            ops = "+/*()-";
        }
        //Since I'm using the infix-to-postfix algorith to parse
        //the expression, this regex doesn't need to be very
        //sophisticated. It just needs to check if the
        //input is an expression of some kind.
        String expression = "[" + variableNames + "0-9\\s\\." + ops + "]+";

        return expression;
    }

    /**
     * Method to create a pattern matcher specifically to check for valid expressions
     *
     * @param exp           String entered by the user
     * @return Matcher      Regex pattern matcher
     */
    private Matcher getExpressionRegex(String exp) {
        String expression = buildExpRegex(true);        //see method dec. for info on the "true" option

        Pattern expRegex = Pattern.compile(expression);

        Matcher out = expRegex.matcher(exp);
        return out;
    }

    /**
     * Method to evaluate whether or not the entered expression
     * is actually a valid expression
     *
     * @param exp           string entered by the user
     * @return boolean      True if the passed string is a valid expression
     */
    private boolean checkIfExpression(String exp) {
        Matcher expMatcher = getExpressionRegex(exp);

        return expMatcher.matches();
    }

    /**
     * This method returns an integer value, representing
     * what type of expression the user has inputted.
     *
     * It also catches any non-valid input and returns accordingly
     *
     * 1 = variable assignment
     * 2 = normal expression
     * 0 = invalid
     *
     * @param exp       the expression entered by the user
     * @return int      An integer representing the type of expression (see above)
     */
    private int getCategory(String exp) {

        if( checkIfVariable(exp) ) {
            return 1;
        }
        else if( checkIfExpression(exp) ) {
            return 2;
        }

        //If it didn't match either,
        //it wasn't valid input
        return 0;

    }

    public ArrayList<String> getTokens() {
        return steps;
    }
}
