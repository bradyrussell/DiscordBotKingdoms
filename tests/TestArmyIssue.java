import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Emojis;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.ResourceTypes;
import com.bradyrussell.data.dbobjects.Army;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import net.dv8tion.jda.api.EmbedBuilder;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestArmyIssue {
    @Test
    public void testIssue() {
        Session session = DatabaseUtil.getProductionSessionFactory().openSession();

        try {
            Player player = Player.get(session, 374003555478142997L);

            assertNotNull(player);
            assertNotNull(player.kingdom);

            session.refresh(player.kingdom);

            assertNotNull(player.kingdom.units);

            List<Unit> units = player.kingdom.units;
            for (Unit unit : units) {
                assertNotNull(unit);
                System.out.println(unit.type.DisplayName);
            }

            System.out.println("Armies: "+player.kingdom.armies.size());
            for (Army army : player.kingdom.armies) {
                assertNotNull(army);
                assertNotNull(army.name);
                System.out.println(army.name);
            }

            for (Unit unit : units) {
                assertNotNull(unit);
                System.out.println(unit.type.DisplayName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
    }

}
