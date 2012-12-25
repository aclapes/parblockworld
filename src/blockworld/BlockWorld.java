/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Constant;
import goalstackplanner.Operator;
import goalstackplanner.Predicate;
import goalstackplanner.Problem;
import goalstackplanner.State;
import java.util.ArrayList;
import java.util.Collections;
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
        int iter = 0;
        
        while(!stack.empty())
        {   
            showStack(iter++);
            
            heuristicConditionsRearrange(this.state.getPredicates());// DEBUG
            ArrayList<Predicate> predicates = this.state.getPredicates();
                    
            Object obj = stack.pop();
            
            if (obj instanceof Predicate && !((Predicate) obj).isInstanciated()) 
            {
                Predicate predicate = (Predicate) obj;
                if (predicate instanceof UsedColsNum)
                {
                    ((UsedColsNum) predicate).instanciate(state);
                    instanciateStackedElements(predicate);
                }
                else
                {
                    ArrayList<Block> blockCandidates = (ArrayList<Block>) predicate.getInstanciationCandidates(state);
                    heuristicPredicateInstanciation(predicate, blockCandidates);
                    instanciateStackedElements(predicate);
                }
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
                
                if (operator instanceof Stack) // Heuristics
                    ((Block)operator.getC1()).stackOn((Block) operator.getC2());
                
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
        
        ArrayList<Integer> priorities = getOnTablePriorities(onTableConditions, preconditions);
        ArrayList<Predicate> sortedOnTableConditions = sortOnTables(onTableConditions, priorities); // TODO: petada quan no hi ha "On" predicates.  
        
//        ArrayList<Predicate> hSortedConditions = new ArrayList<Predicate>();
        preconditions.clear();
        preconditions.addAll(usedColsNumConditions);
        preconditions.addAll(freeArmConditions);

        preconditions.addAll(pickedUpConditions);
        preconditions.addAll(freeConditions);
        preconditions.addAll(sortedOnTableConditions);
        preconditions.addAll(onConditions);
   
        
        
        preconditions.addAll(heavierConditions);
    }
    
    private Block putOnHeaviestNotRepeating(Predicate predicate, ArrayList<Block> candidateBlocks)
    {
        Block bestCandidate = candidateBlocks.get(0);
        int maxCounts = 0;
        for (Block b : candidateBlocks)
        {
            if (b.equals( ((Block) predicate.getC2()).lastStackOn() ) )
                continue;

            int counts = 0;
            for (Predicate p : this.state.getPredicates() )
            {
                if (p instanceof Heavier && b.equals(p.getC1()))
                    counts++;
            }
            if (counts > maxCounts)
            {
                bestCandidate = b;
                maxCounts = counts;
            }       
        }
        return bestCandidate;
    }
    
    private Block putOnLightestNotRepeating(Predicate predicate, ArrayList<Block> candidateBlocks)
    {
        Block bestCandidate = null;
        int minCounts = 10000;
        for (Block b : candidateBlocks)
        {
            if (b.equals( ((Block) predicate.getC2()).lastStackOn() ) )
                continue;

            int counts = 0;
            for (Predicate p : this.state.getPredicates() )
            {
                if (p instanceof Heavier && b.equals(p.getC1()))
                    counts++;
            }
            if (counts < minCounts)
            {
                bestCandidate = b;
                minCounts = counts;
            }       
        }
        
        if (bestCandidate == null)
            return candidateBlocks.get((int) (Math.random() * candidateBlocks.size()));
        else
            return bestCandidate;
    }
    
    private Block putOnRandomlyNotRepeating(Predicate predicate, ArrayList<Block> candidateBlocks)
    {
        ArrayList<Block> notRepeatedCandidates = new ArrayList<Block>();
        for (Block b : candidateBlocks)
        {
            if (b.equals( ((Block) predicate.getC2()).lastStackOn() ) )
                continue;
            
            notRepeatedCandidates.add(b);
        }
        int idx = (int) (Math.random() * notRepeatedCandidates.size()); 
        
        return notRepeatedCandidates.get(idx);
    }
          
    private void heuristicPredicateInstanciation(Predicate predicate, ArrayList<Block> candidateBlocks)
    {
        
        if (predicate instanceof Heavier)
        {

            //Block b = putOnHeaviestNotRepeating(predicate, candidateBlocks);
            //Block b = putOnRandomlyNotRepeating(predicate, candidateBlocks);   
            Block b = putOnLightestNotRepeating(predicate, candidateBlocks); 
            predicate.setC1(b);
        }
        else if (predicate instanceof On)
        {
            if (predicate.getC1() == null)
                predicate.setC1(candidateBlocks.get(0));
            else
                predicate.setC2(candidateBlocks.get(0));
        }
        else
            predicate.setC1(candidateBlocks.get(0));
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
                Predicate pickedUp = state.getPredicate(PickedUp.class);
//                Block x = (Block) pickedUp.getC1();
                operator = freeArmHeuristic(candidates);
                operator.instanciate(pickedUp);
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
            else 
                return candidates.get(1);
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
    
    public void showStack(int iter)
    {
        Iterator<Object> it = stack.iterator();
        String stackStr = "+--------------------------------------+\n"
                        + "|           STACK (" + iter + ")\n"
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
    
//    private UsedColsNum getUsedColsNumPredicate()
//    {
//        UsedColsNum usedColsNumPredicate = null;
//        for (Predicate p : state.getPredicates())
//        {
//            if (p instanceof UsedColsNum)
//            {
//                usedColsNumPredicate = (UsedColsNum) p;
//                break;
//            }
//        }
//        
//        return usedColsNumPredicate;
//    }

    public ArrayList<Predicate> buildTower(On base, ArrayList<Predicate> preconditions)
    {
        ArrayList<Predicate> tower = new ArrayList<Predicate>();
        tower.add(base);
    
        boolean growing = true;
        
        do {
            growing = false;
            for (Predicate p : preconditions)
            {
                if (p instanceof On)
                {
                    On on = (On) p;
                    if ( tower.get(tower.size()-1).getC1().equals(on.getC2()) )
                    {
                        tower.add(on);
                        growing = true;
                        break;
                    }
                }
            }
        } while (growing);
        
        return tower;
    }
    
    public ArrayList<Predicate> sortOnTables(ArrayList<Predicate> onTables, ArrayList<Integer> heights)
    {
        ArrayList<Predicate> sortedOnTables = new ArrayList<Predicate>();
        ArrayList<Integer> sortedHeights = new ArrayList<Integer>();
        
        Integer i = 0;
        for (Predicate p : onTables)
        {
            OnTable ont = (OnTable) p;
            boolean inserted = false;
            for (int j = 0; !inserted && j < sortedOnTables.size(); j++)
            {
                if ( !(heights.get(i) > sortedHeights.get(j)) )
                {
                    sortedOnTables.add(j, p);
                    sortedHeights.add(j, heights.get(i));
                    inserted = true;
                }
            }
            
            if (!inserted)
            {
                sortedOnTables.add(p);
                sortedHeights.add(heights.get(i));
            }
            i++;
        }
        
        return sortedOnTables;
    }
    
    public ArrayList<Integer> getOnTablePriorities(ArrayList<Predicate> onTables, ArrayList<Predicate> preconditions)
    {
        ArrayList<Integer> counts = new ArrayList<Integer>();
        for (Predicate pi : onTables)
        {
            ArrayList<Predicate> tower = null;
            
            for (Predicate pj : preconditions)
            {
                if (pj instanceof On && pi.getC1().equals(pj.getC2()) )
                        tower = buildTower((On)pj, preconditions);
            }
            
            if (tower != null) 
                counts.add(tower.size());
            else
                counts.add(0);
        }
        
        return counts;
    }

}

