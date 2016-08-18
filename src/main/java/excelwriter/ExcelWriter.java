package main.java.excelwriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import main.java.datasdownloading.entities.Country;
import main.java.datasdownloading.entities.Record;
import main.java.datasdownloading.entities.RecordWithTotal;
import main.java.sort.FirstSheet;



public  class ExcelWriter {


    /**
     * @return the ExcelSheet with fields filled in
     */
    public void writeExcelDocument(String filePath,List<Record> records) {
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            
            HSSFWorkbook workbook = new HSSFWorkbook(); 
            
            
            
            
            //Create a blank sheet
            HSSFSheet sheet1 = workbook.createSheet("Sheet 1");
            
            HSSFSheet sheet2 = workbook.createSheet("Sheet 2");


            List<Country> countries = FirstSheet.getCountryList(records);

            writeFirstSheetData(sheet1,countries);
            
            writeSecondSheetData(sheet2,FirstSheet.getRecordWithDataList(countries));

            workbook.write(fos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       
    }

    
    private void writeFirstSheetData(HSSFSheet sheet, List<Country> list) {
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("merguez");
        
        
    }
    

    private void writeSecondSheetData(HSSFSheet sheet, List<RecordWithTotal> list) {
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("zhengqin");
    }


    
    
    
//    public void readStartDate() {
//        HSSFRow row = sheet.getRow();
//        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//
//        String dates = row.getCell(documentStructure.getDatesCol())
//                .getStringCellValue();
//
//        String[] s = dates.split(" ");
//        
//        try {
//			campaign.getCampaignHeader().setStartDate(f.parse(s[0].replaceAll("-", "/")));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//    }

    
    public static void main(String [] args){
        ExcelWriter excelWriter = new ExcelWriter();
        excelWriter.writeExcelDocument("zk.xls",new ArrayList<Record>());
    }
    
}
