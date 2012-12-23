/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * Free predicate. x does not have any object on it.
 * 
 * @author aclapes
 */
public class Free extends Predicate 
{    
    public Free()
    {
        super();
    }
    
    public Free(Block x) {
        super(x);
    }
    
    public int getNumConstants()
    {
        return 1;
    }
    
    @Override
    public String toString() {
        String s = "Free ";
        
        if (getC1() != null)
            s += getC1().getName();
        else if (getC1() == null)
            s += '_';
        
        return s + "    PREDICATE";
    }
    
    @Override
    public boolean isInstanciated()
    {
        return getC1() != null;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;
        Free other = (Free) ob;
        if (!this.getC1().equals(other.getC1())) return false;
//        if (getC1() != null && other.getC1() != null)
//            if (!this.getC1().equals(other.getC1())) return false;
//        else if (getC1() != null && other.getC2() != null)
//            if (!this.getC1().equals(other.getC2())) return false;
//        else if (getC2() != null && other.getC1() != null)
//            if (!this.getC2().equals(other.getC1())) return false;
//        else if (getC2() != null && other.getC2() != null)
//            if (!this.getC2().equals(other.getC2())) return false;
        
        return true;
    }
    
    @Override
    public Free clone()
    {
        return new Free((Block) getC1().clone());
    }    
}
