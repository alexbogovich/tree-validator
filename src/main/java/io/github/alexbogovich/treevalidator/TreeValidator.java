package io.github.alexbogovich.treevalidator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class TreeValidator {
    public static void main(String[] args) throws IOException, ParseException {

        CommandLine parse = new DefaultParser().parse(parseOptions(), args);

        Path input = Path.of(parse.getOptionValue("i"));
        Path out = Path.of(parse.getOptionValue("o", input.getParent().toAbsolutePath() + "/out.txt"));

        if (!Files.exists(out)) {
            Files.createFile(out);
        }

        try (
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(out)));
                BufferedReader reader = Files.newBufferedReader(input)
        ) {
            reader.lines().forEach(line -> {
                String output = TreeLineReader.getReversedInlineNodeTree(line);

                try {
                    writer.write(output);
                    writer.newLine();
                } catch (Exception e) {
                    log.error("Unable write " + line + "to output file. Exception:" + e.getMessage());
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
