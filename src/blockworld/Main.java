/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import java.io.IOException;

/**
 *
 * @author aclapes
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    
//    public static boolean dependsOn(On a, On b)
//    {
//        if (a.getC1().equals(b.getC2()))
//            return true;
//        if (a.getC2().equals(b.getC1()))
//            return true;
//            
//        return false;    
//    }
    
//    public static boolean goesFirst(On a, On b)
//    {
//        if (a.getC2().equals(b.getC1()))
//            return true;
//            
//        return false;
//    }
    
    public static void main(String[] args) throws IOException, CloneNotSupportedException 
    {
//        Block a = new Block("A");
//        Block b = new Block("B");
//        Block c = new Block("C");
//        Block d = new Block("D");
//        Block e = new Block("E");
//        Block f = new Block("F");
//        ArrayList<Predicate> preconditions = new ArrayList<Predicate>();
//        preconditions.add(new OnTable(d));
//        preconditions.add(new OnTable(e));
//        
//        preconditions.add(new On(b,c));
//        preconditions.add(new On(a,b));
//        preconditions.add(new On(c,d));
//        
//        preconditions.add(new On(f,e));
//        
        
        BlockWorldParser parser = new BlockWorldParser("init.txt");
        BlockWorld blockWorld = new BlockWorld(parser.parse());
        blockWorld.exec();
    }
}
