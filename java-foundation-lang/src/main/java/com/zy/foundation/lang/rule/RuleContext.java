package com.zy.foundation.lang.rule;

import lombok.Data;

@Data
public class RuleContext {
    private String matchedText;
    private String matchedSentence;
    private int matchedIndex;
    private int matchedLength;
    private String preSentence;
    private String nextSentence;
}
