import com.bradyrussell.data.DatabaseUtil;
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

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDatabase {
    private static final int MigrateOrder = 0;
    private static final int WriteOrder = 1;
    private static final int ReadOrder = 2;

    @Test @Order(MigrateOrder)
    public void testMigrations() {
        try (Connection conn = DriverManager.getConnection(DatabaseUtil.TestDatabaseConnectionString)) {
            if (conn != null) {
                Migrations.run(conn);
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test @Order(WriteOrder)
    public void testCreatePlayer() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();
            session.beginTransaction();

            Player player = new Player(1234);

            session.persist(player);
            session.getTransaction().commit();

            session.close();
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(ReadOrder)
    public void testCheckPlayer() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            Player player = Player.get(session, 1234);

            System.out.println(player.joined.toLocaleString());

            session.close();
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(WriteOrder)
    public void testCreateKingdom() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();
            session.beginTransaction();

            Kingdom kingdom = new Kingdom(1234,"Testland");

            session.persist(kingdom);
            session.getTransaction().commit();

            session.close();
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(ReadOrder)
    public void testCheckKingdom() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            Kingdom kingdom = Kingdom.getByOwner(session, 1234);

            System.out.println(kingdom.name);

            session.close();
        } catch (Exception e){
            fail(e);
        }
    }
}
