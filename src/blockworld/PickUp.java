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
public class PickUp extends Operator 
{
    BlockWorld blockWorld;
    
    private UsedColsNum preUsedColsNum;
    private OnTable preOnTable;
    private FreeArm preFreeArm;
    private Free preFree;
    
    private OnTable remOnTable;
    private FreeArm remFreeArm;
    private UsedColsNum remUsedColsNum;
    
    private PickedUp addPickedUp;
    private UsedColsNum addUsedColsNum;
    
    // Constructor
    
    public PickUp() 
    {
        super(null);
                
        preUsedColsNum = new UsedColsNum();
        preOnTable = new OnTable(null);
        preFreeArm = new FreeArm();
        preFree = new Free(null);
        
        remOnTable = new OnTable(null);
        remFreeArm = new FreeArm();
        remUsedColsNum = new UsedColsNum();
        
        addPickedUp = new PickedUp(null);
        addUsedColsNum = new UsedColsNum();
        
        init();
    }    
    
    public PickUp(Block x) 
    {
        super(x);
        
        preUsedColsNum = new UsedColsNum();
        preOnTable = new OnTable(x);
        preFreeArm = new FreeArm();
        preFree = new Free(x);
        
        remOnTable = new OnTable(x);
        remFreeArm = new FreeArm();
        remUsedColsNum = new UsedColsNum();
        
        addPickedUp = new PickedUp(x);
        addUsedColsNum = new UsedColsNum();

        init();
    }
    
    private void init()
    {
        addPrecondition(preUsedColsNum);
        addPrecondition(preOnTable);
        addPrecondition(preFreeArm);
        addPrecondition(preFree);
        
        addRemoving(remOnTable);
        addRemoving(remFreeArm);
        addRemoving(remUsedColsNum);
        
        addAdding(addPickedUp);
        addAdding(addUsedColsNum);
    }
    
    @Override
    public String toString() {
        String s = "PickUp ";
        
        if (getC1() != null)
            s += getC1().getName();
        else if (getC1() == null)
            s += '_';
        
        return s + "    OPERATOR";
    }
    
    @Override
    public PickUp clone()
    {
        PickUp pickUpClone = new PickUp();
        //super.deepCopy(pickUpClone);
        
        return pickUpClone;
    }
    
    @Override
    public boolean isInstanciated() {
        return getC1() != null;
    }

    @Override
    public void instanciate(Predicate predicate)
    {
        Constant c1 = predicate.getC1();
        Constant c2 = predicate.getC2();
        
        setC1(c1);
        
        preOnTable.setC1(c1);
        preFree.setC1(c1);
        
        remOnTable.setC1(c1);
        
        addPickedUp.setC1(c1);
    }
    
    @Override
    public void partiallyInstanciate(Predicate predicate)
    {
        if (!isInstanciated())
            instanciate(predicate);
    }
    
    public void instanciateUsedColsNum(int n)
    {
        preUsedColsNum.setN(n);
        
        remUsedColsNum.setN(n);
        
        addUsedColsNum.setN(n-1);
    }
    
}
