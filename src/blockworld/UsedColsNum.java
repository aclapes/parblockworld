/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.*;

/**
 * UsedColsNum predicate. n columns are being used.
 * 
 * @author aclapes
 */
public class UsedColsNum extends Predicate 
{
    
    // Attributes
    private int n;
    
    // Constructors
    
    public UsedColsNum()
    {
        super();
               
        this.n = 0;
    }
    
    public UsedColsNum(int n)
    {
        super();
        
        this.n = n;
    }

    public int getN() {
        return n;
    }
    
    public void setN(int n)
    {
        this.n = n;
    }
    
    @Override
    public String toString() 
    {
        String s = "UsedColsNum " + String.valueOf(this.n);

        return s + "    PREDICATE";
    }

    @Override
    public int getNumConstants() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // TODO
    }

    @Override
    public boolean isInstanciated() 
    {
        return n > 0;
    }

    @Override
    public void instanciate(State state) 
    {
        for (Predicate p : state.getPredicates())
        {
            if (p instanceof UsedColsNum)
            {
                this.n += ((UsedColsNum) p).getN();
                break;
            }
        }
    }
//
//    @Override
//    public void partiallyInstanciate(Constant c) {
//        return;
//    }
    
    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;
        if (ob.getClass() != getClass()) return false;
        UsedColsNum other = (UsedColsNum) ob;
        
        if ( this.n != other.getN() ) return false;
        
        return true;
    }
    
    @Override
    public UsedColsNum clone()
    {
        return new UsedColsNum(this.n);
    }
}
