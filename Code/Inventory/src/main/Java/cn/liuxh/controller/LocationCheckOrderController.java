package cn.liuxh.controller;

import cn.liuxh.model.*;
import cn.liuxh.service.LocationCheckOrderService;
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
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by liuxianghong on 16/11/28.
 */

@Controller
public class LocationCheckOrderController {

    @Autowired
    private LocationCheckOrderService locationCheckOrderService;



    @RequestMapping("/getAllLocationOrdersE")
    @ResponseBody
    public Map getAllLocationOrdersE(HttpServletRequest request,int page, int rows) throws Exception{
        Map map = new HashMap();
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {
            int start = (page-1)*rows;
            List students = locationCheckOrderService.getAllLocationOrdersE(start,rows,group.getId());
            map.put("rows",students);
            map.put("total", locationCheckOrderService.SkuCount(group.getId()));
        }

        return map;
    }


    @RequestMapping("/getLocationCheckOrders")
    @ResponseBody
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid)
            throws Exception{
        int start = (page-1)*rows;
        User user = UserController.userController.getUser(uid);
            if (user == null) return null;
        Map map = new HashMap();
        List<LocationCheckOrder> students = locationCheckOrderService.getAll(start, rows,user.getGroupId());
        for (int i = 0; i < students.size(); i++) {
            LocationCheckOrder order = students.get(i);
            List<LocationSku> skus = locationCheckOrderService.getAllSku(order.getId());
            for (LocationSku sku:
                 skus) {
                order.addSku(sku);
            }
            System.out.println("getLocationCheckOrders: "+order.toString());
        }
        map.put("total",locationCheckOrderService.count(user.getGroupId()));
        map.put("orders",students);
        return map;
    }

    @RequestMapping("/createLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder addOrder(@RequestBody LocationCheckOrderUser orderUser,HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            User user = UserController.userController.getUser(orderUser.getUid());
            if (user == null) return null;
            LocationCheckOrder order = orderUser.getOrder();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            order.setTime(time);
            order.setGroupId(user.getGroupId());
            int ret = 0;
            ret = locationCheckOrderService.add(order);

            List<LocationSku> skus = new ArrayList<LocationSku>();
            List<LocationLocation> locations = order.getLocations();
            for (LocationLocation location:locations) {
                for (LocationSku sku:
                location.getSku()) {
                    skus.add(sku);
                    sku.setOrderId(order.getId());
                }
            }
            locationCheckOrderService.addSku(skus);

            if (ret != 0){
                return getOrderDetail(order.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("createLocationCheckOrder: "+e.toString());
        }
        return null;
    }

    public LocationCheckOrder getOrderDetail(int id) {
        LocationCheckOrder order = locationCheckOrderService.getDetailById(id);
        List<LocationSku> skus = locationCheckOrderService.getAllSku(id);
        for (LocationSku sku:
                skus) {
            order.addSku(sku);
        }

        System.out.println("getOrderDetail: "+order.toString());

        return order;
    }


    @RequestMapping("/getLocationDetails")
    @ResponseBody
    public Map getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String LocationNo = request.getParameter("locationNo");
        String uid = request.getParameter("uid");
        User user = UserController.userController.getUser(Integer.parseInt(uid));
        if (user == null) return null;
        List<Goods> goodses = locationCheckOrderService.getDetailByLocationNo(LocationNo,user.getGroupId());

        Map map = new HashMap();
        map.put("locationNo",LocationNo);
        map.put("sku",goodses);
        return map;
    }

    @RequestMapping("/updateLocationCheckOrder")
    @ResponseBody
    public LocationCheckOrder updateOrder(@RequestBody LocationCheckOrderUser orderUser) throws Exception {
        try {
            int ret = 0;
            User user = UserController.userController.getUser(orderUser.getUid());
            if (user == null) return null;
            LocationCheckOrder order = orderUser.getOrder();
            locationCheckOrderService.deleteSku(order.getId());
            ret = locationCheckOrderService.update(order);
            List<LocationSku> skus = new ArrayList<>();
            for (LocationLocation locations:
                    order.getLocations()) {
                skus.addAll(locations.getSku());
            }

            for (LocationSku sku:skus) {
                sku.setOrderId(order.getId());
            }
            locationCheckOrderService.addSku(skus);
            if (ret != 0){
                return getOrderDetail(order.getId());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/deleteLocationCheckOrder")
    @ResponseBody
    public Map delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id = request.getParameter("id");

            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            int ret = 0;
            int ret2 = 0;
            if (!id.isEmpty()) {
                ret = locationCheckOrderService.delete(Integer.parseInt(id));
                ret2 = locationCheckOrderService.deleteSku(Integer.parseInt(id));
            }
            return getAllOrders(1,100,Integer.parseInt(uid));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }


    @RequestMapping("/truncateLocationOrder")
    @ResponseBody
    public int truncate(HttpServletRequest request) {
        try {

            Group group = (Group) request.getSession().getAttribute("group");
            if (group != null && group.getId() != 0) {
                int ret = locationCheckOrderService.truncate(group.getId());
                if (ret != 0){
                    return 1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }


    @RequestMapping("/exportLocationOrder")
    public String exportSortOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return  "login";
        }
        XSSFWorkbook webBook = new XSSFWorkbook();
        XSSFSheet sheet = webBook.createSheet("SKU盘点");
        XSSFRow row = sheet.createRow((int)0);

        XSSFCellStyle style = webBook.createCellStyle();
        //产品名称	规格	条形码	库位	数量	盘点数量	盘点差异
        String[] strs = {"盘点表单名称","产品名称","规格","条形码","库位","数量","盘点数量","盘点差异"};
        for (int i = 0; i < strs.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(strs[i]);
            cell.setCellStyle(style);
        }

        int count = locationCheckOrderService.count(group.getId());
        int rows = 2000;
        for (int i=0 ;i*rows < count ; i++) {
            List<LocationSkuE> goodsList = locationCheckOrderService.getAllLocationOrdersE(i*rows,rows, group.getId());

            for (int j=0;j<goodsList.size();j++){
                row = sheet.createRow(i * 2000 + j + 1);

                LocationSkuE goods = goodsList.get(j);
                row.createCell(0).setCellValue(goods.getOrderName());
                row.createCell(1).setCellValue(goods.getName());
                row.createCell(2).setCellValue(goods.getSize());
                row.createCell(3).setCellValue(goods.getSeriesNo());
                row.createCell(4).setCellValue(goods.getLocationNo());
                row.createCell(5).setCellValue(goods.getCount());
                row.createCell(6).setCellValue(goods.getCalculate());
                row.createCell(7).setCellValue(goods.getCount() - goods.getCalculate());
            }
        }

        String msg = "操作失败！";
        boolean flag = false;
        //第六步 将文件存放到指定位置
        try{

            String path = request.getSession().getServletContext().getRealPath("download");
            String fileName = "SKU盘点" + new Date().getTime();
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

    @RequestMapping(value = "/uploadLocationOrder")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file
            , HttpServletRequest request, ModelMap model) {

        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return  "login";
        }
        System.out.println("upload:"+"开始");
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);
        return "index";
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

        Workbook workbook = WorkbookFactory.create(file);//new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int totalRows=sheet.getPhysicalNumberOfRows();

        int totalCells = 0;
        //得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){
            totalCells =sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<LocationSku> customerList=new ArrayList<LocationSku>();
        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            LocationSku goods = new LocationSku();
            //循环Excel的列
            for(int c = 0; c <totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    String str = "";
                    if (cell.getCellTypeEnum() == CellType.NUMERIC){
                        str += cell.getNumericCellValue();
                    } else if (cell.getCellTypeEnum() == CellType.STRING) {
                        str = cell.getStringCellValue();
                    }
                    if (c == 0) {
                        goods.setName(str);
                    } else if (c == 1) {
                        goods.setSize(str);
                    } else if (c == 2) {
                        goods.setSeriesNo(str);
                    } else if (c == 3) {
                        goods.setLocationNo(str);
                    } else if (c == 4) {
                        goods.setCount(safeInt(str));
                    }
                    System.out.println("getExcelInfo:"+str);
                }
            }
            if (goods.getSeriesNo() != null
                    && !goods.getSeriesNo().trim().isEmpty()) {
                customerList.add(goods);
                //System.out.println("getExcelInfo:"+customerList.size());
                if (customerList.size() > 2000) {
                    //locationCheckOrderService.importGoods(customerList);
                    customerList.clear();
                }
            }
        }
        if (!customerList.isEmpty()) {
            //locationCheckOrderService.importGoods(customerList);
            customerList.clear();
        }
        workbook.close();
    }
}




