package com.zy.foundation.lang.rule;

import org.apache.commons.lang3.StringUtils;

public class RuleTest {

    public static void main(String[] args) {
        f1();
    }

    private static void f1() {
        String msg = "Our data suggest a general mechanism of response to PARPi and chemotherapy as demonstrated by various overlapping gene dependencies. The screen of the genetic status of the genes identified correlated with PARPi sensitivity may allow better stratification of patients with increased benefit to this treatment.";
        if (StringUtils.isBlank(msg)) {
            return;
        }
        String regex = "/((?:\\w+\\W*){5})\\b' . $keyword . '\\b((?:\\W*\\w+){5})/";
        String regex1 = "(?:[a-zA-Z'-]+[^a-zA-Z'-]+){0,5}dependencies(?:[^a-zA-Z'-]+[a-zA-Z'-]+){0,5}";
        String regex2 = "\"((?:[a-zA-Z'-]+[^a-zA-Z'-]+){0,5}\\W)(\" + dependencies + \")(\\W(?:[^a-zA-Z'-]+[a-zA-Z'-]+){0,5})\"";
        String[] sentences = msg.split("\\.");
        String name = "general";
        RuleContext ruleContext = new RuleContext();
        RuleEngine.match(ruleContext, sentences, name);
        System.out.println(ruleContext.getMatchedSentence());
    }

}
