/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Constant;

/**
 *
 * @author aclapes
 */
public class Block extends Constant 
{
    
    private Block lastStackOver;
    // Constructors
    
    public Block(String name)
    {
        super(name);
    }

    @Override
    public Block clone() {
        Block clone = new Block(getName());
        clone.stackOn(this.lastStackOn());
        return clone;
    }
    
    @Override
    public String toString()
    {
        return super.toString();
    }
    
    public Block lastStackOn()
    {
        return this.lastStackOver;
    }
    
    public void stackOn(Block block)
    {
        this.lastStackOver = block;
    }
}
