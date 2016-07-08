package com.softwareleaf.jira.jql;

import com.softwareleaf.jira.util.StringUtility;

/**
 * Represents a keyword in JQL.
 *
 * @author Jonathon Hope
 * @since 19/06/2015
 */
public class JQLKeyword {

    private JQL jql;

    public JQLKeyword(JQL aJQL) {
        jql = aJQL;
    }

    private JQLField addKeywordToField(String field) {
        jql.add(field);
        return new JQLField(jql);
    }

    private JQLOrder addKeywordToOrder(String field) {
        jql.add(field);
        return new JQLOrder(jql);
    }

    public JQLField and() {
        return addKeywordToField(" AND ");
    }

    public JQLField or() {
        return addKeywordToField(" OR ");
    }

    /**
     * Used to specify the fields by whose values the search results will be sorted.
     * <p/>
     * By default, the field's own sorting order will be used. You can override
     * this by specifying ascending order {@code "asc"} or descending order {@code "desc"}.
     */
    public JQLOrder orderBy(String... fieldNames) {
        StringUtility.verifyParams(2, fieldNames);
        return addKeywordToOrder(" ORDER BY " + StringUtility.commaSeparatedList(fieldNames));
    }

    public String build() {
        return jql.build();
    }

    public JQL andReturn() {
        return jql;
    }

}