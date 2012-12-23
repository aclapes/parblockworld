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
    
    // Constructors
    
    public Block(String name)
    {
        super(name);
    }

    @Override
    public Block clone() {
        return new Block(getName());
    }
    
}
