package com.springleaf.cloud.discovery.condition;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.springleaf.cloud.discovery.base.AbstractFilterableRegistration;
import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;
import com.netflix.loadbalancer.Server;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/26 14:51
 */
public class ConditionFactoryTest {

    private IConditionFactory conditionFactory ;

    private List<Server> serverList;

    private FilterableRegistration filterableRegistration;
    @Before
    public void init(){
        conditionFactory = new SimpleConditionFactory();

        Server server1 = new Server("192.168.0.1",8080);
        Server server2 = new Server("192.168.0.2",8080);
        Server server3 = new Server("192.168.0.3",8080);
        serverList = new ArrayList<Server>();
        serverList.add(server1);
        serverList.add(server2);
        serverList.add(server3);

        filterableRegistration = new AbstractFilterableRegistration(null, null,null) {
            @Override
            public Map<String, String> getServerMetadata(Server server) {
                Map<String, String> map = new HashMap<>();
                map.put("host",server.getHost());
                map.put("serviceId",server.getId());
                map.put("serviceId",String.valueOf(server.getPort()));
                return map;
            }

            @Override
            public String getMetadata(String metadataKey) {
                return getMetadata().get(metadataKey);
            }

            @Override
            public int getServerWeight(Server server) {
                return 0;
            }

            @Override
            public Map<String, String> getMetadata() {
                Map<String, String> map = new HashMap<>();
                map.put("host","127.0.0.1");
                map.put("serviceId","demo-a");
                map.put("tag","");
                return map;
            }

            @Override
            public String getGroupKey() {
                return null;
            }
        };
    }


    @Test
    public void testConditionMatcher_register(){

        IMatcher matcher = conditionFactory.getMatcher(makeBaseRule("host!=10.11.* & application =^[0-9a-zA-Z-]*$"));
        boolean match = matcher.isMatch(filterableRegistration);
        Assert.assertTrue(match);

        IMatcher matcher2 = conditionFactory.getMatcher(makeBaseRule("host=129.0.* & application =^[0-9a-zA-Z-]*$"));
        boolean match2 = matcher2.isMatch(filterableRegistration);
        Assert.assertFalse(match2);

        IMatcher matcher3 = conditionFactory.getMatcher(makeBaseRule("host=127.*.1,*.0.0.1"));
        boolean match3 = matcher3.isMatch(filterableRegistration);
        Assert.assertTrue(match3);

        IMatcher matcher4 = conditionFactory.getMatcher(makeBaseRule("host=127.*.1 & host=*.0.0.1"));
        boolean match4 = matcher4.isMatch(filterableRegistration);
        Assert.assertTrue(match4);

        IMatcher matcher5 = conditionFactory.getMatcher(makeBaseRule("host=*.1,*.0.1 & host=*.0.0.1"));
        boolean match5 = matcher5.isMatch(filterableRegistration);
        Assert.assertTrue(match5);

        IMatcher matcher6 = conditionFactory.getMatcher(makeBaseRule("host!=*.2,*.0.2 & host=*.0.0.1"));
        boolean match6 = matcher6.isMatch(filterableRegistration);
        Assert.assertTrue(match6);

        IMatcher matcher7 = conditionFactory.getMatcher(makeBaseRule("tag='' & host=*.0.0.1"));
        boolean match7 = matcher7.isMatch(filterableRegistration);
        Assert.assertTrue(match7);
    }

    private BaseRule makeBaseRule(String conditions) {
        BaseRule baseRule = new BaseRule();
        baseRule.setConditions(conditions);
        return baseRule;
    }

    @Test
    public void testConditionRouter(){

        RouterRule routerRule  = RouterRule.of("demo-a", "host=127.* & application =^[0-9a-zA-Z-]*$ => host=*.0.1");
        IRouter router = conditionFactory.getRouter(routerRule);
        List<? extends Server> route = router.route(serverList, filterableRegistration);
        Assert.assertEquals(route.size(), 1);

        RouterRule routerRule2  = RouterRule.of("demo-a", "host=127.* & application =^[0-9a-zA-Z-]*$ => host!=*.0.1");
        IRouter router2 = conditionFactory.getRouter(routerRule2);
        List<? extends Server> route2 = router2.route(serverList, filterableRegistration);
        Assert.assertEquals(route2.size(), 2);
    }


}
