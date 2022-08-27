package com.zy.foundation.lang.rule;

import org.apache.commons.lang3.StringUtils;

public class RuleEngine {

    public static void match(RuleContext ruleContext, String[] sentences, String name) {
        ruleMatch(ruleContext, sentences);
        if (StringUtils.isNotBlank(ruleContext.getMatchedSentence())) {
            return;
        }
        nameMatch(ruleContext, sentences, name);
    }

    public static void ruleMatch(RuleContext ruleContext, String[] sentences) {
        for (Rule rule : Rule.getOrderedRules()) {
            for (int i = 0; i < sentences.length; i++) {
                String sentence = sentences[i];
                if (StringUtils.containsIgnoreCase(sentence, rule.getText())) {
                    process(ruleContext, sentences, i, rule.getText());
                    if (StringUtils.isNotBlank(ruleContext.getMatchedSentence())) {
                        return;
                    }
                }
            }
        }
    }

    public static void nameMatch(RuleContext ruleContext, String[] sentences, String name) {
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            if (StringUtils.containsIgnoreCase(sentence, name)) {
                process(ruleContext, sentences, i, name);
                if (StringUtils.isNotBlank(ruleContext.getMatchedSentence())) {
                    return;
                }
            }
        }
    }

    public static void process(RuleContext ruleContext, String[] sentences, int i, String name) {
        String sentence = sentences[i];
        String[] arr = sentence.replace(",|\\.", "").split(" ");
        for (int j = 0; j < arr.length; j++) {
            if (StringUtils.equalsIgnoreCase(arr[j], name)) {
                ruleContext.setMatchedSentence(sentence);
                ruleContext.setMatchedIndex(j);
                ruleContext.setMatchedLength(arr.length);
                ruleContext.setMatchedText(name);
                // 特殊逻辑：某个词前后 5 个词中没有 xxx
                ruleContext.setPreSentence(i == 0 ? null :sentences[i - 1]);
                ruleContext.setNextSentence(i == sentences.length - 1 ? null :sentences[i + 1]);
                return;
            }
        }
    }

}
