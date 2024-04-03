package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.stock.GetAllStocksInput;
import com.example.restaurantrestful.dto.inputs.stock.GetStocksByIngredientIdInput;
import com.example.restaurantrestful.dto.payloads.StockPayload;
import com.example.restaurantrestful.entity.Stock;
import com.example.restaurantrestful.enums.SortBy;
import com.example.restaurantrestful.enums.UnitTypeEnums;
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
class StockServiceTest {

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

        CustomException result = assertThrows(CustomException.class, () -> stockServiceMock.getStockById("test"));

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

    @DisplayName("getStocksByIngredientId should return page stock when getAllStocksInput given input")
    @Test
    void testGetAllStocks_success() {
        GetAllStocksInput getAllStocks = new GetAllStocksInput(1, 1, SortBy.ASC, "create_date");
        List<Stock> stockList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "create_date");
        stockList.add(stockMock);
        Page<Stock> stocksPage = new PageImpl<>(stockList, pageable, stockList.size());

        when(stockRepositoryMock.findAll(pageable)).thenReturn(stocksPage);

        Page<Stock> result = stockServiceMock.getAllStocks(getAllStocks);

        assertNotNull(result);
        assertEquals(stocksPage.getTotalElements(), result.getTotalElements());
    }

    @DisplayName("updateStockQuantity should return valid stockPayload when given id exist and quantity greather zero")
    @Test
    void testUpdateStockQuantity_successCaseKG() {
        String test_id = "test_id";
        Double quantity = 100.0;
        stockMock.setUnit(UnitTypeEnums.GR.toString());
        stockMock.setQuantity(900.0);

        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.ofNullable(stockMock));

        StockPayload result = stockServiceMock.updateStockQuantity(test_id, quantity);

        assertEquals(test_id, result.getId());
        assertEquals(UnitTypeEnums.KG.toString(), result.getUnit());
    }

    @DisplayName("updateStockQuantity should return valid stockPayload when given id exist and quantity greather zero")
    @Test
    void testUpdateStockQuantity_successCaseGR() {
        String test_id = "test_id";
        Double quantity = -0.01;
        stockMock.setQuantity(1.0);

        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.ofNullable(stockMock));

        StockPayload result = stockServiceMock.updateStockQuantity(test_id, quantity);

        assertEquals(test_id, result.getId());
        assertEquals(UnitTypeEnums.GR.toString(), result.getUnit());
    }

    @DisplayName("updateStockQuantity should return valid stockPayload when given id exist and quantity greather zero")
    @Test
    void testUpdateStockQuantity_successCaseLT() {
        String test_id = "test_id";
        Double quantity = 1.0;
        stockMock.setUnit(UnitTypeEnums.MIL.toString());
        stockMock.setQuantity(1000.0);

        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.ofNullable(stockMock));

        StockPayload result = stockServiceMock.updateStockQuantity(test_id, quantity);

        assertEquals(test_id, result.getId());
        assertEquals(UnitTypeEnums.LT.toString(), result.getUnit());

    }

    @DisplayName("updateStockQuantity should return valid stockPayload when given id exist and quantity greather zero")
    @Test
    void testUpdateStockQuantity_successCaseMIL() {
        String test_id = "test_id";
        Double quantity = -0.01;
        stockMock.setUnit(UnitTypeEnums.LT.toString());
        stockMock.setQuantity(1.0);

        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.ofNullable(stockMock));

        StockPayload result = stockServiceMock.updateStockQuantity(test_id, quantity);

        assertEquals(test_id, result.getId());
        assertEquals(UnitTypeEnums.MIL.toString(), result.getUnit());
    }



    @DisplayName("updateStockQuantity should throw custom exception stock not found exception when given id does not exist")
    @Test
    void testUpdateStockQuantity_stockNotFound() {
        String test_id = "test_id";
        Double quantity = 100.0;


        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> stockServiceMock.updateStockQuantity(test_id, quantity));

        assertEquals("Stock not found", exception.getMessage());
    }

    @DisplayName("updateStockQuantity should throw illegal argument exception when stock id is exist but quantity value equals zero or less than zero")
    @Test
    void testUpdateStockQuantity_illegalArgumentException() {
        String test_id = "test_id";
        Double quantity = 0.0;

        when(stockRepositoryMock.findByIdAndIsDeletedFalse(test_id)).thenReturn(Optional.ofNullable(stockMock));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> stockServiceMock.updateStockQuantity(test_id, quantity));

        assertEquals("Quantity must be greater or less than 0", exception.getMessage());
    }
}
