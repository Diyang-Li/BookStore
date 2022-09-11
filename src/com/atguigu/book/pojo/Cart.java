package com.atguigu.book.pojo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * @author Diyang Li
 * @create 2022-05-13 4:08 PM
 */
public class Cart {
    private Map<Integer, CartItem> cartItemMap;
    private double totalMoney;
    private Integer totalCount;
    private Integer totalBookCount;

    public Cart() {
    }

    public Map<Integer, CartItem> getCartItemMap() {
        return cartItemMap;
    }

    public void setCartItemMap(Map<Integer, CartItem> cartItemMap) {
        this.cartItemMap = cartItemMap;
    }

    public double getTotalMoney() {
        totalMoney = 0.0;
        if (cartItemMap != null && cartItemMap.size() > 0) {
            Set<Map.Entry<Integer, CartItem>> cartItemEntry = cartItemMap.entrySet();
            for (Map.Entry<Integer, CartItem> entries : cartItemEntry) {
                CartItem cartItem = entries.getValue();
                BigDecimal bookPriceDecimal = new BigDecimal("" + cartItem.getBook().getPrice());
                BigDecimal buyCountDecimal = new BigDecimal(""+cartItem.getBuyCount());
                Double totalDecimal = bookPriceDecimal.multiply(buyCountDecimal).doubleValue();
                totalMoney += totalDecimal;

            }
        }
        return totalMoney;
    }

    public Integer getTotalCount() {
        totalCount = 0;
        if (cartItemMap != null & cartItemMap.size() > 0) {
            totalCount = cartItemMap.size();
        }
        return totalCount;
    }

    public Integer getTotalBookCount() {
        totalBookCount = 0;
        if (cartItemMap != null & cartItemMap.size() > 0) {
            for (CartItem cartItem: cartItemMap.values()){
                totalBookCount+=cartItem.getBuyCount();
            }
        }
        return totalBookCount;
    }
}
