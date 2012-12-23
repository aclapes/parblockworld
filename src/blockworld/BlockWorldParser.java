/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Constant;
import goalstackplanner.Predicate;
import goalstackplanner.Problem;
import goalstackplanner.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aclapes
 */
public class BlockWorldParser {
    
    // Attributes
    
    private String path;
    private BufferedReader in;
    
    private Problem problem;
       
    
    // Constructor
    
    public BlockWorldParser(String path) throws FileNotFoundException
    {
        this.path = path;
        
        in = new BufferedReader(new FileReader(path));
    }
    
    // Methods
    
    public Problem parse() throws IOException
    {
        
        ArrayList<Constant> constants = new ArrayList<Constant>();
        ArrayList<Predicate> initialStatePredicates = new ArrayList<Predicate>();
        ArrayList<Predicate> goalStatePredicates = new ArrayList<Predicate>();
        // Read the two lines of the file
        
        String strBlocks = null;
        String strInitialStateDefinition = null;
        String strGoalStateDefinition = null;
        try {
            strBlocks = in.readLine();
            strInitialStateDefinition = in.readLine();
            strGoalStateDefinition = in.readLine();
            
            System.out.println(strBlocks);
            System.out.println(strInitialStateDefinition);
            System.out.println(strGoalStateDefinition);
            
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*
         * Parse the read lines: blocks line and predicates line.s
         */
        
        // Blocks
        
        Pattern pattern = Pattern.compile("\\w+");
        
        Matcher matcher = pattern.matcher(strBlocks);
        while (matcher.find()) {
            if ( (matcher.group()).compareTo("Blocks") != 0 )
            {
                String blockName = matcher.group();
                constants.add(new Block(blockName));
            }
        }
        
        // Predicates
        
        pattern = Pattern.compile("(\\w+( )*-( )*\\w+)|(\\w+)");
        
        matcher = pattern.matcher(strInitialStateDefinition);
        matcher.find();
        if ( (matcher.group()).compareTo("Initial_state") == 0 )
        {
            System.out.println("Started parsing Initial_state");
            while (matcher.find()) 
            {
                String predicateName = matcher.group();
                Predicate predicate = createPredicate(predicateName, matcher);
                //if (! (predicate instanceof Heavier)) // TODO
                    initialStatePredicates.add(predicate);
            }
        }
        
        pattern = Pattern.compile("(\\w+( )*-( )*\\w+)|(\\w+)");
        
        matcher = pattern.matcher(strGoalStateDefinition);
        matcher.find();
        if ( (matcher.group()).compareTo("Goal_state") == 0 )
        {
            System.out.println("Started parsing Goal_state");
            while (matcher.find())
            {
                String predicateName = matcher.group();
                Predicate predicate = createPredicate(predicateName, matcher);
                //if (! (predicate instanceof Heavier)) // TODO
                    goalStatePredicates.add(predicate);
            }
        }
        
        this.problem = new Problem(constants,
                                    new State(initialStatePredicates), 
                                    new State(goalStatePredicates));
        
        return problem;
    }
    
    public Predicate createPredicate(String predicateName, Matcher matcher)
    {
        if ( predicateName.compareTo("ON") == 0  )
        {
            matcher.find();
            Block x = new Block(matcher.group());
            matcher.find();
            Block y = new Block(matcher.group());
            return new On(x,y);
        }    
        else if ( predicateName.compareTo("FREE") == 0 )
        {
            matcher.find();
            Block x = new Block(matcher.group());
            return new Free(x);
        } 
        else if ( predicateName.compareTo("HEAVIER") == 0 )
        {
            matcher.find();
            Block x = new Block(matcher.group());
            matcher.find();
            Block y = new Block(matcher.group());
            return new Heavier(x, y);
        }         
        else if ((Pattern.compile("ON( )*-( )*TABLE")).matcher(predicateName).find())
        {
            matcher.find();
            Block x = new Block(matcher.group());
            return new OnTable(x);
        }
        else if ( (Pattern.compile("FREE( )*-( )*ARM")).matcher(predicateName).find())
        {
            return new FreeArm();
        }
        else if ((Pattern.compile("PICKED( )*-( )*UP")).matcher(predicateName).find())
        {
            matcher.find();
            Block x = new Block(matcher.group());
            return new PickedUp(x);
        }
        else if ((Pattern.compile("USED( )*-( )*COLS( )*-( )*NUM")).matcher(predicateName).find())
        {
            matcher.find();
            String str = matcher.group();
            return new UsedColsNum(Integer.parseInt(str));
        }   
        
        return null;
    }
    
    public Problem getProblem()
    {
        return this.problem;
    }
}
