/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Operator;
import goalstackplanner.Predicate;
import goalstackplanner.Problem;
import goalstackplanner.State;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author aclapes
 */
public class BlockWorld 
{
    // Stable variables
    private ArrayList<Block> blocks;
    private State goalState;
    private ArrayList<Operator> operators;
    
    // Unstable variables (change throughout time)
    private ArrayList<Operator> plan;
    private java.util.Stack stack;
    private State state;
    
    public BlockWorld(Problem problem)
    {
        // Prepare pre-loaded information from the parser
        blocks = (ArrayList<Block>) problem.getConstants();
        state = problem.getInitialState();
        goalState = problem.getFinalState();
        
        // Establish available operators
        this.operators = new ArrayList<Operator>();
        this.operators.add(new PickUp());
        this.operators.add(new Leave());
        this.operators.add(new Unstack());
        this.operators.add(new blockworld.Stack());
        
        // Prepare stack: push goal state
        stack = new java.util.Stack();
        stack.push(goalState.getPredicates());
        System.out.println(goalState.getPredicates().size());
        for (Predicate p : goalState.getPredicates())
            stack.push(p);
        
        // Prepare actions list
        plan = new ArrayList<Operator>();
    }
    
    public void exec()
    {
        while(!stack.empty())
        {   
            showStack();
            
            ArrayList<Predicate> predicates = this.state.getPredicates(); // DEBUG
            Object obj = stack.pop();
            
            if (obj instanceof Predicate && !((Predicate) obj).isInstanciated()) 
            {
                Predicate predicate = (Predicate) obj;
                predicate.instanciate(state);
                instanciateStackedElements(predicate);
                stack.push(predicate);
            }
            else if (obj instanceof Predicate && ((Predicate) obj).isInstanciated())
            {
                Predicate predicate = (Predicate) obj;
                // Is this instanciated precondition fullfilled in the current state?
                boolean bFullfills = state.fullfills(predicate);
                if (bFullfills) // It is? Move on.
                    continue;
                else // It is not fullfilled? Wait.. you should.
                {
                    ArrayList<Operator> candidates = findOperatorToFullfilPredicate(predicate);
                    Operator operator = heuristicOperatorChoice(candidates, predicate);
                    operator.instanciate(predicate);
                    stackOperator(operator);
                }
                
            }
            else if (obj instanceof ArrayList) 
            {
            }
            else if (obj instanceof Operator) 
            {
                Operator operator = (Operator) obj;
                state.applyOperator(operator);
                plan.add(operator);           
            }
        }
    }
    
    private void stackOperator(Operator operator)
    {
        this.stack.push(operator);
        this.stack.push(operator.getPreconditions());
        
        // Stacking UsedColsNum just before the operator.
        
        Predicate usedColsNumPredicate = null;
        for (Predicate condition : operator.getPreconditions())
           {
            if (condition instanceof UsedColsNum)
            {
                usedColsNumPredicate = condition;
                break;
            }
        }
        
        if (usedColsNumPredicate != null)
        {
            this.stack.push(usedColsNumPredicate);
            operator.getPreconditions().remove(usedColsNumPredicate);
        }
        
        for (Predicate condition : operator.getPreconditions())
        {
            this.stack.push(condition);
        }
    }
    
    private ArrayList<Operator> findOperatorToFullfilPredicate(Predicate p)
    {
        ArrayList<Operator> candidates = new ArrayList<Operator>();
        for (Operator op : this.operators) 
        {
            if (op.fullfills(p))
                candidates.add(((Operator) op).clone());
        }
        
        return candidates;
    }
    
    private Operator heuristicOperatorChoice(ArrayList<Operator> candidates, Predicate predicate)
    {
        Operator operator = null;
        
        if (candidates.size() > 1)
        {
            if (predicate instanceof PickedUp)
            {
                operator = pickedUpHeuristic(candidates, predicate);
            }
            else if (predicate instanceof FreeArm)
            {
                operator = freeArmHeuristic(candidates);
            }
        }
        // Heuristics to choice among several possibilities.
        if (operator != null)
            return operator;
        else
            return candidates.get(0);
    }
        
    public Operator pickedUpHeuristic(ArrayList<Operator> candidates, Predicate predicate)
    {
        if ( candidates.get(0) instanceof PickUp && candidates.get(1) instanceof Unstack )
        {
            Block x = (Block) predicate.getC1();
            OnTable onTablePredicate = new OnTable(x);
            if (state.fullfills(onTablePredicate)) 
                return candidates.get(0);
            else 
                return candidates.get(1);
        }
        else if ( candidates.get(0) instanceof Unstack && candidates.get(1) instanceof PickUp )
        {
            Block x = (Block) predicate.getC1();
            OnTable onTablePredicate = new OnTable(x);
            if (state.fullfills(onTablePredicate)) 
                return candidates.get(1);
            else 
                return candidates.get(0);
        }
        
        return candidates.get(0);
    }
    
    public Operator freeArmHeuristic(ArrayList<Operator> candidates)
    {
        int usedColsNum = 0;
        for (Predicate p : state.getPredicates())
        {
            if (p instanceof UsedColsNum)
            {
                usedColsNum = ((UsedColsNum) p).getN();
                break;
            }
        }
        
        if ( candidates.get(0) instanceof Leave && candidates.get(1) instanceof Stack )
        {
            if (usedColsNum < 3) return candidates.get(0);
            else return candidates.get(1);
        }
        else if ( candidates.get(0) instanceof Stack && candidates.get(1) instanceof Leave )
        {
            if (usedColsNum < 3) return candidates.get(1);
            else return candidates.get(0);
        }
        
        return candidates.get(0);
    }
    
    public void instanciateStackedElements(Predicate predicate)
    {
        for (int i = this.stack.size() - 1; i >= 0; i--)
        {
            Object obj = this.stack.get(i);
            if (obj instanceof Operator)
            {
                Operator op = (Operator) obj;
                if (!op.isInstanciated())
                    op.partiallyInstanciate(predicate);
                
                return;
            }
        }

    }
    
    public void showStack()
    {
        Iterator<Object> it = stack.iterator();
        String stackStr = "+--------------------------------------+\n"
                        + "|           STACK (" + stack.size() + ")\n"
                        + "+======================================+\n";
        
        String whitespaces = "        ";
        Integer c = 0;
        while (it.hasNext())
        {
            Object obj = it.next();
            
            String suffix = c.toString() + whitespaces.substring(c.toString().length());
            if (!it.hasNext()) suffix = "        ---<> ";
            
            if (obj instanceof ArrayList) {
                ArrayList<Predicate> predicatesList = (ArrayList<Predicate>) obj;
                stackStr += suffix + "| {list}\n";
            }
            else if (obj instanceof Predicate)
            {
                Predicate p = (Predicate) obj;
                stackStr += suffix + "| " + p.toString() +"\n";
            }
            else if (obj instanceof Operator)
            {
                Operator op = (Operator) obj;
                stackStr += suffix + "| " + op.toString() +"\n";
            }
            
            c++;
        }
        stackStr += "+--------------------------------------+\n";
        System.out.println(stackStr);
    }
}

