package com.springleaf.cloud.discovery.filter;

import org.springframework.core.Ordered;

/**
 *
 * Filter base class
 *
 * see {@link ConditionSelector}
 * see {@link ConditionPredicate}
 * see {@link ConditionFilter}
 *
 * @author leon
 */
public interface Filter extends Ordered {

    @Override
    default int getOrder(){
        return 0;
    }

}
