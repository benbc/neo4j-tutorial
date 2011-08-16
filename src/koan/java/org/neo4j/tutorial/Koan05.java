package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import java.util.HashSet;
import java.util.Set;

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

        for(Relationship relationship : universe.theDoctor().getRelationships(DoctorWhoUniverse.COMPANION_OF)) {
            Node companion = relationship.getStartNode();
            if (companion.hasRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING)) {
                Node species = companion.getSingleRelationship(DoctorWhoUniverse.IS_A, Direction.OUTGOING).getEndNode();
                if (species.getProperty("species").equals("Human")) {
                    humanCompanions.add(companion);
                }
            }
        }

        int numberOfKnownHumanCompanions = 36;
        assertEquals(numberOfKnownHumanCompanions, humanCompanions.size());
        assertThat(humanCompanions, containsOnlyHumanCompanions());
    }

    @Test
    public void shouldFindAllEpisodesWhereRoseTylerFoughtTheDaleks() {
        Index<Node> friendliesIndex = universe.getDatabase().index().forNodes("characters");
        Index<Node> speciesIndex = universe.getDatabase().index().forNodes("species");

        Node rose = friendliesIndex.get("name", "Rose Tyler").getSingle();
        Node daleks = speciesIndex.get("species", "Dalek").getSingle();

        Set<Node> episodesWithRose = new HashSet<Node>();
        for (Relationship relationship : rose.getRelationships(DoctorWhoUniverse.APPEARED_IN)) {
            episodesWithRose.add(relationship.getEndNode());
        }

        Set<Node> episodesWithDaleks = new HashSet<Node>();
        for (Relationship relationship : daleks.getRelationships(DoctorWhoUniverse.APPEARED_IN)) {
            episodesWithDaleks.add(relationship.getEndNode());
        }

        Set<Node> episodesWhereRoseFightsTheDaleks = intersection(episodesWithRose, episodesWithDaleks);

        assertThat(episodesWhereRoseFightsTheDaleks,
                containsOnlyTitles("Army of Ghosts", "The Stolen Earth", "Doomsday", "Journey's End", "Bad Wolf", "The Parting of the Ways", "Dalek"));
    }

    private <T> Set<T> intersection(Set<T> first, Set<T> second) {
        HashSet<T> temp = new HashSet<T>(first);
        temp.retainAll(second);
        return temp;
    }
}
