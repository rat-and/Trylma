import Logic.*;

import Logic.Point;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BoardTests {

    @Test
    public void createFromString(){
        String str = "(10,0,-10)";
        HexCell<Piece> cp = new HexCell<Piece>(str);

        assertEquals(cp.toString(), "(10,0,-10)");
    }

    @Test
    public void testLattice(){

        Latticing<Piece> latt = new Latticing<>();
        latt.insert(new HexCell<Piece>(null,1,0,-1));

        assertTrue(latt.containsNode(1,0,-1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWrongArgument() throws  IllegalArgumentException
    {
        Latticing<Piece> latt = new Latticing<>();
        latt.insert(new HexCell<Piece>(null,-1,0,-1));
    }



}
