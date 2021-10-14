package com.springleaf.cloud.discovery.base;

/**
 *
 * see {@link FilterableRegistration}
 *
 * @author leon
 */
public interface Groupable {

    String getGroupKey();

    String getGroup();

    String getGroup(String groupKey);

}
