package cn.liuxh.controller;

import cn.liuxh.model.SortOrders;
import cn.liuxh.model.SortOrdersUser;
import cn.liuxh.model.SortSku;
import cn.liuxh.service.SortOrdersService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by liuxianghong on 2016/11/30.
 */
@Controller
public class SortOrdersController {
    @Autowired
    private SortOrdersService sortOrdersService;


    @RequestMapping("/getAllSortOrdersE")
    @ResponseBody
    public Map getAllUsersE(@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="rows", defaultValue="1000") int rows) throws Exception{
        int start = (page-1)*rows;
        List students = sortOrdersService.getAllSkuPage(start,rows);
        Map map = new HashMap();
        map.put("rows",students);
        map.put("total", sortOrdersService.selectSkUCount());
        return map;
    }


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

            getExcelInfo(fileName,targetFile);

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

    public void getExcelInfo(String fileName,File file) throws IOException, InvalidFormatException {

        if (!validateExcel(fileName)){
            return;
        }

        List<SortSku> skus = new ArrayList<SortSku>();

        Workbook workbook = WorkbookFactory.create(file);//new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int totalRows=sheet.getPhysicalNumberOfRows();

        int totalCells = 0;
        //得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){
            totalCells =sheet.getRow(0).getPhysicalNumberOfCells();
        }

        SortOrders order = null;
        boolean neddSave = false;
        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;

            if (neddSave && order != null) {
                sortOrdersService.importOrders(order);
                neddSave = false;
            }
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
                                sortOrdersService.deleteSku(order.getOrderName());
                                neddSave = true;
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
                if (skus.size() > 2000) {
                    sortOrdersService.importSkus(skus);
                    skus.clear();
                }
            }
        }

        if (neddSave && order != null) {
            sortOrdersService.importOrders(order);
        }
        sortOrdersService.importSkus(skus);
        skus.clear();
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


    @RequestMapping("/exportSortOrder")
    public String exportSortOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
        XSSFWorkbook webBook = new XSSFWorkbook();
        XSSFSheet sheet = webBook.createSheet("订单数据");
        XSSFRow row = sheet.createRow((int)0);

        XSSFCellStyle style = webBook.createCellStyle();
        String[] strs = {"系统订单编码","拣货单","产品名称","规格","条形码","数量","订单状态"};
        for (int i = 0; i < strs.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(strs[i]);
            cell.setCellStyle(style);
        }

        int count = sortOrdersService.selectSkUCount();
        int rows = 2000;
        for (int i=0 ;i*rows < count ; i++) {
            List<SortSku> goodsList = sortOrdersService.getAllSkuPage(i*rows,rows);

            for (int j=0;j<goodsList.size();j++){
                row = sheet.createRow(i * 2000 + j + 1);

                SortSku goods = goodsList.get(j);
                row.createCell(0).setCellValue("SOI" + goods.getOid());
                row.createCell(1).setCellValue(goods.getOrderName());
                row.createCell(2).setCellValue(goods.getProductName());
                row.createCell(3).setCellValue(goods.getSize());
                row.createCell(4).setCellValue(goods.getSeriesNo());
                row.createCell(5).setCellValue(goods.getCount());
                row.createCell(6).setCellValue(goods.getCalculate());
            }
        }

        String msg = "操作失败！";
        boolean flag = false;
        //第六步 将文件存放到指定位置
        try{

            String path = request.getSession().getServletContext().getRealPath("download");
            String fileName = "订单数据" + new Date().getTime();
            System.out.println("download getRealPath:"+path);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            //FileOutputStream fout = new FileOutputStream(targetFile.getAbsolutePath());
            webBook.write(os);
            webBook.close();

            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName).getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
            return null;



//            flag = true;
//            msg = "操作成功！";
        }catch(Exception e){
            e.printStackTrace();
            msg = "操作失败";
            throw e;
        }




//        Map<String, Object> modelMap = new HashMap<String, Object>();
//        modelMap.put("flag", 1);
//        modelMap.put("msg", 2);
//        return JSONObject.toJSONString(modelMap);
    }
}
