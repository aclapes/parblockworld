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
    private Operator lastOperator;
    
    public BlockWorld(Problem problem)
    {
        // Prepare pre-loaded information from the parser
        blocks = (ArrayList<Block>) problem.getConstants();
        
        heuristicConditionsRearrange(problem.getInitialState().getPredicates());
        heuristicConditionsRearrange(problem.getFinalState().getPredicates());
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
//                    if (operator instanceof Leave)
//                        ((Leave) operator).instanciateUsedColsNum(getUsedColsNumPredicate());
//                    else if (operator instanceof PickUp)
//                        ((PickUp) operator).instanciateUsedColsNum(getUsedColsNumPredicate());
                    
                    stackOperator(operator);
                }
                
            }
            else if (obj instanceof ArrayList) 
            {
                ArrayList<Predicate> conditions = (ArrayList<Predicate>) obj;
                fulfillState(conditions);
            }
            else if (obj instanceof Operator) 
            {
                Operator operator = (Operator) obj;
                state.applyOperator(operator);
                lastOperator = operator.clone();
                plan.add(operator);           
            }
        }
        
        String s = "";
        for (Operator action : this.plan)
        {
            s += action.toString() + "\n";
        }
        System.out.println(s);
    }
    
    private void stackOperator(Operator operator)
    {
        this.stack.push(operator);
        this.stack.push(operator.getPreconditions());
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
    
    private void heuristicConditionsRearrange(ArrayList<Predicate> preconditions)
    {
        ArrayList<Predicate> onTableConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> onConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> freeConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> freeArmConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> pickedUpConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> usedColsNumConditions = new ArrayList<Predicate>();
        ArrayList<Predicate> heavierConditions = new ArrayList<Predicate>();
        
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof OnTable)
                onTableConditions.add(predicate);
        }
        
        //
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof On)
            {
                On on = (On) predicate;
                if (onConditions.size() < 1)
                    onConditions.add(on);
                else
                {
                    boolean inserted = false;
                    for (int i = 0; i < onConditions.size() && !inserted; i++)
                    {
                        On on_i = (On) onConditions.get(i);
                        if (on.dependsOn(on_i))
                        {
                            if (on.goesFirst(on_i))
                                onConditions.add(i+1, on); // Going first is being late in the list (stack effect).
                            else 
                                onConditions.add(i, on);
                            
                            inserted = true;
                        }
                    }
                    
                    if (!inserted) onConditions.add(on);
                }
            }
        }
       
        
        //
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof Free)
            {
                freeConditions.add(predicate);
            }
        }
        
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof FreeArm)
            {
                freeArmConditions.add(predicate);
            }
        }
        
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof PickedUp)
            {
                pickedUpConditions.add(predicate);
            }
        }
        
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof UsedColsNum)
                usedColsNumConditions.add(predicate);
        }
        
        for (Predicate predicate : preconditions)
        {
            if (predicate instanceof Heavier)
                heavierConditions.add(predicate);
        }
        
//        ArrayList<Predicate> hSortedConditions = new ArrayList<Predicate>();
        preconditions.clear();
        preconditions.addAll(usedColsNumConditions);
        preconditions.addAll(freeArmConditions);

        preconditions.addAll(pickedUpConditions);
        preconditions.addAll(freeConditions);
        preconditions.addAll(onConditions);
        preconditions.addAll(onTableConditions);
        
        preconditions.addAll(heavierConditions);
    }
    
    private Operator heuristicOperatorChoice(ArrayList<Operator> candidates, Predicate predicate)
    {
        Operator operator = null;
        
        if (candidates.size() > 1)
        {
//            if (lastOperator instanceof Leave)
//            {
//                for (Operator o : candidates)
//                {
//                    if (o instanceof PickUp)
//                        candidates.remove(o);
//                }
//            }
            
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
        if (predicate instanceof UsedColsNum)
        {
            UsedColsNum usedColsNum = (UsedColsNum) predicate;
            for (int i = this.stack.size() - 1; i >= 0; i--)
            {
                Object obj = this.stack.get(i);
                if (obj instanceof Operator)
                {
                    Operator op = (Operator) obj;
                    
                    if (op instanceof Leave)
                        ((Leave) op).instanciateUsedColsNum(usedColsNum);
                    else if (op instanceof PickUp)
                        ((PickUp) op).instanciateUsedColsNum(usedColsNum);

                    return;
                }
            }
        }
        
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
    
    public void fulfillState(ArrayList<Predicate> conditions)
    {
        ArrayList<Predicate> nonFulfilledList = new ArrayList<Predicate>();
        
        for (Predicate condition : conditions)
        {
            boolean conditionFulfilled = false;
            for (Predicate predicate : this.state.getPredicates())
            {
                if (condition.equals(predicate))
                {
                    conditionFulfilled = true;
                    break;
                }
            }
            if (!conditionFulfilled && !(condition instanceof UsedColsNum))
                nonFulfilledList.add(condition);
        }
        
        if (!nonFulfilledList.isEmpty())
        {
            this.stack.push(conditions);
            for (Predicate condition : nonFulfilledList)
            {
                stack.push(condition);
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
    
    private UsedColsNum getUsedColsNumPredicate()
    {
        UsedColsNum usedColsNumPredicate = null;
        for (Predicate p : state.getPredicates())
        {
            if (p instanceof UsedColsNum)
            {
                usedColsNumPredicate = (UsedColsNum) p;
                break;
            }
        }
        
        return usedColsNumPredicate;
    }
}

