package com.lsyiverson.excelreader;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.lsyiverson.excelreader.excel.ExcelReader;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public class ResultCalculator<F, T> {
    private Class<F> inputType;
    private Class<T> outputType;

    public ResultCalculator(Class<F> inputType, Class<T> outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public T parseFile(File excelFile, Function<List<F>, T> function) {
        List<F> tableContent = readTable(excelFile);
        return function.apply(tableContent);
    }

    private List<F> readTable(File excelFile) {
        List<F> tableContent = Lists.newArrayList();
        try {
            tableContent.addAll(ExcelReader.readXLSFile(Files.asByteSource(excelFile).openStream(), inputType));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Read file failed");
        }
        return tableContent;
    }
}
