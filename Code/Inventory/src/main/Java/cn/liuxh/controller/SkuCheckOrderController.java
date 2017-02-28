package cn.liuxh.controller;

import cn.liuxh.model.Group;
import cn.liuxh.model.SkuCheckOrder;
import cn.liuxh.model.User;
import cn.liuxh.service.SkuCheckOrderService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
 * Created by liuxianghong on 2016/11/28.
 */

@Controller
public class SkuCheckOrderController {

    @Autowired
    private SkuCheckOrderService skuCheckOrderService;

    @RequestMapping("/getSkuCheckOrders")
    @ResponseBody
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        User user = UserController.userController.getUser(uid);
        if (user == null) return map;
        List students = skuCheckOrderService.getAll(start,rows,user.getGroupId());
        map.put("total",skuCheckOrderService.count(user.getGroupId()));
        map.put("orders",students);
        return map;
    }

    @RequestMapping(value = "/getSkuCheckOrderById")
    @ResponseBody
    public SkuCheckOrder getSkuCheckOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id =  request.getParameter("id");
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid)))
                return null;
            System.out.println("getSkuCheckOrderById: "+ id);
            return skuCheckOrderService.getDetailById(Integer.parseInt(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/createSkuCheckOrder")
    @ResponseBody
    public SkuCheckOrder addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            User user = UserController.userController.getUser(Integer.parseInt(uid));
            if (user == null) return null;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String seriesNo =  request.getParameter("seriesNo");
            String orderName = request.getParameter("orderName");
            String calculate = request.getParameter("calculate");
            SkuCheckOrder sku = new SkuCheckOrder();
            sku.setGroupId(user.getGroupId());
            sku.setOrderName(orderName);
            sku.setSeriesNo(seriesNo);
            sku.setCalculate(Integer.parseInt(calculate));
            sku.setTime(time);
            if (skuCheckOrderService.haveGoods(seriesNo,user.getGroupId())) {
                int ret = 0;
                ret = skuCheckOrderService.add(sku);
                if (ret != 0){
                    return skuCheckOrderService.getDetailById(sku.getId());
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/updateSkuCheckOrder")
    @ResponseBody
    public SkuCheckOrder updateProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String orderName = request.getParameter("orderName");
            String calculate = request.getParameter("calculate");
            String id = request.getParameter("id");
            SkuCheckOrder sku = new SkuCheckOrder();
            sku.setOrderName(orderName);
            sku.setId(Integer.parseInt(id));
            sku.setCalculate(Integer.parseInt(calculate));
            sku.setTime(time);
            int ret = 0;
            ret = skuCheckOrderService.update(sku);
            if (ret != 0){
                return skuCheckOrderService.getDetailById(sku.getId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/deleteSkuCheckOrder")
    @ResponseBody
    public Map delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String uid = request.getParameter("uid");
            if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
            String id = request.getParameter("id");


            int ret = 0;
            if (!id.isEmpty()) {
                ret = skuCheckOrderService.delete(Integer.parseInt(id));
            }
            if (ret != 0){
                return getAllOrders(1,100,Integer.parseInt(uid));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @RequestMapping(value = "/getAllSkuOrdersE")
    @ResponseBody
    public Map getAllSkuOrdersE(HttpServletRequest request,@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {
            List students = skuCheckOrderService.getAll(start,rows, group.getId());
            map.put("rows",students);
            map.put("total", skuCheckOrderService.count(group.getId()));
            System.out.println("getAllSkuOrdersE: " + students.size());
        }

        return map;
    }


    @RequestMapping("/truncateSkuOrder")
    @ResponseBody
    public int truncate(HttpServletRequest request) {
        try {

            Group group = (Group) request.getSession().getAttribute("group");
            if (group != null && group.getId() != 0) {
                int ret = skuCheckOrderService.truncate(group.getId());
                if (ret != 0){
                    return 1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping("/exportSkuOrder")
    public String exportSortOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {

        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return "login";
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

        int count = skuCheckOrderService.count(group.getId());
        int rows = 2000;
        for (int i=0 ;i*rows < count ; i++) {
            List<SkuCheckOrder> goodsList = skuCheckOrderService.getAll(i*rows,rows, group.getId());

            for (int j=0;j<goodsList.size();j++){
                row = sheet.createRow(i * 2000 + j + 1);

                SkuCheckOrder goods = goodsList.get(j);
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

    @RequestMapping(value = "/uploadSku")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file
            , HttpServletRequest request, ModelMap model) {

        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return "login";
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
            getExcelInfo(fileName,targetFile,group);
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

    public void getExcelInfo(String fileName, File file, Group group) throws IOException, InvalidFormatException {

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

        //盘点单号，产品名称，规格，条形码，数量
        List<SkuCheckOrder> customerList=new ArrayList<SkuCheckOrder>();
        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            SkuCheckOrder goods = new SkuCheckOrder();
            goods.setCalculate(0);
            goods.setGroupId(group.getId());
            Timestamp time = new Timestamp(System.currentTimeMillis());
            goods.setTime(time);
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
                        goods.setOrderName(str);
                    } else if (c == 1) {
                        goods.setName(str);
                    } else if (c == 2) {
                        goods.setSize(str);
                    } else if (c == 3) {
                        goods.setSeriesNo(str);
                    } else if (c == 4) {
                        //goods.setCalculate(safeInt(str));
                    }
                    System.out.println("getExcelInfo:"+str);
                }
            }
            if (goods.getSeriesNo() != null
                    && !goods.getSeriesNo().trim().isEmpty()
                    && skuCheckOrderService.haveGoods(goods.getSeriesNo(),group.getId())) {
                customerList.add(goods);
                //System.out.println("getExcelInfo:"+customerList.size());
                if (customerList.size() > 2000) {
                    skuCheckOrderService.importSkus(customerList);
                    customerList.clear();
                }
            }
        }
        if (!customerList.isEmpty()) {
            skuCheckOrderService.importSkus(customerList);
            customerList.clear();
        }
        workbook.close();
    }
}
