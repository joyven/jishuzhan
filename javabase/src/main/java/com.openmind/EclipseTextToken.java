package com.openmind;

//import org.eclipse.jface.text.rules.EndOfLineRule;
//import org.eclipse.jface.text.rules.IPredicateRule;
//import org.eclipse.jface.text.rules.IToken;
//import org.eclipse.jface.text.rules.MultiLineRule;
//import org.eclipse.jface.text.rules.Token;

import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * EclipseTextToken
 *
 * @author zhoujunwen
 * @date 2020-09-01
 * @time 11:07
 * @desc
 */
public class EclipseTextToken {
//    public static void main(String[] args) {
//        Token token1 = new Token("__multiline_comment");
//        Token token2 = new Token("__singleline_comment");
//        Token token3 = new Token("__string_closed");
//        ArrayList<MultiLineRule> arrayList = new ArrayList();
//        arrayList.add(new MultiLineRule("\"", "\"", (IToken) token3, '\\'));
//        arrayList.add(new MultiLineRule("'", "'", (IToken) token3, '\\'));
//        arrayList.add(new EndOfLineRule("--", (IToken) token2));
//        arrayList.add(new MultiLineRule("/*", "*/", (IToken) token1, false, true));
//        IPredicateRule[] arrayOfIPredicateRule = new IPredicateRule[arrayList.size()];
//        arrayList.toArray(arrayOfIPredicateRule);
//        setPredicateRules(arrayOfIPredicateRule);
//    }

    public static void main(String[] args) throws BackingStoreException {
        Preferences node = Preferences.userRoot().node("/3t/mongochef/enterprise");
//        node.put("wang", "w1202302");
        String[] keys = node.keys();
        Arrays.stream(keys).forEach(k -> {
            System.out.println(k);
            System.out.println(node.get(k, ""));

        });
        node.clear();
    }
}
