package AnilibriaProject.anilibria.service;
import AnilibriaProject.anilibria.dto.FranchiseDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/*
  Генератор Excel-файла.
  Использует Apache POI: создает .xlsx документ в памяти и возвращает его
  как byte[]. Потом UserController отдает эти байты браузеру как файл.
 */
@Component
public class ExcelGenerateServiceImpl implements ExcelGenerateService {

    // В отчете сейчас 11 колонок. Это число используется для автоширины.
    private static final int COLUMN_COUNT = 11;
    // Excel хранит ширину в 1/256 символа. 80 * 256 - верхний лимит ширины колонки.
    private static final int MAX_COLUMN_WIDTH = 80 * 256;

    @Override
    public byte[] generateFranchiseExcel(List<FranchiseDto> franchiseDtoList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Workbook - весь Excel-файл, Sheet - один лист внутри файла.
            Sheet sheet = workbook.createSheet("Franchises");
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            // Первая строка - заголовки колонок.
            headerRow.createCell(colIndex++).setCellValue("id");
            headerRow.createCell(colIndex++).setCellValue("name");
            headerRow.createCell(colIndex++).setCellValue("thumbnail");
            headerRow.createCell(colIndex++).setCellValue("rating");
            headerRow.createCell(colIndex++).setCellValue("last_year");
            headerRow.createCell(colIndex++).setCellValue("first_year");
            headerRow.createCell(colIndex++).setCellValue("name_english");
            headerRow.createCell(colIndex++).setCellValue("total_episodes");
            headerRow.createCell(colIndex++).setCellValue("total_releases");
            headerRow.createCell(colIndex++).setCellValue("total_duration");
            headerRow.createCell(colIndex++).setCellValue("total_duration_in_seconds");

            int rowNum = 1;
            for (FranchiseDto franchiseDto : franchiseDtoList) {
                // Каждая франшиза становится отдельной строкой Excel.
                Row row = sheet.createRow(rowNum++);
                colIndex = 0;
                row.createCell(colIndex++).setCellValue(franchiseDto.getId());
                row.createCell(colIndex++).setCellValue(franchiseDto.getName());
                row.createCell(colIndex++).setCellValue(franchiseDto.getThumbnail());
                row.createCell(colIndex++).setCellValue(franchiseDto.getRating());
                row.createCell(colIndex++).setCellValue(franchiseDto.getLast_year());
                row.createCell(colIndex++).setCellValue(franchiseDto.getFirst_year());
                row.createCell(colIndex++).setCellValue(franchiseDto.getName_english());
                row.createCell(colIndex++).setCellValue(franchiseDto.getTotal_episodes());
                row.createCell(colIndex++).setCellValue(franchiseDto.getTotal_releases());
                row.createCell(colIndex++).setCellValue(franchiseDto.getTotal_duration());
                row.createCell(colIndex++).setCellValue(franchiseDto.getTotal_duration_in_seconds());
            }

            // Подбираем ширину колонок под текст, но не даем длинным ссылкам разнести таблицу.
            for (int i = 0; i < COLUMN_COUNT; i++) {
                sheet.autoSizeColumn(i);
                if (sheet.getColumnWidth(i) > MAX_COLUMN_WIDTH) {
                    sheet.setColumnWidth(i, MAX_COLUMN_WIDTH);
                }
            }

            // Записываем Excel в память, а не на диск. Controller сразу отдаст эти байты браузеру.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
