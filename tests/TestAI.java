import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.GoldUtil;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.game.AIUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAI {
    @Test @Order(1)
    public void testCreateAI() {
        Session session = DatabaseUtil.getTestSessionFactory().openSession();

        AIUtil.createAIKingdom(session, 100);

        session.close();
    }

    @Test @Order(2)
    public void testCheckCreatedAI() {
        Session session = DatabaseUtil.getTestSessionFactory().openSession();

        List<Player> ais = Player.getAIs(session);

        for (Player ai : ais) {
            System.out.println(ai.kingdom.name);
        }

        session.close();
    }

}
