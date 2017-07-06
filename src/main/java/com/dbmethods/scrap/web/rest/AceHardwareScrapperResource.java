package com.dbmethods.scrap.web.rest;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.annotation.Timed;
import com.dbmethods.scrap.web.rest.vm.Store;
import com.dbmethods.scrap.web.rest.vm.Store.StoreDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/aceHardware")
public class AceHardwareScrapperResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final String OUTPUT_FILE_PATH = "/home/hai/Work/projects/scrapGeofence/StoreInfo.xlsx";
    
    private double leftPos = -126.835935;
    private double rightPos = -64.984297;
    private double topPos = 50.929610;
    private double bottomPos = 23.743778;
    private double radius = 85.0;   //   = 120 / sqrt(2)
    
    private double horizonDensity = 120.0;
    private double verticalDensity = 60.0;
    
    //alaska
//    private double leftPos = -168.744602;
//    private double rightPos = -129.018043;
//    private double topPos = 71.471811;
//    private double bottomPos = 53.019870;
//    private double radius = 85.0;   //   = 120 / sqrt(2)
//    
//    private double horizonDensity = 31.0;
//    private double verticalDensity = 79.0;
    
    
    
    //hawaii
//    private double leftPos = -160.350462;
//    private double rightPos = -154.742946;
//    private double topPos = 22.259077;
//    private double bottomPos = 18.860004;
//    private double radius = 85.0;   //   = 120 / sqrt(2)
//    
//    private double horizonDensity = 18.0;
//    private double verticalDensity = 5.0;
//    
    private Set<String> addresses = new HashSet<>();
    private Set<String> phones = new HashSet<>();
    
    private Set<String> addresses1 = new HashSet<>();
    private Set<String> phones1 = new HashSet<>();
    
    private Set<String> addresses2 = new HashSet<>();
    private Set<String> phones2 = new HashSet<>();
    
 
   

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/scrap")
    @Timed
    public String scrapData(HttpServletRequest request) {
        log.debug("REST request to scrap data from Ace Hardware");
        
        double horizonInterval = (rightPos - leftPos) / horizonDensity;
        double verticalInterval = (topPos - bottomPos) / verticalDensity;
        
        System.out.println(horizonInterval + " " + verticalInterval);
        
        List<Double> horizonMarks = new ArrayList<>();
        List<Double> verticalMarks = new ArrayList<>();
        
        double horizonSlider = leftPos;
        while (horizonSlider < rightPos) {
            horizonMarks.add(horizonSlider);
            //System.out.println("horizonSlider" + horizonSlider);
            horizonSlider += horizonInterval;
            
        }
        horizonMarks.add(rightPos);
        
        double verticalSlider = bottomPos;
        while (verticalSlider < topPos) {
            verticalMarks.add(verticalSlider);
            //System.out.println("verticalSlider: " + verticalSlider);
            verticalSlider += verticalInterval;
            
        }
        verticalMarks.add(topPos);
        
        initCheckList();
        compare();
        
        RestTemplate restTemplate = new RestTemplate();
        //scrap now
        for (Double horizonMark : horizonMarks) {
            for (Double verticalMark : verticalMarks) {
                
                
                String url = "http://www.acehardware.com/storeLocServ?operation=radiusSearch&lat=" + verticalMark.doubleValue() + "&lon=" + horizonMark.doubleValue() + "&radius=50&token=ACE";
                
                System.out.println("Lat Long: " + url);
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root;
                try {
                    root = mapper.readTree(response.getBody());
                    JsonNode results = root.path("RESULTS");
                    
                    Store[] stores = mapper.readValue(results.toString(), Store[].class);
                    System.out.println(stores.length);
                    
                    writeData(OUTPUT_FILE_PATH, stores); 
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return request.getRemoteUser();
    }
    
    private void compare () {
        for (String phone2 : phones2) {
            if (!phones1.contains(phone2)) {
                System.out.println("Found difference: " + phone2);
            }
        }
    }
    
    private void initCheckList1() {
        addresses1 = new HashSet<>();
        phones1 = new HashSet<>();
        
        try {
            InputStream fis = new FileInputStream("/home/hai/Work/projects/scrapGeofence/store1.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
                Row row = (XSSFRow) rowIterator.next();
                
                Cell address1Cell = row.getCell(5);
                Cell address2Cell = row.getCell(6);
                
                Cell phoneCell = row.getCell(12);
                String address1 = (address1Cell == null) ? "" : address1Cell.getStringCellValue();
                String address2 = (address2Cell == null) ? "" : address2Cell.getStringCellValue();
                String phone = (phoneCell == null) ? "" : phoneCell.getStringCellValue();
                addresses1.add(address1 + address2);
                phones1.add(phone.replace(".", ""));
               
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
    
    private void initCheckList2() {
        addresses2 = new HashSet<>();
        phones2 = new HashSet<>();
        
        try {
            InputStream fis = new FileInputStream("/home/hai/Work/projects/scrapGeofence/store2-old.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
                Row row = (XSSFRow) rowIterator.next();
                
                Cell address1Cell = row.getCell(5);
                Cell address2Cell = row.getCell(6);
                
                Cell phoneCell = row.getCell(12);
                String address1 = (address1Cell == null) ? "" : address1Cell.getStringCellValue();
                String address2 = (address2Cell == null) ? "" : address2Cell.getStringCellValue();
                String phone = (phoneCell == null) ? "" : phoneCell.getStringCellValue();
                addresses2.add(address1 + address2);
                phones2.add(phone.replace(".", ""));
               
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
    
    private void initCheckList() {
        addresses = new HashSet<>();
        phones = new HashSet<>();
        
        try {
            InputStream fis = new FileInputStream(OUTPUT_FILE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
                Row row = (XSSFRow) rowIterator.next();
                
                Cell address1Cell = row.getCell(5);
                Cell address2Cell = row.getCell(6);
                
                Cell phoneCell = row.getCell(12);
                String address1 = (address1Cell == null) ? "" : address1Cell.getStringCellValue();
                String address2 = (address2Cell == null) ? "" : address2Cell.getStringCellValue();
                String phone = (phoneCell == null) ? "" : phoneCell.getStringCellValue();
                addresses.add(address1 + address2);
                phones.add(phone);
               
            }
            workbook.close();
            fis.close();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }
    
    
    
    
    private void initExcelFile(String filePath) throws FileNotFoundException, IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        
        
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        
        workbook.close();
        outputStream.flush();
        outputStream.close();
        
    }
    
    private void writeData(String filePath, Store[] stores) {
        log.debug("Write data to excel. Size = " + stores.length);
        InputStream inp;
        try {
            inp = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
            return;
        }
         
        Workbook wb;
        try {
            wb = WorkbookFactory.create(inp);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            
            log.debug(e.getMessage());
            return;
        } 
         
         Sheet sheet = wb.getSheetAt(0);
         int startRow = sheet.getLastRowNum() + 1;
         log.debug("startRow: " + startRow);
             
         
         
         for (Store store : stores) {
             StoreDetail storeDetail = store.getStore();
             if (!addresses.contains(storeDetail.getAddress1() + storeDetail.getAddress2()) && !phones.contains(storeDetail.getPhoneNumber())) {
                 Row row = sheet.getRow(startRow);
                 if (row == null) {
                     row = sheet.createRow(startRow);
                 }
                 
                 
                 int cellIndex = 0;
                 createCell(row, storeDetail.getGroup(), cellIndex++);
                 createCell(row, storeDetail.getSubGroup(), cellIndex++);
                 createCell(row, storeDetail.getCompanyName(), cellIndex++);
                 createCell(row, storeDetail.getBrand(), cellIndex++);
                 createCell(row, storeDetail.getLocationName(), cellIndex++);
                 createCell(row, storeDetail.getAddress1(), cellIndex++);
                 createCell(row, storeDetail.getAddress2(), cellIndex++);
                 createCell(row, storeDetail.getCity(), cellIndex++);
                 createCell(row, storeDetail.getStateCode(), cellIndex++);
                 createCell(row, storeDetail.getPostalCode(), cellIndex++);
                 createCell(row, storeDetail.getCountry(), cellIndex++);
                 createCell(row, storeDetail.getCountryCode(), cellIndex++);
                 createCell(row, storeDetail.getPhoneNumber(), cellIndex++);
                 createCell(row, storeDetail.getLatitude(), cellIndex++);
                 createCell(row, storeDetail.getLongitude(), cellIndex++);
                 
                 addresses.add(storeDetail.getAddress1() + storeDetail.getAddress2());
                 phones.add(storeDetail.getPhoneNumber());
                 startRow++;
             }
             
         }
    
         // Write the output to a file
         FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.debug(e.getMessage());
        } finally {
            try {
                fileOut.flush();
                fileOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.debug(e.getMessage());
            }
            
        }
    }
    
    private void createCell(Row row, Object value, int index) {
        Cell cell = row.createCell(index);
        if (value instanceof String) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue((String)value);
        } else if (value instanceof Double) {
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            cell.setCellValue((Double)value);
        } 
    }
}
