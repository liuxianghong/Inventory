package cn.liuxh.controller;

import cn.liuxh.model.*;
import cn.liuxh.service.SortOrdersService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by liuxianghong on 2016/11/30.
 */
@Controller
public class SortOrdersController {
    @Autowired
    private SortOrdersService sortOrdersService;


    private int pickOrderMaxMun = 150;

//    @RequestMapping("/getAllSortOrdersE")
//    @ResponseBody
//    public Map getAllUsersE(HttpServletRequest request,@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="rows", defaultValue="1000") int rows) throws Exception{
//        Map map = new HashMap();
//        Group group = (Group) request.getSession().getAttribute("group");
//        if (group != null && group.getId() != 0) {
//            int start = (page-1)*rows;
//            List students = sortOrdersService.getAllSkuPage(start,rows,group.getId());
//            map.put("rows",students);
//            map.put("total", sortOrdersService.selectSkUCount());
//        }
//
//        return map;
//    }

    @RequestMapping("/getAllPickSkuE")
    @ResponseBody
    public Map getAllPickSkuE(HttpServletRequest request,@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="rows", defaultValue="1000") int rows) throws Exception{
        Map map = new HashMap();
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {
            int start = (page-1)*rows;
            List students = sortOrdersService.getAllPickSku(start,rows,group.getId());
            map.put("rows",students);
            map.put("total", sortOrdersService.selectPickSkuCount(group.getId()));
        }

        return map;
    }

    @RequestMapping("/getSortOrders")
    @ResponseBody
    public Map getAllOrders(@RequestParam(value="page", defaultValue="1") int page
            , @RequestParam(value="rows", defaultValue="100") int rows,@RequestParam(value="uid") int uid) throws Exception{
        Map map = new HashMap();
        int start = (page-1)*rows;
        User user = UserController.userController.getUser(uid);
        if (user == null) return null;
        List<SortOrders> students = sortOrdersService.getAll(start,rows,user.getGroupId());
        for (int i = 0; i < students.size(); i++) {
            SortOrders order = students.get(i);

            List<SortSku> sku = sortOrdersService.getAllSku(order.getOrderName(),user.getGroupId());
            order.setSku(sku);
        }
        map.put("orders",students);
        map.put("total",sortOrdersService.count(user.getGroupId()));
        return map;
    }


    public SortOrders getOrderDetail(int id,int groupId) {
        SortOrders order = sortOrdersService.getDetailById(id);
        List skus = sortOrdersService.getAllSku(order.getOrderName(),groupId);
        order.setSku(skus);

        return order;
    }



    @RequestMapping("/updateSortOrder")
    @ResponseBody
    public SortOrders updateOrder(@RequestBody SortOrdersUser orderUser) throws Exception {
        try {
            int ret = 0;
            User user = UserController.userController.getUser(orderUser.getUid());
            if (user == null) return null;
            SortOrders order = orderUser.getOrder();
            System.out.println("updateSortOrder: "+order.toString());
            List<SortSku> skus = order.getSku();
            ret = sortOrdersService.updateSku(skus);
            System.out.println("updateSortOrder: "+ret);
            if (ret != 0){
                return getOrderDetail(order.getId(),user.getGroupId());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

//
//    getPickLocations
//            请求
//    {
//        "uid":1
//    }
//    返回
//    {"msg":"查询成功","data":["华中","华北","华南"],"state":0}
//
//
//
//
//
//    getPickOrder
//            请求
//
//    {
//        "uid": 1,
//            "state": 0, 		//0:全部；1：未开始；2：已完成；3：分拣中 4:未开始and分拣中
//            "page": 1,
//            "rows": 1,
//            "location": “华北" //不传为所有地区
//    }
//    返回
//    {"msg":"查询成功","total":44,"data":[{"id":191427,"state":1,"shortName":"PICK-6100060655-52","location":"华南","skus":[{"productName":"镂花美背","size":"红色薄杯-75B","locationNo":"#","seriesNo":"MX64211075B3","count":8}]}],"state":0}
//
//




    @RequestMapping("/getPickOrder")
    @ResponseBody
    public Map getPickOrder(@RequestBody JSONObject jsonObj) throws Exception{
        Map map = new HashMap();

        map.put("state",1);
        try {

            int uid = jsonObj.getInteger("uid");
            int state = jsonObj.getInteger("state");
            int page = jsonObj.getInteger("page");
            int row = jsonObj.getInteger("rows");
            String location = null;
            if (jsonObj.containsKey("location")){
                location = jsonObj.getString("location");
            }

            User user = UserController.userController.getUser(uid);
            if (user == null) {
                map.put("msg","用户不存在");
                return map;
            }
            int start = (page-1)*row;
            List<PickOrder> orderList = sortOrdersService.getAllPickOrders(start,row,state,location,user.getGroupId());
            int total = sortOrdersService.getPickOrdersCount(start,row,state,location,user.getGroupId());
            if (orderList == null) orderList = new ArrayList();
            for (int i = 0; i < orderList.size(); i++) {
                PickOrder order = orderList.get(i);
                List<PickSku> skus = sortOrdersService.getAllPickSkus(order.getId(),user.getGroupId());
                if (skus == null) skus = new ArrayList();
                order.setSkus(skus);

                int state2 = order.getState();
                int lockUid = order.getLockUserId();
                if (state2 == 1){
                    order.setState(2);
                } else if (lockUid != 0){
                    order.setState(3);
                } else {
                    order.setState(1);
                }
            }
            map.put("state",0);
            map.put("total",total);
            map.put("data",orderList);
            map.put("msg","查询成功");

        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }

    @RequestMapping("/getPickLocations")
    @ResponseBody
    public Map getPickLocations(@RequestBody JSONObject jsonObj){
        Map map = new HashMap();
        map.put("state",1);
        try {
            int uid = jsonObj.getInteger("uid");
            User user = UserController.userController.getUser(uid);
            if (user == null) {
                map.put("msg","用户不存在");
                return map;
            }

            List<String> locations = sortOrdersService.getAllPickLocations();
            map.put("state",0);
            map.put("data",locations);
            map.put("msg","查询成功");
        }
        catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }

    @RequestMapping("/updateOrderData")
    @ResponseBody
    public Map updateOrderData(@RequestBody JSONObject jsonObj) throws Exception{
        Map map = new HashMap();

        map.put("state",1);
        try {

            int id = jsonObj.getInteger("id");
            int uid = jsonObj.getInteger("uid");
            JSONArray array = jsonObj.getJSONArray("sku");

            User user = UserController.userController.getUser(uid);
            if (user == null) {
                map.put("msg","用户不存在");
                return map;
            }
            PickOrder pickOrder = sortOrdersService.getPickOrderDetailById(id);

            if (pickOrder.getState() == 1) {
                map.put("msg","订单已完成");
                return map;
            }

            if (pickOrder.getLockUserId() != 0 && pickOrder.getLockUserId() != uid) {
                map.put("msg","订单已被他人锁定");
                return map;
            }
            List<PickSku> skus = sortOrdersService.getAllPickSkus(id,user.getGroupId());

            List<PickSku> unSkus = new ArrayList<>();

            List<PickSku> haveSkus = new ArrayList<>();

            for (PickSku sku:
                    skus) {

                boolean have = false;
                for (int i = 0 ; i < array.size() ; i++) {
                    JSONObject jsku = array.getJSONObject(i);
                    String jseriesNo = jsku.getString("seriesNo");
                    int jcount = jsku.getInteger("count");

                    if (sku.getSeriesNo().equals(jseriesNo)){
                        int count = sku.getCount();
                        sku.setCount(jcount);
                        if (count > jcount) {
                            PickSku unSku = PickSku.NameCopy(sku);
                            unSku.setCount(count - jcount);
                            unSkus.add(unSku);
                        }
                        haveSkus.add(sku);
                        have = true;
                        break;
                    }
                }

                if (!have){
                    sortOrdersService.deletePickSku(sku.getId());
                    unSkus.add(sku);
                }

            }

            sortOrdersService.updatePickOrderState(id,1);
            sortOrdersService.updatePickSkus(haveSkus);
            if (unSkus.size() > 0) {
                PickOrder unpickOrder = sortOrdersService.getUnPickOrder(pickOrder);
                if (unpickOrder == null) {
                    unpickOrder = new PickOrder();
                    unpickOrder.setGroupId(user.getGroupId());
                    unpickOrder.setLocation(pickOrder.getLocation());
                    unpickOrder.setShortName(pickOrder.getShortName());
                    sortOrdersService.adPickOrder(unpickOrder);
                }

                List<PickSku> unpickOrderskus = sortOrdersService.getAllPickSkus(unpickOrder.getId(),user.getGroupId());
                for (PickSku ss:
                        unpickOrderskus) {
                    sortOrdersService.deletePickSku(ss.getId());
                }
                unpickOrder.setSkus(unpickOrderskus);

                for (PickSku addsku:
                unSkus) {
                    int countOrder = unpickOrder.getCount();
                    int countSku = addsku.getCount();

                    while (countSku + countOrder >= pickOrderMaxMun) {

                        PickSku pickskuCopy = PickSku.NameCopy(addsku);
                        pickskuCopy.setCount(countSku + countOrder - pickOrderMaxMun);
                        addsku.setCount(pickOrderMaxMun - countOrder);
                        unpickOrder.addSku(addsku);

                        sortOrdersService.addPickSkus(unpickOrder.getSkus());

                        addsku = pickskuCopy;

                        unpickOrder = new PickOrder();
                        unpickOrder.setGroupId(user.getGroupId());
                        unpickOrder.setLocation(pickOrder.getLocation());
                        unpickOrder.setShortName(pickOrder.getShortName());
                        sortOrdersService.adPickOrder(unpickOrder);
                        countOrder = unpickOrder.getCount();
                        countSku = addsku.getCount();
                    }
                    if (addsku.getCount() > 0) unpickOrder.addSku(addsku);
                }

                if (unpickOrder.getCount() < pickOrderMaxMun) {
                    sortOrdersService.updatePickOrderPickState(unpickOrder.getId(),1);
                }
                sortOrdersService.addPickSkus(unpickOrder.getSkus());
            }

            map.put("state",0);
            map.put("msg","成功");

        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }

    @RequestMapping("/lockPickOrder")
    @ResponseBody
    public Map lockPickOrder(@RequestBody JSONObject jsonObj){
        Map map = new HashMap();
        map.put("state",1);
        try {
            int uid = jsonObj.getInteger("uid");
            int orderId = jsonObj.getInteger("id");
            User user = UserController.userController.getUser(uid);
            if (user == null) {
                map.put("msg","用户不存在");
                return map;
            }

            synchronized(this){
                PickOrder pickOrder = sortOrdersService.getPickOrderDetailById(orderId);
                if (pickOrder == null){
                    map.put("msg","订单不存在");
                }
                else if (pickOrder.getState() == 1) {
                    map.put("msg","订单已完成");
                }
                else
                {
                    if (pickOrder.getLockUserId() != 0) {
                        User userLock = UserController.userController.getUser(pickOrder.getLockUserId());
                        map.put("msg","已被锁定");
                        map.put("data",userLock);
                    } else {
                        int ret = sortOrdersService.lockPickOrderById(orderId,uid);
                        if (ret == 0) {
                            map.put("msg","锁定失败");
                        } else {
                            map.put("state",0);
                            map.put("msg","锁定成功");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }

    @RequestMapping("/unlockPickOrder")
    @ResponseBody
    public Map unlockPickOrder(@RequestBody JSONObject jsonObj) {
        Map map = new HashMap();
        map.put("state",1);
        try {
            int uid = jsonObj.getInteger("uid");
            int orderId = jsonObj.getInteger("id");
            User user = UserController.userController.getUser(uid);
            if (user == null) {

                map.put("msg","用户不存在");
                return map;
            }

            PickOrder pickOrder = sortOrdersService.getPickOrderDetailById(orderId);
            if (pickOrder == null){
                map.put("msg","订单不存在");
            } else {
                if (pickOrder.getLockUserId() == 0) {

                    map.put("msg","没有被锁定");

                } else if (pickOrder.getLockUserId() != uid){
                    User userLock = UserController.userController.getUser(pickOrder.getLockUserId());
                    map.put("msg","锁定人和解锁人不一致");
                    map.put("data",userLock);
                }
                else {
                    int ret = sortOrdersService.lockPickOrderById(orderId,0);
                    if (ret == 0) {
                        map.put("msg","解锁失败");
                    } else {
                        map.put("state",0);
                        map.put("msg","解锁成功");
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }

    @RequestMapping("/searchText")
    @ResponseBody
    public Map searchText(@RequestBody JSONObject jsonObj) {
        Map map = new HashMap();
        map.put("state",1);
        try {
            int uid = jsonObj.getInteger("uid");
            String remake = jsonObj.getString("remake");
            User user = UserController.userController.getUser(uid);
            if (user == null) {
                map.put("msg","用户不存在");
                return map;
            }

            List orders = sortOrdersService.searchRemake(remake,user.getGroupId());
            map.put("orders",orders);
            map.put("msg","成功");
            map.put("state",0);



        } catch (Exception e) {
            // TODO: handle exception
            map.put("msg",e.toString());
        }
        return map;
    }



    @RequestMapping(value = "/uploadOrder")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file
            , HttpServletRequest request, ModelMap model) {

        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return "login";
        }
        String str = sortOrdersService.getSetting("pickOrderMun", group.getId());

        try {
            pickOrderMaxMun = Integer.parseInt(str);
        } catch (Exception e) {
            pickOrderMaxMun = 150;
        }

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

            sortOrdersService.truncateSort(group.getId());
            getExcelInfo(fileName,targetFile,group);

            int orderCount = sortOrdersService.count(group.getId());
            final int rows = 200;
            for (int i = 0 ; i * rows < orderCount; i++){
                List<SortOrders> sortOrders = sortOrdersService.getAll(i * rows,rows,group.getId());
                for (SortOrders order: sortOrders) {
                    int j = 0;
                    String location = null;
                    PickOrder pickorder = null;
                    while (true){
                        List<SortSku> skus = sortOrdersService.getAllSkuCountAndSort(order.getOrderName(),j * rows,rows,group.getId());
                        for (SortSku sku:
                                skus) {
                            PickSku picksku = new PickSku(sku);
                            if (location != null && !location.equals(picksku.getLocation())){
                                if (pickorder.getCount() < pickOrderMaxMun){
                                    sortOrdersService.updatePickOrderPickState(pickorder.getId(),1);
                                }
                                sortOrdersService.addPickSkus(pickorder.getSkus());
                                pickorder = null;
                            }
                            location = picksku.getLocation();
                            if (pickorder == null){
                                pickorder = new PickOrder();
                                pickorder.setGroupId(group.getId());
                                pickorder.setLocation(location);
                                pickorder.setShortName(order.getOrderName());
                                pickorder.setPo(order.getPo());
                                sortOrdersService.adPickOrder(pickorder);
                            }
                            int countOrder = pickorder.getCount();
                            int countSku = picksku.getCount();

                            while (countSku + countOrder >= pickOrderMaxMun) {


                                PickSku pickskuCopy = PickSku.NameCopy(picksku);
                                pickskuCopy.setCount(countSku + countOrder - pickOrderMaxMun);
                                picksku.setCount(pickOrderMaxMun - countOrder);
                                pickorder.addSku(picksku);

                                sortOrdersService.addPickSkus(pickorder.getSkus());

                                picksku = pickskuCopy;

                                pickorder = new PickOrder();
                                pickorder.setGroupId(group.getId());
                                pickorder.setLocation(location);
                                pickorder.setShortName(order.getOrderName());
                                sortOrdersService.adPickOrder(pickorder);
                                countOrder = pickorder.getCount();
                                countSku = picksku.getCount();
                            }
                            if (picksku.getCount() > 0) pickorder.addSku(picksku);

                        }
                        if (skus.size() < rows) {
                            if (pickorder.getCount() < pickOrderMaxMun){
                                sortOrdersService.updatePickOrderPickState(pickorder.getId(),1);
                            }
                            sortOrdersService.addPickSkus(pickorder.getSkus());
                            break;
                        }
                        j++;
                    }

                }
            }

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

    public void getExcelInfo(String fileName, File file, Group group) throws IOException, InvalidFormatException {

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
            sku.setGroupId(group.getId());
            sku.setCalculate(0);
            //循环Excel的列

            String po = "";
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
                        BigDecimal bd = new BigDecimal(str);
                        po = bd.toPlainString();
                    } else if (c == 1) {
                        sku.setOrderName(str);
                        if (order == null || !order.getOrderName().equalsIgnoreCase(str)) {
                            order = new SortOrders();
                            order.setOrderName(str);
                            order.setPo(po);
                            order.setGroupId(group.getId());
                            if (str != null
                                    && !str.trim().isEmpty()) {
                                sortOrdersService.deleteSku(order.getOrderName(),group.getId());
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

    @RequestMapping("/truncateOrder")
    @ResponseBody
    public int truncate(HttpServletRequest request) {
        try {

            Group group = (Group) request.getSession().getAttribute("group");
            if (group != null && group.getId() != 0) {
                int ret = sortOrdersService.truncate(group.getId());
                System.out.println("truncateOrder : ret" + ret);
                if (ret != 0){
                    return 1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping("/truncateZero")
    @ResponseBody
    public int truncateZero(HttpServletRequest request) {
        try {

            Group group = (Group) request.getSession().getAttribute("group");
            if (group != null && group.getId() != 0 && (sortOrdersService.allOrderCount() == 0)) {
                int ret = sortOrdersService.truncateZero();
                System.out.println("truncateOrder : ret" + ret);
                if (ret != 0){
                    return 1;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    @RequestMapping("/exportSortOrder")
    public String exportSortOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Group group = (Group) request.getSession().getAttribute("group");
        if (group != null && group.getId() != 0) {

        } else {
            return "login";
        }
        XSSFWorkbook webBook = new XSSFWorkbook();
        XSSFSheet sheet = webBook.createSheet("订单数据");
        XSSFRow row = sheet.createRow((int)0);

        XSSFCellStyle style = webBook.createCellStyle();
        String[] strs = {"PO单号","条形码","数量","系统订单编码","拣货单","产品名称","规格","订单状态","地址","备注1","备注2"};
        for (int i = 0; i < strs.length; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(strs[i]);
            cell.setCellStyle(style);
        }

        int count = sortOrdersService.selectSkUCount(group.getId());
        int rows = 2000;
        int r = 1;
        for (int i=0 ;i*rows < count ; i++) {
            List<PickSku> goodsList = sortOrdersService.getAllPickSku(i*rows,rows,group.getId());

            for (int j=0;j<goodsList.size();j++){
                row = sheet.createRow(r);
                r++;

                PickSku goods = goodsList.get(j);
                row.createCell(0).setCellValue(goods.getPo());
                row.createCell(1).setCellValue(goods.getSeriesNo());
                row.createCell(2).setCellValue(goods.getCount());
                row.createCell(3).setCellValue(goods.getPickOrderId());
                row.createCell(4).setCellValue(goods.getShortName());
                row.createCell(5).setCellValue(goods.getName());
                row.createCell(6).setCellValue(goods.getGoodSize());
                String state = "未开始";
                if (goods.getState() == 1){
                    state = "已完成";
                } else if (goods.getLockUserId() != 0){
                    state = "分拣中";
                }
                row.createCell(7).setCellValue(state);
                row.createCell(8).setCellValue(goods.getLocation());
                row.createCell(9).setCellValue(goods.getRemarks1());
                row.createCell(10).setCellValue(goods.getRemarks2());

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


    @RequestMapping(value = "/savePickOrderMun", method = RequestMethod.POST)
    @ResponseBody
    public int saveUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            Group group = (Group) request.getSession().getAttribute("group");
            if (group != null && group.getId() != 0) {

                String pickOrderMun = request.getParameter("pickOrderMun");

                System.out.println("savePickOrderMun : " + pickOrderMun);
                int mun = Integer.parseInt(pickOrderMun);
                if (mun > 0 && mun < 1000000) {
                    int ret = sortOrdersService.setSetting("pickOrderMun",pickOrderMun,group.getId());

                    //System.out.println("savePickOrderMun : get" + sortOrdersService.getSetting("pickOrderMun"));
                    if (ret != 0){
                        return 1;
                    }
                }
            }


        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }
}
