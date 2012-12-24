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
    private FreeArm preFreeArm;
    private Free preFree;
    private OnTable preOnTable;
    
    private OnTable remOnTable;
    private FreeArm remFreeArm;
    private UsedColsNum remUsedColsNum;
    
    private PickedUp addPickedUp;
    private UsedColsNum addUsedColsNum;
    
    // Constructor
    
    public PickUp() 
    {
        super(null);
                
        preUsedColsNum = new UsedColsNum(0);
        preOnTable = new OnTable(null);
        preFreeArm = new FreeArm();
        preFree = new Free(null);
        
        remOnTable = new OnTable(null);
        remFreeArm = new FreeArm();
        remUsedColsNum = new UsedColsNum(0);
        
        addPickedUp = new PickedUp(null);
        addUsedColsNum = new UsedColsNum(-1);
        
        init();
    }    
    
    public PickUp(Block x) 
    {
        super(x);
        
        preUsedColsNum = new UsedColsNum(0);
        preOnTable = new OnTable(x);
        preFreeArm = new FreeArm();
        preFree = new Free(x);
        
        remOnTable = new OnTable(x);
        remFreeArm = new FreeArm();
        remUsedColsNum = new UsedColsNum(0);
        
        addPickedUp = new PickedUp(x);
        addUsedColsNum = new UsedColsNum(-1);

        init();
    }
    
    private void init()
    {
        addPrecondition(preUsedColsNum);
        addPrecondition(preFreeArm);
        addPrecondition(preFree);
        addPrecondition(preOnTable);
        
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
    
    public void instanciateUsedColsNum(UsedColsNum usedColsNum)
    {
        preUsedColsNum.setN(usedColsNum.getN());
        remUsedColsNum.setN(usedColsNum.getN());
        addUsedColsNum.setN(usedColsNum.getN()-1);
    }
    
}
