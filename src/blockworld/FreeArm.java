/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * FreeArm predicate. The robotic arm is not holding any object.
 * 
 * @author aclapes
 */
public class FreeArm extends Predicate 
{
    public FreeArm() 
    {

    }

    @Override
    public int getNumConstants()
    {
        return 0;
    }
    
    @Override
    public String toString() {
        return "FreeArm";
    }
    
    @Override
    public boolean isInstanciated()
    {
        return true;
    }

//    @Override
//    public void instanciate(Constant c1, Constant c2)
//    {
//        return;
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
        
        return true;
    }
    
    @Override
    public FreeArm clone()
    {
        return new FreeArm();
    }
}
