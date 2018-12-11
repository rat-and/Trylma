import Other.HexCell;
import Other.Piece;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class HexCellTest {

    @Test
    public void createFromString(){
        String str = "(10,0,-10)";
        HexCell<Piece> cp = new HexCell<Piece>(str);

        assertEquals(cp.toString(), "(10,0,-10)");
    }
}
