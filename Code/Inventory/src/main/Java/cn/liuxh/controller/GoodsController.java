package cn.liuxh.controller;

import cn.liuxh.model.Goods;
import cn.liuxh.service.GoodsService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by liuxianghong on 2016/11/28.
 */

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/getAllGoodsE")
    @ResponseBody
    public Map getAllOrdersE(int page, int rows) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        List students = goodsService.getAllE(start,rows);
        map.put("rows",students);
        map.put("total", goodsService.count());
        return map;
    }

    @RequestMapping("/getAllGoods")
    @ResponseBody
    public Map getAllOrders() throws Exception{
        Map map = new HashMap();
        List students = goodsService.getAll();
        map.put("rows",students);
        return map;
    }

    @RequestMapping(value = "/saveGoods", method = RequestMethod.POST)
    @ResponseBody
    public int saveProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            String userId = request.getParameter("id");
            String name =  request.getParameter("name");
            String Size = request.getParameter("size");
            String SeriesNo = request.getParameter("seriesNo");
            String Count = request.getParameter("count");
            String LocationNo = request.getParameter("locationNo");
            String remarks1 = request.getParameter("remarks1");
            String remarks2 = request.getParameter("remarks2");


            Goods user = new Goods();
            user.setName(name);
            user.setSeriesNo(SeriesNo);
            user.setSize(Size);
            user.setCount(Integer.parseInt(Count));
            user.setLocationNo(LocationNo);
            user.setRemarks1(remarks1);
            user.setRemarks2(remarks2);
            int ret = 0;
            if (!userId.isEmpty()) {
                //修改用户信息
                user.setId(Integer.parseInt(userId));
                ret = goodsService.update(user);
                //ret = userService.updateUser(user);
            } else {
                //ret = userService.addUser(user);
                ret = goodsService.add(user);
            }
            System.out.println("saveProduct:"+" ret: "+ret + " id: "+user.getId());
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping(value = "/delGoods", method = RequestMethod.POST)
    @ResponseBody
    public int delProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String id = request.getParameter("id");


            int ret = 0;
            if (!id.isEmpty()) {
                ret = goodsService.delete(Integer.parseInt(id));
            }
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }


    @RequestMapping("/getSkuDetails")
    @ResponseBody
    public Goods getSkuDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String seriesNo = request.getParameter("seriesNo");
        String uid = request.getParameter("uid");
        if (!UserController.userController.haveUser(Integer.parseInt(uid))) return null;
        return goodsService.getGoodsBySeriesNo(seriesNo);
    }

    @RequestMapping("/updateSkuLocation")
    @ResponseBody
    public Goods updateSkuLocation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String seriesNo = request.getParameter("seriesNo");

        String locationNo = request.getParameter("locationNo");

        Goods user = new Goods();
        user.setSeriesNo(seriesNo);
        user.setLocationNo(locationNo);

        goodsService.updateSkuLocation(user);


        return goodsService.getGoodsBySeriesNo(seriesNo);
    }


    @RequestMapping(value = "/upload")
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

//            for (Goods good:
//            goodses) {
//                System.out.println("importGoods: "+good.getId());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);

        //System.out.println("upload getContextPath:"+request.getContextPath()+"/upload/"+fileName);
        return "index";
    }


    Workbook create(String filePath,FileInputStream is) throws IOException {
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

        Workbook workbook = WorkbookFactory.create(file);//new HSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int totalRows=sheet.getPhysicalNumberOfRows();

        int totalCells = 0;
        //得到Excel的列数(前提是有行数)
        if(totalRows>=1 && sheet.getRow(0) != null){
            totalCells =sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<Goods> customerList=new ArrayList<Goods>();
        //循环Excel行数,从第二行开始。标题不入库
        for(int r=1;r<totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            Goods goods = new Goods();
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
                    goodsService.importGoods(customerList);
                    customerList.clear();
                }
            }
        }
        if (!customerList.isEmpty()) {
            goodsService.importGoods(customerList);
            customerList.clear();
        }
        workbook.close();
    }


    @RequestMapping("/truncateProduct")
    @ResponseBody
    public int truncate() {
        try {

            int ret = goodsService.truncate();
            if (ret != 0){
                return 1;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping("/exportProduct")
    public String exportProduct(HttpServletRequest request,HttpServletResponse response) throws IOException {
        XSSFWorkbook webBook = new XSSFWorkbook();
        XSSFSheet sheet = webBook.createSheet("产品信息");
        XSSFRow row = sheet.createRow((int)0);

        XSSFCellStyle style = webBook.createCellStyle();
        String[] strs = {"产品名称","规格","条形码","库位"};
        for (int i = 0; i < strs.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(strs[i]);
            cell.setCellStyle(style);
        }

        int count = goodsService.count();
        int rows = 2000;
        for (int i=0 ;i*rows < count ; i++) {
            List<Goods> goodsList = goodsService.getAllE(i*rows,rows);

            for (int j=0;j<goodsList.size();j++){
                row = sheet.createRow(i * 2000 + j + 1);

                Goods goods = goodsList.get(j);
                row.createCell(0).setCellValue(goods.getName());
                row.createCell(1).setCellValue(goods.getSize());
                row.createCell(2).setCellValue(goods.getSeriesNo());
                row.createCell(3).setCellValue(goods.getLocationNo());
            }
        }

        String msg = "操作失败！";
        boolean flag = false;
        //第六步 将文件存放到指定位置
        try{

            String path = request.getSession().getServletContext().getRealPath("download");
            String fileName = "产品库位数据" + new Date().getTime();
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
