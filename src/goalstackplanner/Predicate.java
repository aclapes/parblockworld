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
public abstract class Predicate {
    
    // Attributes
    
    private Constant c1;
    private Constant c2;
    
    // Constructors

    public Predicate()
    {
        this.c1 = null;
        this.c2 = null;
    }
    
    public Predicate(Constant c)
    {
        if (c != null) this.c1 = c.clone();
        else this.c1 = null;
        this.c2 = null;
    }    
    
    public Predicate(Constant c1, Constant c2)
    {
        if (c1 != null) this.c1 = c1.clone();
        else this.c1 = null;
        if (c2 != null) this.c2 = c2.clone();
        else this.c2 = null;
    }
    
    public abstract int getNumConstants();
    
    // Methods

    @Override
    public abstract boolean equals(Object ob);
//    @Override
//    public boolean equals(Object ob) {
//        if (ob == null) return false;
//        if (ob.getClass() != getClass()) return false;
//        Predicate other = (Predicate) ob;
//        
//        // Not fully instanciated
//        if (this.c1 == null && !(other.getC1() == null))
//            return false;
//        if (!(this.c1 == null) && other.getC1() == null)
//        if (this.c2 == null && !(other.getC2() == null))
//            return false;
//        if (!(this.c2 == null) && other.getC2() == null)
//            return false;
//        
//        if (this.c1 == null && other.getC1() == null)
//            return true;
//
//        if (!this.c1.equals(other.getC1())) return false;
//        
//        if (this.c2 == null && other.getC2() == null)
//            return true;
//        
//        if (!this.c2.equals(other.getC2())) return false;
//            return true;
//    }    
    
    public Constant getC1()
    {
        return this.c1;
    }
    
    public Constant getC2()
    {
        return this.c2;
    }
    
    public abstract boolean isInstanciated();
    
//    public abstract void instanciate(Constant c1, Constant c2);
//
//    public abstract void partiallyInstanciate(Constant c);
    
    public void setC1(Constant c) {
        if (c != null) this.c1 = c.clone();
    }

    public void setC2(Constant c) {
        if (c != null) this.c2 = c.clone();
    }
    
    public abstract Predicate clone();
    
    public void instanciate(State state)
    {
        for (int i = state.getPredicates().size() - 1; i >= 0; i--)
        {
            Predicate p = state.getPredicates().get(i);
            if (p.getClass() == this.getClass())
            {
                // Operator(null) <- Operator(Predicate) , TODO several possibilities!!!
                if (this.getC1() == null && this.getC2() == null)
                {
                    this.setC1(p.getC1());
                    return;
                }
                else if (this.getC1() == null && this.getC2() != null)
                {
                    // Operator(Predicate, null) <- Operator(Predicate, Predicate)
                    if ( p.getC2().equals(this.getC2()) )
                    {
                        this.setC1(p.getC1());
                        return;
                    }
                }
                else if (this.getC1() != null && this.getC2() == null)
                {
                    // Operator(Predicate, null) <- Operator(Predicate, Predicate)
                    if ( p.getC1().equals(this.getC1()) )
                    {
                        this.setC2(p.getC2());
                        return;
                    }
                }
            }
        } 
    }
}
