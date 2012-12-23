/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goalstackplanner;

import java.util.ArrayList;

/**
 *
 * @author aclapes
 */
public class Problem {
    
    ArrayList<Constant> constants;
    State initialState;
    State finalState;
    
    public Problem(ArrayList<Constant> constants, State initialState, State finalState)
    {
        this.constants = constants;
        
        this.initialState = initialState;
        this.finalState = finalState;
    }
    
    public ArrayList<? extends Constant> getConstants()
    {
        return this.constants;
    }
    
    public State getInitialState()
    {
        return this.initialState;
    }
    
    public State getFinalState()
    {
        return this.finalState;
    }
}
