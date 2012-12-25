/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * Pickedup predicate. The object x is being held by the robot arm.
 * 
 * @author aclapes
 */
public class PickedUp extends Predicate 
{
    public PickedUp()
    {
        super();
    }
    
    public PickedUp(Block x) 
    {
        super(x);
    }
    
    public int getNumConstants()
    {
        return 1;
    }
    
    @Override
    public String toString() {
        String s = "PickedUp ";
        
        if (getC1() != null)
            s += getC1().getName();
        else if (getC1() == null)
            s += '_';
        
        return s + "    PREDICATE";
    }    
    
    @Override
    public boolean isInstanciated() 
    {
        return this.getC1() != null;
    }

//    @Override
//    public void instanciate(Constant c1, Constant c2) {
//        setC1(c1);
//    }
//
//    @Override
//    public void partiallyInstanciate(Constant c) {
//        return;
//    }
    
    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;
        PickedUp other = (PickedUp) ob;
        
        if (!this.getC1().equals(other.getC1())) return false;
        
        return true;
    }
    
    @Override
    public PickedUp clone()
    {
        return new PickedUp((Block) getC1());
    }
}
