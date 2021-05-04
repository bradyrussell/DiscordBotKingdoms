import com.bradyrussell.data.BuildingTypes;
import com.bradyrussell.data.DatabaseUtil;
import com.bradyrussell.data.Migrations;
import com.bradyrussell.data.UnitTypes;
import com.bradyrussell.data.dbobjects.Building;
import com.bradyrussell.data.dbobjects.Kingdom;
import com.bradyrussell.data.dbobjects.Player;
import com.bradyrussell.data.dbobjects.Unit;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDatabase {
    private static final int MigrateOrder = 0;
    private static final int WriteOrder = 1;
    private static final int PopulateOrder = 2;
    private static final int ReadOrder = 3;
    private static final int DeleteOrder = 4;
    private static final int AfterDeleteOrder = 5;

    @BeforeAll
    static void setup(){
        BasicConfigurator.configure();
    }

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

            assertNull(player.kingdom);

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

    @Test @Order(ReadOrder)
    public void testCheckNewPlayer() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            int userid = ThreadLocalRandom.current().nextInt();
            Player player = Player.get(session, userid);

            assertEquals(player.userid, userid);

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
    public void testCheckUnit() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            Player player = Player.get(session, 1234);

            System.out.println(player.kingdom.units.get(0).ready.toLocaleString());

            session.close();
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
            assertTrue(kingdom.getUnitsByType(session,UnitTypes.Wizard).size() > 0);
            assertTrue(kingdom.getUnitCountByType(session,UnitTypes.Wizard) > 0);
            assertEquals(kingdom.units.get(0).type,UnitTypes.Wizard);

            assertTrue(kingdom.buildings.size() > 0);
            assertTrue(kingdom.getBuildingsByType(session,BuildingTypes.ThroneRoom).size() > 0);
            assertTrue(kingdom.getBuildingCountByType(session,BuildingTypes.ThroneRoom) > 0);
            assertEquals(kingdom.buildings.get(0).type,BuildingTypes.ThroneRoom);

            assertTrue(Kingdom.getCount(session) > 0);

            session.close();
        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }
    }

    @Test @Order(DeleteOrder)
    public void testDeleteKingdom() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            Kingdom kingdom = Kingdom.getByOwnerId(session, 1234);

            session.beginTransaction();
            session.delete(kingdom);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }
    }

    @Test @Order(AfterDeleteOrder)
    public void testAfterDeleteKingdom() {
        try{
            Session session = DatabaseUtil.getTestSessionFactory().openSession();

            // no kingdoms exist
            assertEquals(0, Kingdom.getCount(session));

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<Building> root = query.from(Building.class);
            query.select(builder.count(root));

            // no buildings exist
            assertEquals(0, (long) session.createQuery(query).getSingleResult());

            CriteriaBuilder builder2 = session.getCriteriaBuilder();
            CriteriaQuery<Long> query2 = builder2.createQuery(Long.class);
            Root<Unit> root2 = query2.from(Unit.class);
            query2.select(builder2.count(root2));

            // no units exist
            assertEquals(0, (long) session.createQuery(query2).getSingleResult());

            session.close();
        } catch (Exception e){
            e.printStackTrace();
            fail(e);
        }
    }
}
