/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Problem;
import goalstackplanner.State;
import java.util.ArrayList;

/**
 *
 * @author aclapes
 */
public class BlockWorld 
{
    private ArrayList<Block> blocks;
    private State initialState;
    private State goalState;
    
    public BlockWorld(Problem problem)
    {
        blocks = (ArrayList<Block>) problem.getConstants();
        initialState = problem.getInitialState();
        goalState = problem.getFinalState();
    }
    
    public void exec()
    {
        
    }
}

