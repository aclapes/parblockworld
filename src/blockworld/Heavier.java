/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * Heavier predicate. The object x weights more than y.
 * 
 * @author aclapes
 */
public class Heavier extends Predicate 
{
    public Heavier() 
    {
        super();
    }
    
    public Heavier(Block x, Block y)
    {
        super(x, y);
    }
    
    public int getNumConstants()
    {
        return 2;
    } 
    
    @Override
    public String toString() {
        String s = "Heavier ";
        
        if (getC1() != null && getC2() != null)
            s += getC1().getName() + " " + getC2().getName();
        else if (getC1() != null && getC2() == null)
            s += getC1().getName() + " _";
        else if (getC1() == null && getC2() != null)
            s += "_ " + getC2().getName();
        
        return s + "    PREDICATE";
    }    
    
    @Override
    public boolean isInstanciated() 
    {
        return this.getC1() != null && this.getC2() != null;
    }

//    @Override
//    public void instanciate(Constant c1, Constant c2) {
//        setC1(c1);
//        setC2(c2);
//    }
//
//    @Override
//    public void partiallyInstanciate(Constant c) {
//        
//        setC2(c);
//    }
    
    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;
        Heavier other = (Heavier) ob;
        
        if (!this.getC1().equals(other.getC1())) return false;
        if (!this.getC2().equals(other.getC2())) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    @Override
    public Heavier clone()
    {
        return new Heavier((Block) getC1(), (Block) getC2());
    }
    
    public void instanciate(Block x)
    {
        if (this.getC1() == null) this.setC1(x);
    }
}
