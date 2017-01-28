package com.softwareleaf.jira.jql;

/**
 * Provides a DSL for generating JQL.
 * <p/>
 * Example Usage: the following code creates a {@code JQL} instance representing the Query below.
 * <pre> {@code
 *     project = 10190 AND fixVersion = 15648 ORDER BY priority DESC, key ASC
 * }</pre>
 * <pre>{@code
 *     JQL jql = JQL.builder()
 *              .project()
 *              .equal("10190")
 *              .and()
 *              .fixVersion()
 *              .equal("15648")
 *              .orderBy("priority")
 *              .desc()
 *              .orderBy("key")
 *              .asc()
 *              .andReturn();
 * }</pre>
 *
 * @author Jonathon Hope
 */
public class JQL {

    private StringBuilder sb;

    public JQL() {
        clear();
    }

    public StringBuilder sb() {
        return sb;
    }

    public void add(String string) {
        sb.append(string);
    }

    public void clear() {
        sb = new StringBuilder();
    }

    public String build() {
        String jql = sb.toString();

        jql = removeEnd(jql, " AND ");
        jql = removeEnd(jql, " OR ");

        return jql;
    }

    public static JQLField builder() {
        return new JQLField(new JQL());
    }

    private static String removeEnd(String string, String remove) {
        if (string.endsWith(remove)) {
            return string.substring(0, string.length() - remove.length());
        }

        return string;
    }
}