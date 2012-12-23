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
        this.c1 = c;
        this.c2 = null;
    }    
    
    public Predicate(Constant c1, Constant c2)
    {
        this.c1 = c1;
        this.c2 = c2;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (this.c1 != null ? this.c1.hashCode() : 0);
        hash = 43 * hash + (this.c2 != null ? this.c2.hashCode() : 0);
        return hash;
    }
    
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
}
