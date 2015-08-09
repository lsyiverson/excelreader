package com.lsyiverson.excelreader;

import com.lsyiverson.excelreader.bean.Report;
import com.lsyiverson.excelreader.excel.ExcelBuilder;
import com.lsyiverson.excelreader.util.SeparatorUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {
    private List<Report> reportTable;

    public ReportGenerator(List<Report> reportTable) {
        this.reportTable = reportTable;
    }

    public void writeFile(File dir, String filename) throws IOException {
        File file = new File(dir.getPath() + SeparatorUtils.getFileSeparator() + filename);

        ExcelBuilder<Report> excelBuilder = ExcelBuilder.prepareExcel(Report.class).sheetName("report");
        excelBuilder.addColumn("时间").generatedBy(Report::getDate);
        excelBuilder.addColumn("最大负荷预测值").generatedBy(Report::getValue);

        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(excelBuilder.build(reportTable));
        fos.close();
        System.out.println(file.getPath());
    }
}
