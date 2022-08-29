package com.zy.foundation.lang.rule;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExcellentResult2 {

    private final List<String> excellentList = new ArrayList<>();
    private final List<String> excludeList = new ArrayList<>();

    {
        excellentList.add("not .* equal");
        excellentList.add("nice");
        excellentList.add("k");

        excludeList.add("no");
        excludeList.add("not");
    }

    public void match(RuleContext ruleContext, String[] sentence) {
        for (int i = 0; i < sentence.length; i++) {
            if (excellentList.contains(sentence[i])) {
                int min = Math.max(0, i - 5);
                int max = Math.min(i + 5, sentence.length);
                for (int j = min; j < max; j++) {
                    for (String exclude : excludeList) {
                        if (StringUtils.equalsIgnoreCase(exclude, sentence[j])) {
                            RuleContext.MatchedResult matchedResult = RuleContext.MatchedResult.builder()
                                    .index(i)
                                    .resultType(ResultType.excellent)
                                    .build();
                            ruleContext.getMatchedResults().add(matchedResult);
                            return;
                        }
                    }
                }
            }
        }
    }

}
