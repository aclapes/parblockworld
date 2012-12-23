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
public class Unstack extends Operator 
{    
    private On preOn;
    private Free preFree;
    private FreeArm preFreeArm;
    
    private On remOn;
    private FreeArm remFreeArm;
    
    private PickedUp addPickedUp;
    private Free addFree;
    
    public Unstack(Block x, Block y) 
    {
        super(x, y);
        
        preOn = new On(x,y);
        preFree = new Free(x);
        preFreeArm = new FreeArm();
        
        remOn = new On(x,y);
        remFreeArm = new FreeArm();
        
        addPickedUp = new PickedUp(x);
        addFree = new Free(y);
        
        init();
    }

    public Unstack() {
        super(null, null);
        
        preOn = new On(null, null);
        preFree = new Free(null);
        preFreeArm = new FreeArm();
        
        remOn = new On(null, null);
        remFreeArm = new FreeArm();
        
        addPickedUp = new PickedUp(null);
        addFree = new Free(null);
        
        init();
    }
    
    private void init()
    {
        addPrecondition(preFree);
        addPrecondition(preFreeArm);
        addPrecondition(preOn);
        
        addRemoving(remOn);
        addRemoving(remFreeArm);
        
        addAdding(addPickedUp);
        addAdding(addFree);
    }
    
    @Override
    public String toString() {
        String s = "Unstack ";
        
        if (getC1() != null && getC2() != null)
            s += getC1().getName() + " " + getC2().getName();
        else if (getC1() != null && getC2() == null)
            s += getC1().getName() + " _";
        else if (getC1() == null && getC2() != null)
            s += "_ " + getC2().getName();
        
        return s + "    OPERATOR";
    }   
    
    @Override
    public Unstack clone()
    {
        Unstack unstackClone = new Unstack();
        //super.deepCopy(unstackClone);
        
        return unstackClone;
    }

    @Override
    public boolean isInstanciated() {
        return getC1() != null && getC2() != null;
    }

    @Override
    public void instanciate(Predicate predicate)
    {
        Block x;
        Block y;
        
        if (predicate instanceof PickedUp)
        {
            x = (Block) predicate.getC1();
            y = null;
        }
        else
        {
            x = null;
            y = (Block) predicate.getC1();
        }
        
        setC1(x);
        setC2(y);
        
        preOn.setC1(x);
        preOn.setC2(y);
        preFree.setC1(x);
        
        remOn.setC1(x);
        remOn.setC2(y);
        
        addPickedUp.setC1(x);
        addFree.setC1(y);
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

            preOn.setC1(x);      
            preFree.setC1(x);
            remOn.setC1(x);
            addPickedUp.setC1(x);
        }
        else if (getC2() == null)
        {
            setC2(y);
            
            preOn.setC2(y);
            remOn.setC2(y);
            addFree.setC1(y);
        }
    }
}
