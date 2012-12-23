/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 *
 * @author aclapes
 */
public class Stack extends Operator 
{   
    private PickedUp prePickedUp;
    private Free preFree;
    private Heavier preHeavier;
    
    private PickedUp remPickedUp;
    private Free remFree;
    
    private On addOn;
    private FreeArm addFreeArm;
    
    public Stack() 
    {
        super(null, null);
        
        prePickedUp = new PickedUp(null);
        preFree = new Free(null);
        preHeavier = new Heavier(null, null);
        
        remPickedUp = new PickedUp(null);
        remFree = new Free(null);
        
        addOn = new On(null, null);
        addFreeArm = new FreeArm();
        
        init();
    }    
    
    public Stack(Block x, Block y) {
        super(x, y);
        
        prePickedUp = new PickedUp(x);
        preFree = new Free(y);
        preHeavier = new Heavier(y, x);
        
        remPickedUp = new PickedUp(x);
        remFree = new Free(y);
        
        addOn = new On(x,y);
        addFreeArm = new FreeArm();
       
        init();
    }
    
    private void init()
    {
        addPrecondition(prePickedUp);
        addPrecondition(preFree);
        addPrecondition(preHeavier);
        
        addRemoving(remPickedUp);
        addRemoving(remFree);
        
        addAdding(addOn);
        addAdding(addFreeArm);
    }
            
    
    @Override
    public String toString() {
        String s = "Stack ";
        
        if (getC1() != null && getC2() != null)
            s += getC1().getName() + " " + getC2().getName();
        else if (getC1() != null && getC2() == null)
            s += getC1().getName() + " _";
        else if (getC1() == null && getC2() != null)
            s += "_ " + getC2().getName();
        
        return s + "    OPERATOR";
    }     
    
    @Override
    public Stack clone()
    {
        Stack stackClone = new Stack();
//      super.deepCopy(stackClone);
        
        return stackClone;
    }
    
    @Override
    public boolean isInstanciated() {
        return getC1() != null && getC2() != null;
    }

    @Override
    public void instanciate(Predicate predicate) 
    {
        Block x = (Block) predicate.getC1();
        Block y = (Block) predicate.getC2();
        
        setC1(x);
        setC2(y);
        
        prePickedUp.setC1(x);
        preFree.setC1(y);
        preHeavier.setC1(y);
        preHeavier.setC2(x);

        remPickedUp.setC1(x);
        remFree.setC1(y);
        
        addOn.setC1(x);
        addOn.setC2(y);
        
    }

    @Override
    public void partiallyInstanciate(Predicate predicate)
    {
        Block x = (Block) predicate.getC1();
        Block y = (Block) predicate.getC2();
        
        if (getC1() == null && getC2() == null) // Not instanciated at all?
        {
            instanciate(predicate);
            return;
        }
        
        if (getC1() == null)
        {
            setC1(x);
            
            prePickedUp.setC1(x);
            preHeavier.setC2(x);
            
            remPickedUp.setC1(x);
            
            addOn.setC1(x);
            
        }
        else if (getC2() == null)
        {
            setC2(y);

            preFree.setC1(y);
            preHeavier.setC1(y);
            
            remFree.setC1(y);
            
            addOn.setC2(y);
        }
    }
}
