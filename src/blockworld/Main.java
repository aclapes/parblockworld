/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blockworld;

import goalstackplanner.Predicate;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author aclapes
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    
    public static boolean dependsOn(On a, On b)
    {
        if (a.getC1().equals(b.getC2()))
            return true;
        if (a.getC2().equals(b.getC1()))
            return true;
            
        return false;    
    }
    
    public static boolean goesFirst(On a, On b)
    {
        if (a.getC2().equals(b.getC1()))
            return true;
            
        return false;
    }
    public static ArrayList<Predicate> buildTower(On base, ArrayList<Predicate> preconditions)
    {
        ArrayList<Predicate> tower = new ArrayList<Predicate>();
        tower.add(base);
    
        boolean growing = true;
        
        do {
            growing = false;
            for (Predicate p : preconditions)
            {
                if (p instanceof On)
                {
                    On on = (On) p;
                    if ( tower.get(tower.size()-1).getC1().equals(on.getC2()) )
                    {
                        tower.add(on);
                        growing = true;
                        break;
                    }
                }
            }
        } while (growing);
        
        return tower;
    }
    
    public static ArrayList<Predicate> sortOnTables(ArrayList<Predicate> onTables, ArrayList<Integer> heights)
    {
        ArrayList<Predicate> sortedOnTables = new ArrayList<Predicate>();
        ArrayList<Integer> sortedHeights = new ArrayList<Integer>();
        
        Integer i = 0;
        for (Predicate p : onTables)
        {
            OnTable ont = (OnTable) p;
            boolean inserted = false;
            for (int j = 0; !inserted && j < sortedOnTables.size(); j++)
            {
                if ( !(heights.get(i) > sortedHeights.get(j)) )
                {
                    sortedOnTables.add(j, p);
                    sortedHeights.add(j, heights.get(i));
                    inserted = true;
                }
            }
            
            if (!inserted)
            {
                sortedOnTables.add(p);
                sortedHeights.add(heights.get(i));
            }
            i++;
        }
        
        return sortedOnTables;
    }
    
    public static ArrayList<Integer> getOnTablePriorities(ArrayList<Predicate> onTables, ArrayList<Predicate> preconditions)
    {
        ArrayList<Integer> counts = new ArrayList<Integer>();
        for (Predicate pi : onTables)
        {
            ArrayList<Predicate> tower = null;
            
            for (Predicate pj : preconditions)
            {
                if (pi.getC1().equals(pj.getC2()))
                    tower = buildTower((On)pj, preconditions);
            }
            
            if (tower != null) counts.add(tower.size());
        }
        return counts;
    }
    
    public static void main(String[] args) throws IOException, CloneNotSupportedException 
    {
        Block a = new Block("A");
        Block b = new Block("B");
        Block c = new Block("C");
        Block d = new Block("D");
        Block e = new Block("E");
        Block f = new Block("F");
        ArrayList<Predicate> preconditions = new ArrayList<Predicate>();
        OnTable ont1 = new OnTable(d);
        preconditions.add(ont1);
        OnTable ont2 = new OnTable(e);
        preconditions.add(ont2);
        ArrayList<Predicate> ontables = new ArrayList<Predicate>();
        ontables.add(ont1);
        ontables.add(ont2);
                
        preconditions.add(new On(b,c));
        preconditions.add(new On(a,b));
        preconditions.add(new On(c,d));
        
        preconditions.add(new On(f,e));
        
        ontables.add( new OnTable(new Block("Z")) );
        ontables.add( new OnTable(new Block("Y")) );
        ontables.add( new OnTable(new Block("M")) );
        
        
        ArrayList<Integer> priorities = getOnTablePriorities(ontables, preconditions);
//        for (Predicate pi : ontables)
//        {
//            ArrayList<Predicate> tower = null;
//            
//            for (Predicate pj : preconditions)
//            {
//                if (pi.getC1().equals(pj.getC2()))
//                    tower = buildTower((On)pj, preconditions);
//            }
//            
//            if (tower != null) counts.add(tower.size());
//        }
        
        
        for (Predicate onTable : ontables)
        {
            System.out.println( ((Predicate)onTable).toString() );
        }
        for (Integer i : priorities)
        {
            System.out.println( i.toString() );
        }        
                

        priorities.add( 10 );
        priorities.add( 1 );
        priorities.add( 2 );
        
        ArrayList<Predicate> sortedOnTables = sortOnTables(ontables, priorities);
        
        BlockWorldParser parser = new BlockWorldParser("init.txt");
        BlockWorld blockWorld = new BlockWorld(parser.parse());
        blockWorld.exec();
        
    }
}
