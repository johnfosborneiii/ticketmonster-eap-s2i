package org.jboss.jdf.example.ticketmonster.rest.search;

import org.hibernate.search.query.facet.Facet;

/**
 * Represent a facet with its value, the number of matching results and
 * whether or not the user has selected it.
 * 
 * @author Emmanuel Bernard
 */
public class FacetView {
    private String value;
    private int count;
    private boolean selected;
    
    public FacetView(Facet facet, boolean selected) {
        this.value = facet.getValue();
        this.count = facet.getCount();
        this.selected = selected;
    }
    
    public String getValue() {
        return value;
    }
    
    public void overrideValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public boolean isSelected() {
        return selected;
    }
}
