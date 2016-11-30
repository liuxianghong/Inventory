package cn.liuxh.controller;

import cn.liuxh.model.Goods;
import cn.liuxh.service.GoodsService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


            Goods user = new Goods();
            user.setName(name);
            user.setSeriesNo(SeriesNo);
            user.setSize(Size);
            user.setCount(Integer.parseInt(Count));
            user.setLocationNo(LocationNo);
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
            List<Goods> goodses = getExcelInfo(fileName,targetFile);
            goodsService.importGoods(goodses);
            for (Goods good:
            goodses) {
                System.out.println("importGoods: "+good.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);

        System.out.println("upload getContextPath:"+request.getContextPath()+"/upload/"+fileName);
        return "index";
    }


    Workbook create(String filePath,FileInputStream is) throws IOException {
        return new HSSFWorkbook(is);
    }

    public boolean validateExcel(String filePath){
        return(filePath.endsWith(".xls") || filePath.endsWith(".xlsx"));
    }

    public List<Goods> getExcelInfo(String fileName,File file) throws IOException, InvalidFormatException {

        if (!validateExcel(fileName)){
            return  null;
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
                    if (c == 0) {
                        goods.setName(cell.getStringCellValue());
                        str = cell.getStringCellValue();
                    } else if (c == 1) {
                        goods.setSize(cell.getStringCellValue());
                        str = cell.getStringCellValue();
                    } else if (c == 2) {
                        goods.setSeriesNo(cell.getStringCellValue());
                        str = cell.getStringCellValue();
                    } else if (c == 3) {
                        if (cell.getCellTypeEnum() == CellType.NUMERIC){
                            goods.setCount((int)cell.getNumericCellValue());
                            str += cell.getNumericCellValue();
                        } else if (cell.getCellTypeEnum() == CellType.STRING) {
                            goods.setCount(Integer.parseInt(cell.getStringCellValue()));
                            str = cell.getStringCellValue();
                        }
                    } else if (c == 4) {
                        goods.setLocationNo(cell.getStringCellValue());
                        str = cell.getStringCellValue();
                    }
                    System.out.println("getExcelInfo:"+str);
                }
            }
            if (goods.getSeriesNo() != null
                    && !goods.getSeriesNo().trim().isEmpty()) {
                customerList.add(goods);
                System.out.println("getExcelInfo:"+customerList.size());
            }
        }
        workbook.close();
        return customerList;
    }
}
