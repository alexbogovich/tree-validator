package io.github.alexbogovich.treevalidator;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeValidator {
    public static void main(String[] args) throws IOException, ParseException {

        CommandLine parse = new DefaultParser().parse(parseOptions(), args);

        Path input = Path.of(parse.getOptionValue("i"));
        Path out = Path.of(parse.getOptionValue("o", input.getParent().toAbsolutePath() + "/out.txt"));

        if (!Files.exists(out)) {
            Files.createFile(out);
        }


        try (
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(out)));
                BufferedReader bufferedReader = Files.newBufferedReader(input)
        ) {
            bufferedReader.lines().forEach(line -> {
                String output = "ERROR";
                try {
                    output = TreeLineReader.getReversedInlineNodeTree(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    bw.write(output);
                    bw.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static Options parseOptions() {
        final Option verboseOption = Option.builder("i")
                .required()
                .longOpt("input")
                .hasArg(true)
                .desc("Input file")
                .build();
        final Option fileOption = Option.builder("o")
                .required(false)
                .longOpt("output")
                .hasArg()
                .desc("Output file")
                .build();
        final Options options = new Options();
        options.addOption(verboseOption);
        options.addOption(fileOption);
        return options;
    }
}
