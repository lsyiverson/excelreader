package com.lsyiverson.excelreader.excel;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelReader {
    private static final Logger LOG = Logger.getLogger(ExcelReader.class);

    private ExcelReader() {
    }

    public static <T> List<T> readXLSFile(InputStream inputStream,
                                          Class<T> type) throws IOException, InstantiationException {
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);

        HSSFSheet sheet = wb.getSheetAt(0);

        Iterator rows = sheet.rowIterator();

        List<T> instances = Lists.newArrayList();

        if (rows.hasNext()) {
            rows.next();
        }
        while (rows.hasNext()) {
            instances.add(createInstance(type, (HSSFRow) rows.next()));
        }

        return instances;
    }

    private static <T> T createInstance(Class<T> type, HSSFRow row) {
        try {
            T instance = type.newInstance();
            PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();

            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if (propertyUtils.isWriteable(instance, field.getName())) {
                    ColumnNumber columnNumber = field.getAnnotation(ColumnNumber.class);
                    HSSFCell cell = row.getCell(columnNumber.value());
                    propertyUtils.setProperty(instance, field.getName(), getFieldValueByType(cell, field.getType()));
                }
            }
            return instance;
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
            LOG.warn("Failed to create instance of type: " + type.getName(), e);
            throw new RuntimeException();
        }
    }

    private static Object getFieldValueByType(HSSFCell cell, Class<?> fieldType) {
        Object value = null;
        if (fieldType.isEnum()) {
            Object[] enums = fieldType.getEnumConstants();
            for (Object e : enums) {
                if (Objects.equals(e.toString(), cell.getStringCellValue())) {
                    value = e;
                    break;
                }
            }
        } else if (fieldType.isAssignableFrom(Number.class)) {
            value = cell.getNumericCellValue();
        } else if (fieldType.isAssignableFrom(boolean.class)
            || fieldType.isAssignableFrom(Boolean.class)) {
            value = cell.getBooleanCellValue();
        } else {
            value = cell.getStringCellValue();
        }
        return value;
    }
}
