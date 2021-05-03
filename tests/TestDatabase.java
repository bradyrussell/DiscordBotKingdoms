import com.bradyrussell.Main;
import com.bradyrussell.data.Migrations;
import com.bradyrussell.data.objects.Kingdom;
import com.bradyrussell.data.objects.Player;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatabase {
    @Test
    public void testMigrations() {
        try (Connection conn = DriverManager.getConnection(Main.TestDatabaseConnectionString)) {
            if (conn != null) {
                Migrations.run(conn);
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPlayer() {
        try (Connection conn = DriverManager.getConnection(Main.TestDatabaseConnectionString)) {
            if (conn != null) {

                Player player = new Player();
                player.userid = 123;
                assertTrue(player.create(conn));
                assertTrue(player.refresh(conn));

                Player player1 = Player.get(conn, 123);
                assertNotNull(player1);
                assertEquals(player.joined, player1.joined);

            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testKingdom() {
        try (Connection conn = DriverManager.getConnection(Main.TestDatabaseConnectionString)) {
            if (conn != null) {
                System.out.println(conn.getMetaData().getDriverName());

                Kingdom kingdom = new Kingdom();
                kingdom.name = "My Test Kingdom";
                kingdom.owner = 123;
                assertTrue(kingdom.create(conn));
                assertTrue(kingdom.refresh(conn));

                Kingdom kingdom1 = Kingdom.get(conn, kingdom.id);
                assertNotNull(kingdom1);
                assertEquals(kingdom.created, kingdom1.created);

            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}
