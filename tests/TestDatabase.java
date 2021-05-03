import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Migrations;
import com.bradyrussell.data.UnitTypes;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
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
    private static final int PopulateOrder = 2;
    private static final int ReadOrder = 3;

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

            assertEquals(player.userid, 1234);

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

            Kingdom kingdom = new Kingdom(Player.get(session, 1234),"Testland");

            session.persist(kingdom);
            session.getTransaction().commit();
            session.close();

            assertEquals(kingdom.owner.userid,1234);
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(PopulateOrder)
    public void testCreateBuilding() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();
            session.beginTransaction();

            Building building = new Building(Kingdom.getByOwnerId(session,1234), BuildingTypes.ThroneRoom);

            session.persist(building);
            session.getTransaction().commit();
            session.close();

            assertEquals(building.kingdom.owner.userid,1234);
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(PopulateOrder)
    public void testCreateUnit() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();
            session.beginTransaction();

            Unit unit = new Unit(Kingdom.getByOwnerId(session, 1234), UnitTypes.Wizard);

            session.persist(unit);
            session.getTransaction().commit();
            session.close();

            assertEquals(unit.kingdom.owner.userid,1234);
        } catch (Exception e){
            fail(e);
        }
    }

    @Test @Order(ReadOrder)
    public void testCheckKingdom() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            Kingdom kingdom = Kingdom.getByOwnerId(session, 1234);

            assertNotNull(kingdom);
            assertNotNull(kingdom.owner);
            assertEquals(kingdom.owner.userid, 1234);

            assertTrue(kingdom.units.size() > 0);

            assertEquals(kingdom.units.get(0).type,UnitTypes.Wizard);

            assertTrue(kingdom.buildings.size() > 0);

            assertEquals(kingdom.buildings.get(0).type,BuildingTypes.ThroneRoom);

            session.close();
        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }
    }
}
