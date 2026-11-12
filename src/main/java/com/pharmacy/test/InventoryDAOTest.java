package com.pharmacy.test;

import com.pharmacy.dao.InventoryDAO;

import java.util.List;
import java.util.Map;

public class InventoryDAOTest {

    public static void main(String[] args) {

        InventoryDAO inventoryDAO = new InventoryDAO();

        List<Map<String, Object>> inventory =
                inventoryDAO.getAllInventory();

        System.out.println("Inventory records: " + inventory.size());

        for (Map<String, Object> row : inventory) {
            System.out.println(
                    row.get("medicine_name")
                            + " | Batch: "
                            + row.get("batch_number")
                            + " | Quantity: "
                            + row.get("quantity")
                            + " | Purchase Price: ₹"
                            + row.get("purchase_price")
                            + " | Selling Price: ₹"
                            + row.get("selling_price")
            );
        }
    }
}
// if(n<4) return 0;
//int digitCount = 0;
//int countUpper = 0;
//
//        for(int i =0; i<n; i++){
//        if(i==0 && (str[i]>='0'&&str[i]<='9')){
//        return 0;
//        }
//        if(str[i] == ' '||str[i]=='/'){
//        return 0;
//        }
//        if(str[i]>='0'&&str[i]<='9') digitCount++;
//        if(str[i]>='A'&&str[i]<='Z') countUpper++;
//        }
//        if(digitCount == 0 || countUpper ==0) return 0;
//
//        return 1;