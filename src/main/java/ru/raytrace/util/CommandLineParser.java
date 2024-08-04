package ru.raytrace.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CommandLineParser {

    private CommandLineParser() {}

    public static Properties parseOptions(String[] args) {
        Properties props = new Properties();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                props.setProperty("doAppend", "true");
            }
            else if (args[i].equals("-f")) {
                props.setProperty("isFullStatistic", "true");
            }
            else if (args[i].equals("-p")) {
                if (i + 1 < args.length) {
                    props.setProperty("prefix", args[i + 1]);
                }
                else {
                    System.out.println("Не найдено значение префикса, префикс будет упущен.");
                }
            }
            else if (args[i].equals("-o")) {
                if (i + 1 < args.length) {
                    props.setProperty("overridePath", args[i + 1]);
                }
                else {
                    System.out.println("Не найдено значение пути для результатов, используется стандартный путь.");
                }
            }
        }

        return props;
    }

    public static List<String> parsePaths(String[] args) {
        List<String> pathList = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains(".txt")) {
                pathList.add(args[i]);
            }
        }

        return validatePathList(pathList);
    }

    private static List<String> validatePathList(List<String> pathList) {
        if (!pathList.isEmpty()) {
            for (int i = 0; i < pathList.size(); i++) {
                if (Files.notExists(Paths.get(pathList.get(i)))) {
                    System.out.println("Несуществующий файл пропущен: " + pathList.get(i));
                    pathList.remove(pathList.get(i));
                }
            }
        }

        return pathList;
    }

}
