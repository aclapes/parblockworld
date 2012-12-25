/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goalstackplanner;

/**
 *
 * @author aclapes
 */
public abstract class Constant {
    
    // Members
    private String name;
    
    // Constructors
    public Constant(String name)
    {
        this.name = name;
    }
    
    // Methods
    public String getName()
    {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object ob) {
        if (ob == null) return false;

        if (getClass() != ob.getClass()) return false;
        Constant other = (Constant) ob;
        if ( (this.name).compareTo(other.getName()) != 0) return false;
        return true;
    }    
    
    @Override
    public abstract Constant clone();
    
    @Override
    public String toString()
    {
        return name;
    }
}
