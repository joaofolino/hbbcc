package com.example.hbbcc.web;

import com.example.hbbcc.service.CheckoutService;
import com.example.hbbcc.service.exception.ProductNotFoundException;
import com.example.hbbcc.web.model.CheckoutResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CheckoutControllerTest {

    @MockBean
    CheckoutService checkoutService;

    @Captor
    ArgumentCaptor<List<Long>> productIdParseCaptor;

    @Test void whenValidCheckoutRequestReceived_containingValidProductIdList_expectValidResponse_containingPrice() throws ProductNotFoundException {
        LinkedList<String> productIdList = new LinkedList<String>(List.of("001", "002", "001", "004", "003"));

        when(checkoutService.checkout(anyList())).thenReturn(Optional.of(new BigDecimal(360)));
        CheckoutController checkoutController = new CheckoutController(checkoutService);
        CheckoutResponse checkoutResponse = checkoutController.checkout(productIdList);

        assertThat(checkoutResponse.getPrice()).isEqualTo(new BigDecimal(360));
    }

    @Test void whenValidCheckoutRequestReceived_containingEmptyProductIdList_expectValidResponse_containingPrice() throws ProductNotFoundException {
        LinkedList<String> productIdList = new LinkedList<String>(List.of("001", "002", "001", "004", "003"));

        when(checkoutService.checkout(anyList())).thenReturn(Optional.empty());
        CheckoutController checkoutController = new CheckoutController(checkoutService);
        CheckoutResponse checkoutResponse = checkoutController.checkout(productIdList);

        assertThat(checkoutResponse.getPrice()).isNull();
    }

    @Test void whenValidCheckoutRequestReceived_containingValidProductIdList_expectCorrectInputParsing() throws ProductNotFoundException {
        LinkedList<String> productIdList = new LinkedList<String>(List.of("001", "002", "001", "004", "003"));
        List<Long> expectedParsedProductIds = List.of(1L, 2L, 1L, 4L, 3L);

        CheckoutController checkoutController = new CheckoutController(checkoutService);
        checkoutController.checkout(productIdList);

        Mockito.verify(checkoutService).checkout(productIdParseCaptor.capture());
        List<Long> parsedProductIds = productIdParseCaptor.getValue();
        assertThat(parsedProductIds).containsExactlyInAnyOrderElementsOf(expectedParsedProductIds);
    }
}
