/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goalstackplanner;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author aclapes
 */
public class State {
    
    private ArrayList<Predicate> predicates;
    
    public State() 
    { 
        this.predicates = new ArrayList<Predicate>();
    }
    
    public State(ArrayList<Predicate> predicates) 
    {
        this.predicates = predicates;
    }

    public ArrayList<Predicate> getPredicates() 
    {
        return predicates;
    }

    public void setPredicates(ArrayList<Predicate> predicates)
    {
        this.predicates = predicates;
    }
    
    public void copy(State other)
    {
        this.predicates = other.getPredicates();
    }
    
    public boolean fullfills(Predicate predicate)
    {
        Iterator<Predicate> it = predicates.iterator();
        while(it.hasNext())
        {
            Predicate p = (Predicate) it.next();
            if ( p.equals(predicate) )
                return true;
        }
        
        return false;
    }

    public void removePredicate(Predicate predicate)
    {
        Predicate matchedPredicate = null;
        for (Predicate p : this.predicates)
        {
            if ( p.equals(predicate) )
            {
                matchedPredicate = p;
                break;
            }
        }
        
        if (matchedPredicate != null)
            this.predicates.remove(matchedPredicate);
    }
    
    public void addPredicate(Predicate predicate)
    {
        for (Predicate p : this.predicates)
        {
            if ( p.equals(predicate) )
                return;
        }

        this.predicates.add(predicate.clone());
    }  
    
     /**
     * addPredicate. Add specifying the adding position.
     * 
     * @param position
     * @param predicate
     */
    public void addPredicate(int position, Predicate predicate)
    {
        for (Predicate p : this.predicates)
        {
            if ( p.equals(predicate) )
                return;
        }

        this.predicates.add(position, predicate.clone());
    }  
    
    @Override
    public State clone() throws CloneNotSupportedException
    {
        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        for (Predicate p : this.predicates) 
        {
            predicates.add(p.clone());
        }
        
        State state = new State(predicates);
        
        return state;
    }
    
    public boolean equals(Object ob)
    {
        if (ob == null) return false;
        if (getClass() != ob.getClass()) return false;
        State other = (State) ob;
        
        int numSatisfiedPrecs = 0;
        int numNonSatisfiedPrecs = 0;
        for (Predicate pi : this.predicates)
        {
            boolean satisfied = false;
            for (Predicate pj: other.getPredicates())
            {
                if (pi.equals(pj))
                {
                    satisfied = true;
                    break;
                }
            }
            if (satisfied) numSatisfiedPrecs++;
            else numNonSatisfiedPrecs++;
        }
        if (numSatisfiedPrecs != this.predicates.size() || numNonSatisfiedPrecs > 0) return false;
        return true;
    }
    
    public void applyOperator(Operator operator)
    {      
        for (Predicate p : operator.getRemovings())
        {
            this.removePredicate(p);
        }
  
        for (Predicate p : operator.getAddings())
        {
            this.addPredicate(p);
        }
        
        
    }
}
