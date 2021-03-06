package com.bytehonor.sdk.server.bytehonor.query;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytehonor.sdk.server.bytehonor.jdbc.SqlInjectUtils;

public class QueryConditionTest {

    private static final Logger LOG = LoggerFactory.getLogger(QueryConditionTest.class);

    @Test
    public void test() {
        QueryCondition condition = QueryCondition.create();
        condition.eq("eq", "111").neq("neq", 222).lt("lt", 333).elt("elt", 4444).gt("gt", 5).eq("name", "john")
                .like("like", "%xxx%").in("ini", Lists.list("a1", "a2")).orderBy(QueryOrder.descOf("xxx"));

        condition.setOffset(10);
//        condition.setOrder(QueryOrder.descOf("id"));

        LOG.info("offset:{}", condition.offsetLimitSql());
        LOG.info("order:{}", condition.getOrder().toSql());
        LOG.info("conditon:{}", condition.getMatchHolder().toAndSql());
        List<Object> args = condition.getMatchHolder().getArgs();
        List<Integer> types = condition.getMatchHolder().getArgTypes();
        int[] typesArr = SqlInjectUtils.listArray(types);
        LOG.info("args:{}, types:{}", args, typesArr);
        assertTrue(args.size() == 7, "test");
    }

}
