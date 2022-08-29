package com.zy.foundation.lang.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
public class RuleContext {
    private String matchedText;
    private String matchedSentence;
    private int matchedIndex;
    private int matchedLength;
    private String preSentence;
    private String nextSentence;
    private Set<MatchedResult> matchedResults = new HashSet<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MatchedResult {
        private int index;
        private ResultType resultType;
    }
}
