package com.lsyiverson.excelreader;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Launcher {
    private static Logger LOG = Logger.getLogger(Launcher.class);

    public static void main(String[] args) {
        String dirPath;
        if (args.length == 0) {
            LOG.info("do not specified work path, use current path");
            dirPath = ".";
        } else {
            dirPath = args[0];
        }


        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("path is not existed or is not a directory");
        }

        LOG.info("Start read files");
        FileReader fileReader = new FileReader(dir);
        fileReader.parseExcelFiles();
        LOG.info("Read files completed");

        LOG.info("Start generate report");
        ReportGenerator reportGenerator = new ReportGenerator(fileReader.getReports());
        try {
            reportGenerator.writeFile(dir, "report.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Generate report completed");
    }
}
