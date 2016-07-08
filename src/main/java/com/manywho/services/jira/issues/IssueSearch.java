package com.manywho.services.jira.issues;

import com.manywho.sdk.api.ComparisonType;
import com.manywho.sdk.api.CriteriaType;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.api.run.elements.type.Property;
import com.softwareleaf.jira.jql.JQL;
import com.softwareleaf.jira.jql.JQLField;

public class IssueSearch {
    public static JQL createJqlFromListFilter(ListFilter listFilter) {
        JQLField jql = JQL.builder();

        // If any WHEREs were passed in, add the supported ones to the JQL
        if (listFilter.hasWhere()) {
            for (ListFilterWhere where : listFilter.getWhere()) {
                switch (where.getColumnName()) {
                    case "Project":
                        addProjectToJql(jql, listFilter.getComparisonType(), where);
                        break;
                    case "Labels":
                        addLabelsToJql(jql, listFilter.getComparisonType(), where);
                        break;
                    default:
                        throw new UnsupportedOperationException("Filtering on the property " + where.getColumnName() + " is not yet supported");
                }
            }
        }

        // If a search query was passed in, then add it to the JQL
        if (listFilter.hasSearch()) {
            jql.text().contains(listFilter.getSearch());
        }

        return jql.getJql();
    }

    private static void addLabelsToJql(JQLField jql, ComparisonType comparisonType, ListFilterWhere where) {
        // If no object data was given then there is nothing to add to the JQL
        if (!where.hasObjectData()) {
            return;
        }

        // Get all the names of the label objects passed in
        // TODO: The list filter should have the types populated into POJOs by the SDK, because this is super-ugly
        Object[] labels = where.getObjectData()
                .stream()
                .filter(object -> object.getProperties().stream().anyMatch(p -> p.getDeveloperName().equals("Name")))
                .map(object -> object.getProperties().stream().findFirst().orElse(new Property()))
                .map(Property::getContentValue)
                .toArray(String[]::new);

        if (where.getCriteriaType().equals(CriteriaType.Contains)) {
            if (comparisonType.equals(ComparisonType.And)) {
                jql.labels()
                        .in(labels)
                        .and();
            } else if (comparisonType.equals(ComparisonType.Or)) {
                jql.labels()
                        .in(labels)
                        .or();
            }
        } else {
            throw new UnsupportedOperationException("The criteria type " + where.getCriteriaType() + " is not currently supported for Labels");
        }
    }

    private static void addProjectToJql(JQLField jql, ComparisonType comparisonType, ListFilterWhere where) {
        // If no object data was given then there is nothing to add to the JQL
        if (!where.hasObjectData()) {
            return;
        }

        if (comparisonType.equals(ComparisonType.And)) {
            jql.project()
                    .equal(where.getObjectData().get(0).getExternalId())
                    .and();
        } else if (comparisonType.equals(ComparisonType.Or)) {
            jql.project()
                    .equal(where.getObjectData().get(0).getExternalId())
                    .or();
        }
    }
}
