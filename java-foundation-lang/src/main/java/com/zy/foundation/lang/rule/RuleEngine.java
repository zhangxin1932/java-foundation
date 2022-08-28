package com.zy.foundation.lang.rule;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

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
                String sentence = StringUtils.trimToNull(sentences[i]);
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
            String sentence = StringUtils.trimToNull(sentences[i]);
            if (StringUtils.containsIgnoreCase(sentence, name)) {
                process(ruleContext, sentences, i, name);
                if (StringUtils.isNotBlank(ruleContext.getMatchedSentence())) {
                    return;
                }
            }
        }
    }

    public static void process(RuleContext ruleContext, String[] sentences, int i, String name) {
        String sentence = StringUtils.trimToNull(sentences[i]);
        String[] sentenceArr = extractWords(sentence);
        if (Objects.isNull(sentenceArr) || sentenceArr.length == 0) {
            return;
        }
        for (int j = 0; j < sentenceArr.length; j++) {
            if (StringUtils.equalsIgnoreCase(sentenceArr[j], name)) {
                ruleContext.setMatchedSentence(sentence);
                ruleContext.setMatchedIndex(j);
                ruleContext.setMatchedLength(sentenceArr.length);
                ruleContext.setMatchedText(name);
                // 特殊逻辑：词A 前后 5 个词中没有 xxx
                String preSentence = i == 0 ? null : StringUtils.trimToNull(sentences[i - 1]);
                String nextSentence = i == sentences.length - 1 ? null : StringUtils.trimToNull(sentences[i + 1]);
                ruleContext.setPreSentence(preSentence);
                ruleContext.setNextSentence(nextSentence);

                // 1.获取 词A 之前的 n 个词
                List<String> preSentenceWords = getPreSentenceWords(sentenceArr, preSentence, j, 5);
                // 2.添加 词A 之后的 n 个词
                List<String> nextSentenceWords = getNextSentenceWords(sentenceArr, nextSentence, j, 5);
                return;
            }
        }
    }

    private static List<String> getNextSentenceWords(String[] sentenceArr, String nextSentence, int index, int n) {
        // 2.添加 词A 之后的 n 个词
        List<String> nextSentenceWords = new ArrayList<>();
        // 2.1 如果 词A 的同一句话中，后面有 5 个词，直接取出即可
        if (sentenceArr.length - 1 - index - n >= 0) {
            nextSentenceWords.addAll(Arrays.asList(sentenceArr).subList(index + 1, index + 1 + n));
        } else {
            // 如果 词A 下标是当前句子的最后一个词，则从下一句开始取
            if (sentenceArr.length - 1 == index) {

            } else {
                // 如果 词A 的下标不是当前句子的最后一个词，则先把当前句子的 词A 之后的词先取完
                nextSentenceWords.addAll(Arrays.asList(sentenceArr).subList(index + 1, sentenceArr.length));
            }
            int nextSize = n - (sentenceArr.length - 1 - index);
            // 从下一个句子中取词
            String[] nextSentenceArr = extractWords(nextSentence);
            // 如果下一个句子为空，则直接跳过
            if (nextSentenceArr == null || nextSentenceArr.length == 0) {

            } else {
                // 如果下一个句子的长度小于等于需要截取的长度，那取下一句的一整句的内容
                if (nextSentenceArr.length <= nextSize) {
                    Collections.addAll(nextSentenceWords, nextSentenceArr);
                } else {
                    // 如果下一个句子的长度小于等于需要截取的长度，那取下一句的前几个词
                    nextSentenceWords.addAll(Arrays.asList(nextSentenceArr).subList(0, nextSize));
                }
            }
        }
        return nextSentenceWords;
    }

    /**
     * 获取 词A 之前的 n 个词
     * @param sentenceArr
     * @param preSentence
     * @param index
     * @param n
     * @return
     */
    private static List<String> getPreSentenceWords(String[] sentenceArr, String preSentence, int index, int n) {
        List<String> preSentenceWords = new ArrayList<>();
        // 1.添加 词A 之前的词
        // 1.1 如果 词A 的同一句话中，前面有 5 个词，直接取出即可
        if (index - n >= 0) {
            preSentenceWords.addAll(Arrays.asList(sentenceArr).subList(index - n, index));
        } else {
            // 1.2 如果 词A 前面不足 5 个词，则先从同一句话中取出 词A 之前的所有词
            // 词A 的下标即为可以从同一句话中取出的词的个数
            if (index == 0) {
                // 词A 下标为0，则直接从 词A 的上一句话中取数
            } else {
                int k = 0;
                while (k < index) {
                    preSentenceWords.add(sentenceArr[k]);
                    k++;
                }
            }
            // 1.3 取出 词A 上一句话中需要补充的词，个数为：5 - 词A 下标
            int preSize = n - index;
            // 这里考虑到现实情况，只取 词A 之前一句话，如果 词A 的上一句话长度不足，则不再继续向上取
            String[] preSentenceArr = extractWords(preSentence);
            if (preSentenceArr == null || preSentenceArr.length == 0) {
                // 如果 词A 的上一句话为空，则停止取词
            } else {
                // 如果 词A 的上一句话不空
                // 如果上一句话的长度小于需要补充的长度，则取出上一句话所有词
                if (preSentenceArr.length <= preSize) {
                    preSentenceWords.addAll(Arrays.asList(preSentenceArr));
                } else {
                    // 如果上一句话的长度小于需要补充的长度，则取出上一句话指定数量的词
                    preSentenceWords.addAll(Arrays.asList(preSentenceArr).subList(preSentenceArr.length - preSize, preSentenceArr.length));
                }
            }
        }
        return preSentenceWords;
    }

    private static String[] extractWords(String sentence) {
        if (StringUtils.isBlank(sentence)) {
            return null;
        }
        return sentence.replace(",|\\.", "").split(" ");
    }

}
