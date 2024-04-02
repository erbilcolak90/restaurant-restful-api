package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.stock.GetStocksByIngredientIdInput;
import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.enums.SortBy;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.StockRepository;
import com.example.restaurantrestful.service.IngredientService;
import com.example.restaurantrestful.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @InjectMocks
    private StockService stockServiceMock;

    @Mock
    private StockRepository stockRepositoryMock;

    @Mock
    private IngredientService ingredientService;

    private Stock stockMock;

    @BeforeEach
    void setUp() {

        stockMock = new Stock();
        stockMock.setId("test_id");
        stockMock.setIngredientId("test_ingredient_id");
        stockMock.setType("BAKERY");
        stockMock.setUnit("KG");
        stockMock.setQuantity(100.0);
        stockMock.setExpireDate(new Date());

    }

    @DisplayName("getStockById should return valid stockPayload with given id input")
    @Test
    void testGetStockById_success() {
        String test_id = "test_id";

        when(stockRepositoryMock.findById(anyString())).thenReturn(Optional.ofNullable(stockMock));

        StockPayload result = stockServiceMock.getStockById(test_id);

        assertNotNull(result);
        assertEquals("test_id", result.getId());
    }

    @DisplayName("getStockById should throw custom exception stock not found with given id input")
    @Test
    void testGetStockById_stockNotFound() {
        when(stockRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

        CustomException result = assertThrows(CustomException.class, () -> stockServiceMock.getStockById(anyString()));

        assertEquals("Stock not found", result.getMessage());
    }

    @DisplayName("getStocksByIngredientId should return page stockpayload when getStocksByIdInput given input")
    @Test
    void testGetStocksByIngredientId_success() {
        GetStocksByIngredientIdInput getStocksByIngredientIdInput = new GetStocksByIngredientIdInput(1, 1, SortBy.ASC, "create_date", "test_id");
        List<Stock> stockList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "create_date");
        stockList.add(stockMock);
        Page<Stock> stocksPage = new PageImpl<>(stockList, pageable, stockList.size());
        Page<StockPayload> stockPayloadsPage = stocksPage.map(stock -> StockPayload.convert(stockMock));
        when(stockRepositoryMock.findByIngredientIdAndIsDeletedFalse(getStocksByIngredientIdInput.getIngredientId(), pageable)).thenReturn(stocksPage);

        Page<StockPayload> result = stockServiceMock.getStocksByIngredientId(getStocksByIngredientIdInput);

        assertNotNull(result);
        assertEquals(stockPayloadsPage.getTotalElements(), result.getTotalElements());
    }


}
