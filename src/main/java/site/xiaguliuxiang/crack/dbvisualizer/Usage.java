package site.xiaguliuxiang.crack.dbvisualizer;

import org.apache.commons.cli.*;
import site.xiaguliuxiang.crack.dbvisualizer.keygen.KeyGen;

/**
 * @author xiaguliuxiang@gmail.com
 * @date 2019-09-09 20:00:00
 */
public class Usage {
    private static final Options OPTIONS = new Options();
    private static final String DEFAULT_LICENSE_ID = "侠骨留香/Www.ChinaPYG.CoM";
    private static final String DEFAULT_LICENSE_NAME = "侠骨留香";
    private static final String DEFAULT_LICENSE_ORG = "ChinaPYG";

    public static void main(String[] args) {
        String usage = "\n====================================================\n"
                + "=======     DbVisualizer Pro Crack Agent     =======\n"
                + "=======   https://github.com/xiaguliuxiang   =======\n"
                + "=======         QQ Group: 532944625          =======\n"
                + "====================================================\n\n";

        System.out.print(usage);
        System.out.flush();

        OPTIONS.addOption("i", "id", true, "License id[default: 侠骨留香/Www.ChinaPYG.CoM]");
        OPTIONS.addOption("n", "name", true, "License name[default: 侠骨留香]");
        OPTIONS.addOption("o", "org", true, "License organization[default: ChinaPYG]");
        OPTIONS.addOption("h", "help", false, "Print help message");

        try {
            CommandLine command = new DefaultParser().parse(OPTIONS, args);
            runCommand(command);
        } catch (ParseException e) {
            printUsage();
        }
    }

    private static void printUsage() {
        String selfPath = Usage.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        System.out.print("KeyGen ");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar " + selfPath, OPTIONS, true);

        System.out.println("\n================================================================================\n");
        System.out.println("# Crack Agent usage: append -javaagent arg to dbvis.vmoptions.");
        System.out.println("# Example(append -javaagent:dbvisualizer-agent.jar to dbvis.vmoptions file): \n");
        System.out.println("  Windows: -javaagent:C:\\Users\\xiaguliuxiang\\dbvisualizer-agent.jar");
        System.out.println("    dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions");
        System.out.println("  Mac OS: -javaagent:/Users/xiaguliuxiang/dbvisualizer-agent.jar");
        System.out.println("    dbvis.vmoptions: ${DbVisualizer_HOME}/Contents/vmoptions.txt");
        System.out.println("  Linux: -javaagent:/home/xiaguliuxiang/dbvisualizer-agent.jar");
        System.out.println("    dbvis.vmoptions: ${DbVisualizer_HOME}/dbvis.vmoptions");
        System.out.println("\n# Then start your DbVisualizer and install license key.\n");

        System.exit(1);
    }

    private static void runCommand(CommandLine commandLine) {
        if (commandLine.hasOption("h")) {
            printUsage();
            return;
        }

        String licenseId = commandLine.hasOption("i") ? commandLine.getOptionValue("i") : DEFAULT_LICENSE_ID;
        String licenseName = commandLine.hasOption("n") ? commandLine.getOptionValue("n") : DEFAULT_LICENSE_NAME;
        String licenseOrg = commandLine.hasOption("o") ? commandLine.getOptionValue("o") : DEFAULT_LICENSE_ORG;

        try {
            String license = KeyGen.generateLicense(licenseId, licenseName, licenseOrg);

            System.out.println("Your license key(Don't copy this line!!!): \n");
            System.out.println(license);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.flush();
    }
}
