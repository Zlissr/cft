package ru.raytrace.parser;

import ru.raytrace.MultiIterator;
import ru.raytrace.stats.StatHelper;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Parser {

    private final boolean doAppend;
    private final String integersPath;
    private final String floatsPath;
    private final String stringsPath;
    private final StatHelper statHelper;

    public Parser(Properties properties, StatHelper statHelper) {
        this.statHelper = statHelper;
        String prefix = properties.getProperty("prefix", "");
        doAppend = Boolean.parseBoolean(properties.getProperty("doAppend", "false"));
        String overridePath = properties.getProperty("overridePath", "");
        overridePath = !overridePath.isEmpty() ? overridePath + "/" : overridePath;
        integersPath = overridePath + prefix + "integers.txt";
        floatsPath = overridePath + prefix + "floats.txt";
        stringsPath = overridePath + prefix + "strings.txt";
    }

    public void parse(List<String> pathList) {
        MultiIterator<String> iterator = collectData(pathList);
        boolean rewritedFloat = false;
        boolean rewritedString = false;
        boolean rewritedInteger = false;

        try {
            while (iterator.hasNext()) {
                String line = iterator.next();

                try {
                    Integer.parseInt(line);

                    if (!doAppend && !rewritedInteger) {
                        doWrite(line, integersPath, Type.INT);
                        rewritedInteger = true;

                        continue;
                    }

                    doAppend(line, integersPath, Type.INT);
                    continue;
                } catch (NumberFormatException ignored) {}

                try {
                    Float.parseFloat(line);

                    if (!doAppend && !rewritedFloat) {
                        doWrite(line, floatsPath, Type.FLOAT);
                        rewritedFloat = true;

                        continue;
                    }

                    doAppend(line, floatsPath, Type.FLOAT);

                    continue;
                } catch (NumberFormatException ignored) {}

                if (!doAppend && !rewritedString) {
                    doWrite(line, stringsPath, Type.STRING);
                    rewritedString = true;

                    continue;
                }

                doAppend(line, stringsPath, Type.STRING);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void tryCreateParentDirectories(String path) {
        try {
            Files.createDirectories(Paths.get(path).getParent());
        } catch (Exception ignored) {}
    }

    private MultiIterator<String> collectData(List<String> pathList) {
        List<Iterator<String>> iterators = new ArrayList<>();

        pathList.forEach(path -> {
            try {
                iterators.add(Files.readAllLines(Paths.get(path)).iterator());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return new MultiIterator<>(iterators);
    }

    private void doAppend(String line, String path, Type type) throws IOException {
        tryCreateParentDirectories(path);
        Files.write(
                Paths.get(path),
                (line + "\n").getBytes(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE);
        statHelper.log(line, type);
    }

    private void doWrite(String line, String path, Type type) throws IOException {
        tryCreateParentDirectories(path);
        Files.write(Paths.get(path), (line + "\n").getBytes());
        statHelper.log(line, type);
    }

    public enum Type {
        INT,
        FLOAT,
        STRING
    }

}
