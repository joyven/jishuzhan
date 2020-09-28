package com.openmind;

import com.sun.tools.javac.parser.Scanner;
import com.sun.tools.javac.parser.ScannerFactory;
import com.sun.tools.javac.util.Context;

/**
 * LexicalAnalyze 词法分析
 *
 * @author zhoujunwen
 * @date 2020-09-22 09:39
 * @desc
 */
public class LexicalAnalyze {
    public static void main(String[] args) {
        ScannerFactory factory = ScannerFactory.instance(new Context());
        Scanner scanner = factory.newScanner("int i = j + x;", false);
        scanner.nextToken();
        System.out.println(scanner.token().kind);
        scanner.nextToken();
        System.out.println(scanner.token().kind);
    }
}
