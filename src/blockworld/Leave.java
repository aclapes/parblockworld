/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * Operator
 * 
 * @author aclapes
 */
public class Leave extends Operator 
{
    private PickedUp prePickedUp;
    private UsedColsNum preUsedColsNum;
    
    private PickedUp remPickedUp;
    private UsedColsNum remUsedColsNum;
    
    private OnTable addOnTable;
    private UsedColsNum addUsedColsNum;
    private FreeArm addFreeArm;

    public Leave() 
    {
        super(null);
        
        prePickedUp = new PickedUp(null);
        preUsedColsNum = new UsedColsNum(0);
        
        remPickedUp = new PickedUp(null);
        remUsedColsNum = new UsedColsNum(0);
        
        addOnTable = new OnTable(null);
        addUsedColsNum = new UsedColsNum(1);
        addFreeArm = new FreeArm();        
        
        init();
    }
    
    public Leave(Block x) {
        super(x);
        
        prePickedUp = new PickedUp(x);
        preUsedColsNum = new UsedColsNum(0);
        
        remPickedUp = new PickedUp(x);
        remUsedColsNum = new UsedColsNum(0);
        
        addOnTable = new OnTable(x);
        addUsedColsNum = new UsedColsNum(1);
        addFreeArm = new FreeArm();
        
        init();
    }
    
    private void init()
    {
        addPrecondition(preUsedColsNum);
        addPrecondition(prePickedUp);
        //definePrecondition(new FreeTable()); // n < 3
        
        addRemoving(remPickedUp);
        addRemoving(remUsedColsNum);
        
        addAdding(addOnTable);
        addAdding(addUsedColsNum);
        addAdding(addFreeArm);
    }
    
    @Override
    public String toString() {
        String s = "Leave ";
        
        if (getC1() != null)
            s += getC1().getName();
        else if (getC1() == null)
            s += '_';
        
        return s + "    OPERATOR";
    }
    
    @Override
    public Leave clone()
    {
        Leave leaveClone = new Leave();
        return leaveClone;
    }
    
    @Override
    public boolean isInstanciated() {
        return getC1() != null;
    }

    @Override
    public void instanciate(Predicate predicate) 
    {
        Block x = (Block) predicate.getC1();
        Block y = (Block) predicate.getC2();
        
        setC1(x);
        
        prePickedUp.setC1(x);

        remPickedUp.setC1(x);
        
        addOnTable.setC1(x);
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
        addUsedColsNum.setN(usedColsNum.getN()+1);
    }
}
