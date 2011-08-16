package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlyHumanCompanions.containsOnlyHumanCompanions;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

/**
 * In this Koan we start to mix indexing and core API to perform more targeted
 * graph operations. We'll mix indexes and core graph operations to explore the
 * Doctor's universe.
 */
public class Koan05 {

    private static EmbeddedDoctorWhoUniverse universe;

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new EmbeddedDoctorWhoUniverse();
    }
    
    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();
    }

    @Test
    public void shouldCountTheNumberOfDoctorsRegenerations() {
        Index<Node> actorsIndex = universe.getDatabase().index().forNodes("actors");
        Node actor = actorsIndex.query("actor:*Hartnell").getSingle();
        int numberOfRegenerations = 0;
        while(actor.hasRelationship(DoctorWhoUniverse.REGENERATED_TO, Direction.OUTGOING)) {
            actor = actor.getSingleRelationship(DoctorWhoUniverse.REGENERATED_TO, Direction.OUTGOING).getEndNode();
            numberOfRegenerations++;
        }
        assertEquals(10, numberOfRegenerations);
    }

    @Test
    public void shouldFindHumanCompanionsUsingCoreApi() {
        HashSet<Node> humanCompanions = new HashSet<Node>();

        // YOUR CODE GOES HERE

        int numberOfKnownHumanCompanions = 36;
        assertEquals(numberOfKnownHumanCompanions, humanCompanions.size());
        assertThat(humanCompanions, containsOnlyHumanCompanions());
    }

    @Test
    public void shouldFindAllEpisodesWhereRoseTylerFoughtTheDaleks() {
        Index<Node> friendliesIndex = universe.getDatabase().index().forNodes("characters");
        Index<Node> speciesIndex = universe.getDatabase().index().forNodes("species");
        HashSet<Node> episodesWhereRoseFightsTheDaleks = new HashSet<Node>();

        // YOUR CODE GOES HERE

        assertThat(episodesWhereRoseFightsTheDaleks,
                containsOnlyTitles("Army of Ghosts", "The Stolen Earth", "Doomsday", "Journey's End", "Bad Wolf", "The Parting of the Ways", "Dalek"));
    }
}
