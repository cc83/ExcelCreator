package main.java.excelwriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.util.CellRangeAddress;

import main.java.datasdownloading.entities.Country;
import main.java.datasdownloading.entities.Record;
import main.java.datasdownloading.entities.RecordWithTotal;
import main.java.datasdownloading.entities.RecordWithTotalChainedComparator;
import main.java.sort.FirstSheet;



public  class ExcelWriter {
	
	private HSSFWorkbook workbook;
	
	private HSSFCellStyle normalStyle;
	
	private HSSFCellStyle boldStyle;

    public ExcelWriter() {
    	workbook = new HSSFWorkbook();
    	normalStyle = workbook.createCellStyle();
        normalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        normalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        boldStyle = workbook.createCellStyle();
        boldStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        boldStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFFont boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldStyle.setFont(boldFont);
	}


	/**
     * @return the ExcelSheet with fields filled in
     */
    public void writeExcelDocument(String filePath,List<Record> records) {
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            
            
            
            
            
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
        row.createCell(0).setCellValue("Country");
        row.getCell(0).setCellStyle(normalStyle);
        row.createCell(1).setCellValue("Publisher");
        row.getCell(1).setCellStyle(normalStyle);
        row.createCell(2).setCellValue("Impressions");
        row.getCell(2).setCellStyle(normalStyle);
        row.createCell(3).setCellValue("Clicks");
        row.getCell(3).setCellStyle(normalStyle);
        
        
        
        
        
        
        int i = 1;
        for (Country c : list) {
            HSSFRow ro = sheet.createRow(i++);
            int firstIndex = i;
            // print total
            ro.createCell(0).setCellValue(c.getName());
            ro.getCell(0).setCellStyle(normalStyle);
            ro.createCell(1).setCellValue(c.getTotal().getPublisherName());
            ro.getCell(1).setCellStyle(boldStyle);
            ro.createCell(2).setCellValue(c.getTotal().getImpressions());
            ro.getCell(2).setCellStyle(boldStyle);
            ro.createCell(3).setCellValue(c.getTotal().getClicks());
            ro.getCell(3).setCellStyle(boldStyle);
            for (int j = 0; j < c.getRecords().size(); j++) {
            	HSSFRow ro1 = sheet.createRow(i++);
            	ro1.createCell(1).setCellValue(c.getRecords().get(j).getPublisherName());
            	ro1.getCell(1).setCellStyle(normalStyle);
                ro1.createCell(2).setCellValue(c.getRecords().get(j).getImpressions());
                ro1.getCell(2).setCellStyle(normalStyle);
                ro1.createCell(3).setCellValue(c.getRecords().get(j).getClicks());
                ro1.getCell(3).setCellStyle(normalStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(firstIndex-1,i-1,0,0));
        }
        HSSFRow last = sheet.createRow(i++);
        HSSFCellStyle gitan = workbook.createCellStyle();
        gitan.setBorderTop(HSSFCellStyle.BORDER_THIN);
        last.createCell(0).setCellStyle(gitan);
        
    }
    

    private void writeSecondSheetData(HSSFSheet sheet, List<RecordWithTotal> list) {
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Publisher");
        row.getCell(0).setCellStyle(normalStyle);
        row.createCell(1).setCellValue("Country");
        row.getCell(1).setCellStyle(normalStyle);
        row.createCell(2).setCellValue("Impressions");
        row.getCell(2).setCellStyle(normalStyle);
        row.createCell(3).setCellValue("Clicks");
        row.getCell(3).setCellStyle(normalStyle);
        
        Collections.sort(list,RecordWithTotalChainedComparator.getComparator(RecordWithTotalChainedComparator.PUBLISHER_NAME,RecordWithTotalChainedComparator.TOTAL_DESC));
     
        int i = 1;
        for (RecordWithTotal r : list) {
            HSSFRow ro = sheet.createRow(i++);
            ro.createCell(0).setCellValue(r.getPublisherName());
            ro.getCell(0).setCellStyle(normalStyle);
            ro.createCell(1).setCellValue(r.getCountryName());
            ro.getCell(1).setCellStyle(normalStyle);
            ro.createCell(2).setCellValue(r.getImpressions());
            ro.getCell(2).setCellStyle(normalStyle);
            ro.createCell(3).setCellValue(r.getClicks());
            ro.getCell(3).setCellStyle(normalStyle);
        }
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

//    
//    public static void main(String [] args){
//        ExcelWriter excelWriter = new ExcelWriter();
//        excelWriter.writeExcelDocument("zk.xls",new ArrayList<Record>());
//    }
    
}
