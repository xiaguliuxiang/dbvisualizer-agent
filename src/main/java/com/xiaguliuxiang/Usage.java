package com.xiaguliuxiang;

import com.xiaguliuxiang.keygen.KeyGen;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.apache.commons.cli.help.TextHelpAppendable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Usage {

    private static final Options OPTIONS = new Options();

    static {
        OPTIONS.addOption("h", "help", false, "Show the help message and exit");
        OPTIONS.addOption("i", "id", true, "Custom License Id [default: random string]");
        OPTIONS.addOption("n", "name", true, "Custom License Name [default: random string]");
        OPTIONS.addOption("o", "org", true, "Custom License Organization [default: random string]");
    }

    public static void main(String[] args) {
        try {
            runCommand(new DefaultParser().parse(OPTIONS, args));
        } catch (ParseException e) {
            printHelp();
        }
    }


    private static void runCommand(CommandLine commandLine) {
        if (commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
            printHelp();
            return;
        }

        String licenseId = commandLine.hasOption("i") ? commandLine.getOptionValue("i") : KeyGen.randomString(8);
        String licenseName = commandLine.hasOption("n") ? commandLine.getOptionValue("n") : KeyGen.randomString(4);
        String licenseOrg = commandLine.hasOption("o") ? commandLine.getOptionValue("o") : KeyGen.randomString(4);

        String selfPath = Usage.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String fileName = new File(selfPath).getParent() + File.separator + "dbvis.license";
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            String license = KeyGen.generateLicense(licenseId, licenseName, licenseOrg);
            writer.write(license);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Your license file: " + fileName);
        System.out.println();
    }

    private static void printHelp() {
        String selfPath = Usage.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String header = String.join(System.lineSeparator(), "",
                "==================================================================",
                "=======                 DbVisualizer Agent                 =======",
                "=======          https://github.com/xiaguliuxiang          =======",
                "=================================================================="
        );

        String footer = String.join(System.lineSeparator(), "",
                "================================================================================",
                "# DbVisualizer Agent usage: append -javaagent arg to dbvis.vmoptions.",
                "# Example(append -javaagent:dbvisualizer-agent.jar to dbvis.vmoptions file): ",
                "#  Windows: -javaagent:C:\\Users\\xiaguliuxiang\\dbvisualizer-agent.jar",
                "#    dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions",
                "#  Mac OS: -javaagent:/Users/xiaguliuxiang/dbvisualizer-agent.jar",
                "#    dbvis.vmoptions: ${DbVisualizer_HOME}/Contents/vmoptions.txt",
                "#  Linux: -javaagent:/home/xiaguliuxiang/dbvisualizer-agent.jar",
                "#    dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions",
                "# Then start your DbVisualizer and install license key."
        );

        TextHelpAppendable helpAppendable = new TextHelpAppendable(System.out);
        helpAppendable.getTextStyleBuilder().setMaxWidth(120).setLeftPad(2).setIndent(0);

        HelpFormatter formatter = HelpFormatter.builder().setShowSince(false).setHelpAppendable(helpAppendable).get();

        try {
            formatter.printHelp("java -jar " + selfPath, header, OPTIONS, footer, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
