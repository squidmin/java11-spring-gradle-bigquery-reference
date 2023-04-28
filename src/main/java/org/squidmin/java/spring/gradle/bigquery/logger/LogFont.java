package org.squidmin.java.spring.gradle.bigquery.logger;

abstract class LogFont {

    protected static final String GREEN = "\u001b[32m";

    protected static final String CYAN = "\u001b[36m";

    protected static final String BOLD = "\u001b[1m";

    protected static final String RESET = "\u001b[0m";

    protected static String boldGreen(String arg) {
        return BOLD + GREEN + arg + RESET;
    }

    protected static String boldCyan(String arg) { return BOLD + CYAN + arg + RESET; }

    protected static String bold(String arg) { return BOLD + arg + RESET; }

}
