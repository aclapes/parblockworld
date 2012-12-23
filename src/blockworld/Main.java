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
    public static void main(String[] args) throws IOException, CloneNotSupportedException 
    {
        BlockWorldParser parser = new BlockWorldParser("init.txt");
        BlockWorld blockWorld = new BlockWorld(parser.parse());
        blockWorld.exec();
    }
}
