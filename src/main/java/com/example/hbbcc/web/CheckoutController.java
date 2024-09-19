package com.example.hbbcc.web;

import com.example.hbbcc.service.CheckoutService;
import com.example.hbbcc.service.exception.ProductNotFoundException;
import com.example.hbbcc.web.model.CheckoutResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CheckoutResponse checkout(@RequestBody LinkedList<String> productIdList) {
        List<Long> productIds = productIdList.stream().map(Long::valueOf).toList();
        Optional<BigDecimal> total;
        try {
            total = checkoutService.checkout(productIds);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
        return total.map(CheckoutResponse::new).orElseGet(CheckoutResponse::new);
    }

}
