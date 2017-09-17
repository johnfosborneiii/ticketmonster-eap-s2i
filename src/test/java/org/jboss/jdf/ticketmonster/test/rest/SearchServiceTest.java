package org.jboss.jdf.ticketmonster.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.constraints.AssertFalse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.jdf.example.ticketmonster.rest.search.FacetGroupView;
import org.jboss.jdf.example.ticketmonster.rest.search.FacetView;
import org.jboss.jdf.example.ticketmonster.rest.search.SearchService;
import org.jboss.jdf.example.ticketmonster.rest.search.ShowResults;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SearchServiceTest {
    
    @Deployment
    public static WebArchive deployment() {
        return RESTDeployment.deployment();
    }
   
    @Inject
    private SearchService searchService;
    
    @Test
    public void testResults() {
        ShowResults results = searchService.search("decade", null, null, null, null);
        assertEquals(3, results.getResults().size());
        assertEquals("Rock concert of the decade", results.getResults().iterator().next().getEventName());
        
        // some geo tests
        results = searchService.search("decade", 0d, 0d, null, null);
        assertEquals(0, results.getResults().size());
        
        results = searchService.search("decade", 43.7252722d, -79.4236759d, null, null);
        assertEquals(1, results.getResults().size());
    }
    
    @Test
    public void testNoResults() {
        ShowResults results = searchService.search("notavalidone", null, null, null, null);
        assertEquals(0, results.getResults().size());
    }
    
    @Test
    public void testFaceting() {
        ShowResults results = searchService.search("decade", null, null, null, null);
        assertEquals(3, results.getResults().size());
        assertEquals("Rock concert of the decade", results.getResults().iterator().next().getEventName());
        for (FacetGroupView group : results.getFacetGroups()) {
            if (group.getName().equals("category")) {
                assertEquals(3, group.getFacets().size());
                Set<String> categories = new HashSet<String>();
                for(FacetView facet : group.getFacets()) {
                    categories.add(facet.getValue());
                }
                assertTrue(categories.contains("concert"));
                assertTrue(categories.contains("theatre"));
                assertTrue(categories.contains("sporting"));
            }
            else if (group.getName().equals("price")) {
                assertEquals(4, group.getFacets().size());
                assertEquals(0, group.getFacets().get(0).getCount());
                assertEquals(0, group.getFacets().get(1).getCount());
                assertEquals(2, group.getFacets().get(2).getCount());
                assertEquals(0, group.getFacets().get(3).getCount());
            }
        }
    }

    @Test
    public void testFacetFiltering() {
        ShowResults results = searchService.search("decade", null, null, 0, 0);
        assertEquals(0, results.getResults().size());
        for (FacetGroupView group : results.getFacetGroups()) {
            if (group.getId().equals("category")) {
                assertEquals(3, group.getFacets().size());
                assertTrue(group.isWithSelectedFacet());
                assertTrue(group.getFacets().get(0).isSelected());
                assertFalse(group.getFacets().get(1).isSelected());
            }
            else if (group.getId().equals("price")) {
                assertEquals(4, group.getFacets().size());
                assertTrue(group.isWithSelectedFacet());
                assertTrue(group.getFacets().get(0).isSelected());
                assertFalse(group.getFacets().get(1).isSelected());
            }
        }
    }
}