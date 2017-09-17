/**
 * Module for the query results model
 */
define([ 
    'configuration',
    'backbone'
], function (config) {
    /**
     * The Results model class definition
     */
    var Results = Backbone.Model.extend({
        urlRoot: config.baseUrl + 'rest/search', // the URL for performing CRUD operations
        initialize  : function() {
            _.bindAll(this,"fetch");
            _.bindAll(this,"appUrl");
        },

        // the URL with params to reach the REST endpoint
        url: function() {
            params = '?query=' + encodeURIComponent(this.get("query"));
            if (typeof this.get("lat") != 'undefined' && typeof this.get("lng") != 'undefined') {
                params = params + '&latitude=' + encodeURIComponent(this.get("lat")) + '&longitude=' + encodeURIComponent(this.get("lng"));
            }
            if (typeof this.get("category") != 'undefined') {
                params = params + '&categoryfacet=' + this.get("category");
            }
            if (typeof this.get("price") != 'undefined') {
                params = params + '&minpricefacet=' + this.get("price");
            }
            console.log("Calling REST endpoint " + this.urlRoot + params);
            return this.urlRoot + params;
        },

        // the application URL as exposed by the application for bookmarkability
        appUrl: function() {
            result = "search/";
            var query = this.get("query");
            var lat = this.get("lat");
            var lng = this.get("lng");
            var category = this.get("category");
            var minprice = this.get("price");
            if (typeof lat != 'undefined' && typeof lng != 'undefined') {
                result += "around/" + lat + "/" + lng + "/";
            }
            else {
                result += "anywhere/";
            }
            result += encodeURIComponent(query);
            result += "/category/"
            if (typeof category != 'undefined') {
                result += category;
            }
            else {
                result += 'all';
            }
            result += "/minprice/"
            if (typeof minprice != 'undefined') {
                result += minprice;
            }
            else {
                result += 'all';
            }
            return result;
        }
    });

    // export the Results class
    return Results;
});