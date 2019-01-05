import Protocols.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProtocolTests {

    @Test
    public void commandsTest(){
        ClientProtocol cp = new StandardClientProtocol();
        ServerProtocol sp = new StandardServerProtocol();

        String s = cp.createMessageToServer(Protocol.ClientToServerType.MOVE,"(0,0,1)(-1,0,0)");
        sp.interpretClientMessage(s);
        assertEquals(sp.getPreparedMessage(), "(0,0,1)(-1,0,0)");
    }
}
