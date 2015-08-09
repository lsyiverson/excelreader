package com.lsyiverson.excelreader;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.io.Files;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.lsyiverson.excelreader.bean.Content;
import com.lsyiverson.excelreader.bean.Report;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

public class FileReader {
    private Logger LOG = Logger.getLogger(FileReader.class);
    private static final String XLS = "xls";
    private File dir;
    private List<Report> reports;

    private final Ordering<Content> ordering = new Ordering<Content>() {
        @Override
        public int compare(Content left, Content right) {
            Number leftValue = left.getDeclareLoad();
            Number rightValue = right.getDeclareLoad();
            if (leftValue instanceof Double) {
                return Doubles.compare(leftValue.doubleValue(), rightValue.doubleValue());
            } else if (leftValue instanceof Long) {
                return Longs.compare(leftValue.longValue(), rightValue.longValue());
            }
            return 0;
        }
    };

    public FileReader(File dir) {
        this.dir = dir;
    }

    public void parseExcelFiles() {
        List<File> files = getFileList(dir);
        reports = Lists.newArrayList();
        files.forEach(file -> {
            if (XLS.equals(Files.getFileExtension(file.getName()))) {
                try {
                    ResultCalculator<Content, Report> resultCalculator
                            = new ResultCalculator(Content.class, Report.class);

                    reports.add(resultCalculator.parseFile(file, (contents) -> {
                        Report report = new Report();
                        report.setDate(Files.getNameWithoutExtension(file.getName()));
                        Content maxContent = ordering.max(contents);
                        report.setValue(maxContent.getDeclareLoad().toString());
                        return report;
                    }));
                } catch (Exception ex) {
                    LOG.warn("reade file failed in " + file.getName());
                }
            }
        });
        reports.forEach(System.out::println);
    }

    public List<Report> getReports() {
        return reports;
    }

    private List<File> getFileList(File dir) {
        return Lists.newArrayList(dir.listFiles());
    }


}
