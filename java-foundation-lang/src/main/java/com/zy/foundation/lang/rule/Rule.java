package com.zy.foundation.lang.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum Rule {
    one("mechanism", 1),
    two("chemotherapy", 2),

    ;
    private final String text;
    private final int order;

    private static final List<Rule> rules = Arrays.stream(Rule.values()).sorted(Comparator.comparing(Rule::getOrder)).collect(Collectors.toList());

    public static List<Rule> getOrderedRules() {
        return rules;
    }
}
