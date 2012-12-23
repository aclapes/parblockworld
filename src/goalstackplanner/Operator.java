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
public abstract class Operator 
{
    // Attributes
    private Constant c1;
    private Constant c2;
    
    private ArrayList<Predicate> addings;
    private ArrayList<Predicate> removings;
    private ArrayList<Predicate> preconditions;
    
    // Constructors
    public Operator()
    {
        this.c1 = null;
        this.c2 = null;
        
        addings = new ArrayList<Predicate>();
        removings = new ArrayList<Predicate>();
        preconditions = new ArrayList<Predicate>();
    }
            
    public Operator(Constant c1)
    {
        this.c1 = c1;
        this.c2 = null;
        
        addings = new ArrayList<Predicate>();
        removings = new ArrayList<Predicate>();
        preconditions = new ArrayList<Predicate>();
    }

    public Operator(Constant c1, Constant c2)
    {
        this.c1 = c1;
        this.c2 = c2;
        
        addings = new ArrayList<Predicate>();
        removings = new ArrayList<Predicate>();
        preconditions = new ArrayList<Predicate>();
    } 
    
    public Constant getC1() 
    {
        return c1;
    }

    public Constant getC2()
    {
        return c2;
    }

    public void setC1(Constant c1) 
    {
        if (c1 != null) this.c1 = c1.clone();
    }

    public void setC2(Constant c2) 
    {
        if (c2 != null) this.c2 = c2.clone();
    }
    
    public boolean equals(Operator other)
    {
        if (other == null) return false;
        if (other.getClass() != getClass()) return false;
        Operator otherOperator = (Operator) other;
        
        if (this.c1 != null && otherOperator.getC1() != null && !this.c1.equals(otherOperator.getC1())) 
            return false;
        if (this.c2 != null && otherOperator.getC2() != null && !this.c2.equals(otherOperator.getC2())) 
            return false;
        
        return true;
    }    

    public void setAddings(ArrayList<Predicate> addings) 
    {
        this.addings = addings;
    }

    public void setRemovings(ArrayList<Predicate> removings) 
    {
        this.removings = removings;
    }

    public void setPreconditions(ArrayList<Predicate> preconditions) 
    {
        this.preconditions = preconditions;
    }

    public ArrayList<Predicate> getAddings() 
    {
        return addings;
    }

    public ArrayList<Predicate> getRemovings() 
    {
        return removings;
    }

    public ArrayList<Predicate> getPreconditions() 
    {
        return preconditions;
    }
    
    protected void addAdding(Predicate p)
    {
        this.addings.add(p);
    }
    
    protected void addRemoving(Predicate p)
    {
        this.removings.add(p);
    }
    
    protected void addPrecondition(Predicate p)
    {
        this.preconditions.add(p);
    }
    
    public boolean fullfills(Predicate predicate) 
    {
        for (Predicate p : this.addings)
        {
            if ( p.getClass() == predicate.getClass() )
                return true;
        }
        
        return false;
    }
    
    @Override
    public abstract Operator clone();

    public abstract boolean isInstanciated();    
    
    public abstract void instanciate(Predicate predicate);
    
    public abstract void partiallyInstanciate(Predicate predicate);
}