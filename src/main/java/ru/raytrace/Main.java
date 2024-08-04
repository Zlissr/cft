package ru.raytrace;

import ru.raytrace.parser.Parser;
import ru.raytrace.stats.StatHelper;
import ru.raytrace.util.CommandLineParser;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties props = CommandLineParser.parseOptions(args);
        boolean isFullStatistics = Boolean.parseBoolean(props.getProperty("isFullStatistic", "false"));
        StatHelper statHelper = new StatHelper(isFullStatistics);
        List<String> pathList = CommandLineParser.parsePaths(args);
        if (!pathList.isEmpty()) {
            new Parser(props, statHelper).parse(pathList);
        }
        else {
            throw new RuntimeException("Не найдены входные файлы, перезапустите программу с верными путями");
        }

        System.out.println(statHelper);

    }
}