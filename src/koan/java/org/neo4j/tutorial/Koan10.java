package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import java.util.HashSet;

import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificActors.containsOnlyActors;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificSpecies.containsOnlySpecies;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

/**
 * In this Koan we use the graph-matching library to look for patterns in the
 * Doctor's universe.
 */
public class Koan10 {

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
    public void shouldFindEpisodesWhereTheDoctorFoughtTheCybermen() {
        HashSet<Node> cybermenEpisodes = new HashSet<Node>();

        // YOUR CODE GOES HERE

        assertThat(cybermenEpisodes, containsOnlyTitles(knownCybermenTitles()));
    }

    private String[] knownCybermenTitles() {
        return new String[] { "The Moonbase", "The Tomb of the Cybermen", "The Wheel in Space", "Revenge of the Cybermen", "Earthshock", "Silver Nemesis",
                "Rise of the Cybermen", "The Age of Steel", "Army of Ghosts", "Doomsday", "The Next Doctor", "The Pandorica Opens", "A Good Man Goes to War" };
    }
    
    @Test
    public void shouldFindDoctorsThatBattledTheCybermen() {
        HashSet<Node> cybermenEpisodes = new HashSet<Node>();

        // YOUR CODE GOES HERE

        assertThat(cybermenEpisodes, containsOnlyActors("David Tennant", "Matt Smith", "Patrick Troughton", "Tom Baker", "Peter Davison", "Sylvester McCoy"));
    }

    @Test
    public void shouldFindEnemySpeciesThatRoseTylerAndTheNinthDoctorEncountered() {
        HashSet<Node> enemySpeciesRoseAndTheNinthDoctorEncountered = new HashSet<Node>();

        // YOUR CODE GOES HERE

        assertThat(enemySpeciesRoseAndTheNinthDoctorEncountered, containsOnlySpecies("Dalek", "Slitheen", "Auton"));
    }
}
