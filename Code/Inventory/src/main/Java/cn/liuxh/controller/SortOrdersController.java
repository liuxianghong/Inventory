package cn.liuxh.controller;

import cn.liuxh.model.SortOrders;
import cn.liuxh.model.SortOrdersUser;
import cn.liuxh.model.SortSku;
import cn.liuxh.service.SortOrdersService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxianghong on 2016/11/30.
 */
@Controller
public class SortOrdersController {
    @Autowired
    private SortOrdersService sortOrdersService;

    @RequestMapping("/getSortOrders")
    @ResponseBody
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        if (!UserController.userController.haveUser(uid)) return null;
        List<SortOrders> students = sortOrdersService.getAll(start,rows);
        for (int i = 0; i < students.size(); i++) {
            SortOrders order = students.get(i);
            List<SortSku> sku = sortOrdersService.getAllSku(order.getOrderName());
            order.setSku(sku);
        }
        map.put("orders",students);
        map.put("total",sortOrdersService.count());
        return map;
    }

//    @RequestMapping("/createLocationCheckOrder")
//    @ResponseBody
//    public LocationCheckOrder addOrder(@RequestBody LocationCheckOrder order, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        try {
//
//            Timestamp time = new Timestamp(System.currentTimeMillis());
//            order.setTime(time);
//            int ret = 0;
//            ret = sortOrdersService.add(order);
//
//            List<LocationSku> skus = order.getSku();
//            for (LocationSku sku:skus) {
//                sku.setOrderId(order.getId());
//            }
//            sortOrdersService.addSku(skus);
//
//            if (ret != 0){
//                return getOrderDetail(order.getId());
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return null;
//    }

    public SortOrders getOrderDetail(int id) {
        SortOrders order = sortOrdersService.getDetailById(id);
        List skus = sortOrdersService.getAllSku(order.getOrderName());
        order.setSku(skus);

        return order;
    }


//    @RequestMapping("/getLocationDetails")
//    @ResponseBody
//    public LocationCheckOrder getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String LocationNo = request.getParameter("locationNo");
//        LocationCheckOrder order = sortOrdersService.getDetailByLocationNo(LocationNo);
//        List skus = sortOrdersService.getAllSku(order.getId());
//        order.setSku(skus);
//        return order;
//    }

    @RequestMapping("/updateSortOrder")
    @ResponseBody
    public SortOrders updateOrder(@RequestBody SortOrdersUser orderUser) throws Exception {
        try {
            int ret = 0;
            if (!UserController.userController.haveUser(orderUser.getUid())) return null;
            SortOrders order = orderUser.getOrder();
            System.out.println("updateSortOrder: "+order.toString());
            List<SortSku> skus = order.getSku();
            ret = sortOrdersService.updateSku(skus);
            System.out.println("updateSortOrder: "+ret);
            if (ret != 0){
                return getOrderDetail(order.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

//    @RequestMapping(value = "/deleteLocationCheckOrder")
//    @ResponseBody
//    public Map delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        try {
//            String id = request.getParameter("id");
//
//            int ret = 0;
//            int ret2 = 0;
//            if (!id.isEmpty()) {
//                ret = sortOrdersService.delete(Integer.parseInt(id));
//                ret2 = sortOrdersService.deleteSku(Integer.parseInt(id));
//            }
//            return getAllOrders();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return null;
//    }

    @RequestMapping(value = "/uploadOrder")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file
            , HttpServletRequest request, ModelMap model) {

        System.out.println("upload:"+"开始");
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
//        String fileName = new Date().getTime()+".jpg";
        if (!validateExcel(fileName)){
            return "index";
        }
        System.out.println("upload getRealPath:"+path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
            List<SortOrders> sortOrders = new ArrayList<SortOrders>();
            List<SortSku> skus = new ArrayList<SortSku>();
            getExcelInfo(fileName,targetFile,sortOrders,skus);
            for (SortOrders order:
            sortOrders) {
                sortOrdersService.importOrders(order);
                sortOrdersService.deleteSku(order.getOrderName());
            }
            sortOrdersService.importSkus(skus);
            //goodsService.importGoods(goodses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);

        System.out.println("upload getContextPath:"+request.getContextPath()+"/upload/"+fileName);
        return "index";
    }


    Workbook create(String filePath, FileInputStream is) throws IOException {
        return new HSSFWorkbook(is);
    }

    public boolean validateExcel(String filePath){
        return(filePath.endsWith(".xls") || filePath.endsWith(".xlsx"));
    }

    public int safeInt(String string) {
        try {
            return (int)Double.parseDouble(string);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void getExcelInfo(String fileName,File file,List<SortOrders> sortOrders,List<SortSku> skus) throws IOException, InvalidFormatException {

        if (!validateExcel(fileName)){
            return;
        }

        Workbook workbook = WorkbookFactory.create(file);//new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int totalRows=sheet.getPhysicalNumberOfRows();

        int totalCells = 0;
        //得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){
            totalCells =sheet.getRow(0).getPhysicalNumberOfCells();
        }

        SortOrders order = null;
        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            SortSku sku = new SortSku();
            sku.setCalculate(0);
            //循环Excel的列
            for(int c = 0; c <totalCells; c++) {
                Cell cell = row.getCell(c);
                String po = "";
                if (null != cell) {
                    String str = "";
                    if (cell.getCellTypeEnum() == CellType.NUMERIC){
                        str += cell.getNumericCellValue();
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        str = cell.getStringCellValue();
                    }
                    if (c == 0) {
                        po = str;
                    } else if (c == 1) {
                        sku.setOrderName(str);
                        if (order == null || !order.getOrderName().equalsIgnoreCase(str)) {
                            order = new SortOrders();
                            order.setOrderName(str);
                            order.setPo(po);
                            if (str != null
                                    && !str.trim().isEmpty()) {
                                sortOrders.add(order);
                            }
                        }
                    } else if (c == 2) {
                        order.setType(str);
                    } else if (c == 3) {
                        order.setAddress(str);
                        sku.setLocation(str);
                    } else if (c == 4) {
                        order.setInTime(str);
                    }
                    else if (c == 5) {
                        sku.setSeriesNo(str);
                    }
                    else if (c == 6) {
                        sku.setGoodNo(str);

                    }
                    else if (c == 7) {
                        sku.setProductName(str);
                    }
                    else if (c == 8) {
                        sku.setSize(str);
                    }
                    else if (c == 9) {

                        sku.setCount(safeInt(str));
                    }else if (c == 10) {

                        sku.setShipped(safeInt(str));
                    }else if (c == 11) {

                        sku.setUnShipped(safeInt(str));
                    }
                    //System.out.println("getExcelInfo:"+str);
                }
            }
            if (sku.getSeriesNo() != null
                    && !sku.getSeriesNo().trim().isEmpty()
                    && sku.getOrderName() != null
                    && !sku.getOrderName().trim().isEmpty()) {
                //order.addSku(sku);
                skus.add(sku);
            }
        }
        workbook.close();
    }

    @RequestMapping("/truncateSortOrder")
    @ResponseBody
    public int truncate() {
        try {

            int ret = sortOrdersService.truncate();
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }
}
