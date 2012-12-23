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
        
        init();
    }
            
    public Operator(Constant c1)
    {
        this.c1 = c1;
        this.c2 = null;
        
        init();
    }

    public Constant getC1() {
        return c1;
    }

    public Constant getC2() {
        return c2;
    }

    public void setC1(Constant c1) {
        if (c1 != null) this.c1 = c1.clone();
    }

    public void setC2(Constant c2) {
        if (c2 != null) this.c2 = c2.clone();
    }

    public void setAddings(ArrayList<Predicate> addings) {
        this.addings = addings;
    }

    public void setRemovings(ArrayList<Predicate> removings) {
        this.removings = removings;
    }

    public void setPreconditions(ArrayList<Predicate> preconditions) {
        this.preconditions = preconditions;
    }

    public ArrayList<Predicate> getAddings() {
        return addings;
    }

    public ArrayList<Predicate> getRemovings() {
        return removings;
    }

    public ArrayList<Predicate> getPreconditions() {
        return preconditions;
    }
    
    public Operator(Constant c1, Constant c2)
    {
        this.c1 = c1;
        this.c2 = c2;
        
        init();
    } 
    
    private void init()
    {
        addings = new ArrayList<Predicate>();
        removings = new ArrayList<Predicate>();
        preconditions = new ArrayList<Predicate>();
    }
    
    protected void definePrecondition(Predicate p)
    {
        this.preconditions.add(p);
    }
    
    protected void defineAdding(Predicate p)
    {
        this.addings.add(p);
    }
    
    protected void defineRemoving(Predicate p)
    {
        this.removings.add(p);
    }
        
//    public void instanciate(Constant c)
//    {
//        Iterator<Predicate> it;
//        
//        it = this.preconditions.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c);
//            //instanciatePrecondition(it.next(), c);
//        }
//       
//        it = this.removings.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c);
//            //instanciatePrecondition(it.next(), c);
//        }
//
//        it = this.addings.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c);
//            //instanciatePrecondition(it.next(), c);
//        }        
//    }
//    
//    public void instanciate(Constant c1, Constant c2)
//    {
//        this.c1 = c1;
//        this.c2 = c2;
//        
//        Iterator<Predicate> it;
//        
//        it = this.preconditions.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c1, c2);
//            //instanciatePrecondition(it.next(), c);
//        }
//       
//        it = this.removings.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c1, c2);
//            //instanciatePrecondition(it.next(), c);
//        }
//
//        it = this.addings.iterator();
//        while (it.hasNext())
//        {
//            (it.next()).instanciate(c1, c2);
//            //instanciatePrecondition(it.next(), c);
//        }        
//    }
    
//    protected abstract void instanciatePrecondition(Predicate p, Constant c);
//    
//    protected abstract void instanciateRemoving(Predicate p, Constant c);
//    
//    protected abstract void instanciateAdding(Predicate p, Constant c);

    public boolean fullfills(Predicate predicate) 
    {
        for (Predicate p : this.addings)
        {
            if ( p.getClass() == predicate.getClass() )
                return true;
        }
        
        return false;
    }
    
    public ArrayList<Predicate> getConditions()
    {
        return this.preconditions;
    }
    
    @Override
    public abstract Operator clone();
    
    protected void deepCopy(Operator operator)
    {
        operator.setC1((getC1()).clone());
        operator.setC2((getC2()).clone());
        operator.setPreconditions( (ArrayList<Predicate>) this.getPreconditions().clone());
        operator.setRemovings( (ArrayList<Predicate>) this.getRemovings().clone());
        operator.setAddings( (ArrayList<Predicate>) this.getAddings().clone());
    }
    
    public abstract void instanciate(Predicate predicate);
    
    public abstract void partiallyInstanciate(Predicate predicate);
            
    public boolean equals(Operator other)
    {
        if (other == null) return false;
        if (other.getClass() != getClass()) return false;
        Operator otherOperator = (Operator) other;
        
        if (this.c1 != null && otherOperator.getC1() != null && !this.c1.equals(otherOperator.getC1())) return false;
        if (this.c2 != null && otherOperator.getC2() != null && !this.c2.equals(otherOperator.getC2())) return false;
        return true;
    }
    
    public abstract boolean isInstanciated();
}
