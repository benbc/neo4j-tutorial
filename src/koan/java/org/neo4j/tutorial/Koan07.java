package org.neo4j.tutorial;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.Traverser;

import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificActors.containsOnlyActors;
import static org.neo4j.tutorial.matchers.ContainsSpecificNumberOfNodes.containsNumberOfNodes;

/**
 * In this Koan we start using the new traversal framework to find interesting
 * information from the graph about the Doctor's past life.
 */
public class Koan07 {

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
	public void shouldDiscoverHowManyIncarnationsOfTheDoctorThereHaveBeen() throws Exception {
		Node theDoctor = universe.theDoctor();
		Traverser traverser = null;

        // YOUR CODE GOES HERE

		assertThat(traverser.nodes(), containsNumberOfNodes(11));
	}

	@Test
	public void shouldFindTheFirstDoctor() {
		Node theDoctor = universe.theDoctor();
		Traverser traverser = null;

        // YOUR CODE GOES HERE

		assertThat(traverser.nodes(), containsOnlyActors("William Hartnell"));
	}
}
