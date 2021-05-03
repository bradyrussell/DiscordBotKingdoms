import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.Migrations;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import org.hibernate.Session;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestMoney {
    @Test
    public void testMoney() {
        long CopperTotal = Long.MAX_VALUE;

        System.out.println(GoldUtil.getGoldSilverCopper(CopperTotal));

        assertEquals(GoldUtil.getGold(CopperTotal), 922337203685477L);
        assertEquals(GoldUtil.getSilverPart(CopperTotal), 58);
        assertEquals(GoldUtil.getCopperPart(CopperTotal), 7);
        assertEquals(GoldUtil.getGoldSilverCopper(CopperTotal), "922337203685477 gold, 58 silver, 7 copper");

        System.out.println(GoldUtil.getGoldSilverCopper(500));

        assertEquals(GoldUtil.getGold(5000), 0);
        assertEquals(GoldUtil.getSilverPart(5000), 50);
        assertEquals(GoldUtil.getCopperPart(5000), 0);
        assertEquals(GoldUtil.getGoldSilverCopper(5000), "50 silver");

        System.out.println(GoldUtil.getGoldSilverCopper(250025));
        assertEquals(GoldUtil.getGoldSilverCopper(250025), "25 gold, 25 copper");

        System.out.println(GoldUtil.getGoldSilverCopper(252500));
        assertEquals(GoldUtil.getGoldSilverCopper(252500), "25 gold, 25 silver");

        System.out.println(GoldUtil.getGoldSilverCopper(0));
        assertEquals(GoldUtil.getGoldSilverCopper(0), "0 copper");
    }

}
