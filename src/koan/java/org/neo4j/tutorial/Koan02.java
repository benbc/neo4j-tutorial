package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.*;

import static org.junit.Assert.*;

/**
 * This first programming Koan will get you started with the basics of managing nodes and relationships with the core API.
 * It will also introduce you to the earliest Doctor Who storylines! 
 */
public class Koan02 {

    private static GraphDatabaseService db;
    private static DatabaseHelper databaseHelper;

    @BeforeClass
    public static void createADatabase() {
        db = DatabaseHelper.createDatabase();
        databaseHelper = new DatabaseHelper(db);
    }
    
    @AfterClass
    public static void closeTheDatabase() {
        db.shutdown();
    }
    
    @Test
    public void shouldCreateANodeInTheDatabase() {
        Transaction transaction = db.beginTx();
        Node node = db.createNode();
        transaction.success();

        assertTrue(databaseHelper.nodeExistsInDatabase(node));
    }

    @Test
    public void shouldCreateSomePropertiesOnANode() {
        Transaction transaction = db.beginTx();
        Node theDoctor = db.createNode();
        theDoctor.setProperty("firstname", "William");
        theDoctor.setProperty("lastname", "Hartnell");
        transaction.success();

        assertTrue(databaseHelper.nodeExistsInDatabase(theDoctor));

        Node storedNode = db.getNodeById(theDoctor.getId());
        assertEquals("William", storedNode.getProperty("firstname"));
        assertEquals("Hartnell", storedNode.getProperty("lastname"));
    }

    @Test
    public void shouldRelateTwoNodes() {
        Transaction transaction = db.beginTx();
        Node theDoctor = db.createNode();
        Node susan = db.createNode();
        Relationship companionRelationship = susan.createRelationshipTo(theDoctor, new RelationshipType() {
            @Override
            public String name() {
                return "Companion";
            }
        });
        transaction.success();

        Relationship storedCompanionRelationship = db.getRelationshipById(companionRelationship.getId());
        assertNotNull(storedCompanionRelationship);
        assertEquals(susan, storedCompanionRelationship.getStartNode());
        assertEquals(theDoctor, storedCompanionRelationship.getEndNode());
    }
    
    @Test
    public void shouldRemoveStarTrekInformation() {
        /* Captain Kirk has no business being in our database, so set phasers to kill */
        Node captainKirk = createPollutedDatabaseContainingStarTrekReferences();

        Transaction transaction = db.beginTx();
        for (Relationship relationship : captainKirk.getRelationships()) {
            relationship.delete();
        }
        captainKirk.delete();
        transaction.success();
        transaction.finish();

        try {
            db.getNodeById(captainKirk.getId());
            fail();
        } catch(NotFoundException nfe) {
            // If the exception is thrown, we've removed Captain Kirk from the database
            assertNotNull(nfe);
        }
    }
    
    
    @Test
    public void shouldRemoveIncorrectEnemyOfRelationshipBetweenSusanAndTheDoctor() {
        Node susan = createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor();

        Transaction transaction = db.beginTx();
        Relationship enemy_of = susan.getSingleRelationship(DynamicRelationshipType.withName("ENEMY_OF"), Direction.OUTGOING);
        enemy_of.delete();
        transaction.success();
        transaction.finish();

        assertEquals(1, databaseHelper.countRelationships(susan.getRelationships()));
    }

    private Node createInaccurateDatabaseWhereSusanIsEnemyOfTheDoctor() {
        Transaction tx = db.beginTx();
        Node susan;
        try {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("name", "The Doctor");

            susan = db.createNode();
            susan.setProperty("firstname", "Susan");
            susan.setProperty("lastname", "Campbell");

            susan.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));
            susan.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("ENEMY_OF"));

            tx.success();
            return susan;
        } finally {
            tx.finish();
        }
        
    }

    private Node createPollutedDatabaseContainingStarTrekReferences() {
        Transaction tx = db.beginTx();
        Node captainKirk;
        try {
            Node theDoctor = db.createNode();
            theDoctor.setProperty("name", "The Doctor");

            captainKirk = db.createNode();
            captainKirk.setProperty("firstname", "James");
            captainKirk.setProperty("initial", "T");
            captainKirk.setProperty("lastname", "Kirk");

            captainKirk.createRelationshipTo(theDoctor, DynamicRelationshipType.withName("COMPANION_OF"));

            tx.success();
            return captainKirk;
        } finally {
            tx.finish();
        }
    }
}
