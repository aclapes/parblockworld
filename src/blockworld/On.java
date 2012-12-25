/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * On predicate. x is placed on y.
 * 
 * @author aclapes
 */
public class On extends Predicate 
{
    public On()
    {
        super();
    }
    
    public On(Block b1, Block b2)
    {
        super(b1, b2);
    }
    
    public int getNumConstants()
    {
        return 2;
    }
    
    @Override
    public String toString() {
        String s = "On ";
        
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
//        setC2(c);
//    }
    
    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;
        On other = (On) ob;
        
        if (!this.getC1().equals(other.getC1())) return false;
        if (!this.getC2().equals(other.getC2())) return false;
        
        return true;
    }
    
    @Override
    public On clone()
    {
        return new On((Block) getC1(), (Block) getC2());
    }
    
    public boolean dependsOn(On other)
    {
        if (this.getC1().equals(other.getC2()))
            return true;
        if (this.getC2().equals(other.getC1()))
            return true;
            
        return false;    
    }
    
    public boolean goesFirst(On other)
    {
        if (this.getC1().equals(other.getC2()))
            return true;
            
        return false;
    }
    
    public void instanciate(Constant c)
    {
        if (this.getC2() == null) this.setC2(c);
    }
}
