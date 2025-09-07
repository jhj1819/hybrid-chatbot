package com.example.hybridchatbot.controller;

import com.example.hybridchatbot.dto.NaverShoppingResponse;
import com.example.hybridchatbot.entity.NaverShoppingItem;
import com.example.hybridchatbot.entity.NaverShoppingSearch;
import com.example.hybridchatbot.service.NaverShoppingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NaverShoppingDataController.class)
class NaverShoppingDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NaverShoppingService naverShoppingService;

    @Autowired
    private ObjectMapper objectMapper;

    private NaverShoppingItem testItem;
    private NaverShoppingResponse testResponse;

    @BeforeEach
    void setUp() {
        testItem = createTestItem();
        testResponse = createTestResponse();
    }

    @Test
    void testSearchAndSaveProducts() throws Exception {
        // Given
        when(naverShoppingService.searchAndSaveProducts(anyString(), anyInt(), anyInt()))
                .thenReturn(testResponse);

        // When & Then
        mockMvc.perform(post("/api/naver-shopping/search-and-save")
                        .param("query", "아이폰")
                        .param("display", "10")
                        .param("start", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].title").value("아이폰 15"));

        verify(naverShoppingService).searchAndSaveProducts("아이폰", 10, 1);
    }

    @Test
    void testSearchProducts() throws Exception {
        // Given
        when(naverShoppingService.searchProducts(anyString(), anyInt(), anyInt()))
                .thenReturn(testResponse);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/search")
                        .param("query", "아이폰")
                        .param("display", "10")
                        .param("start", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].title").value("아이폰 15"));

        verify(naverShoppingService).searchProducts("아이폰", 10, 1);
    }

    @Test
    void testGetSavedProductsByQuery() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByQuery(anyString()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products")
                        .param("query", "아이폰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("아이폰 15"))
                .andExpect(jsonPath("$[0].lprice").value(1000000));

        verify(naverShoppingService).getSavedProductsByQuery("아이폰");
    }

    @Test
    void testSearchSavedProductsByTitle() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByTitle(anyString()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/search")
                        .param("keyword", "아이폰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("아이폰 15"));

        verify(naverShoppingService).getSavedProductsByTitle("아이폰");
    }

    @Test
    void testGetSavedProductsByPriceRange() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByPriceRange(anyInt(), anyInt()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/price-range")
                        .param("minPrice", "500000")
                        .param("maxPrice", "1500000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lprice").value(1000000));

        verify(naverShoppingService).getSavedProductsByPriceRange(500000, 1500000);
    }

    @Test
    void testGetSavedProductsByCategory() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByCategory(anyString()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/category")
                        .param("category1", "디지털/가전"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category1").value("디지털/가전"));

        verify(naverShoppingService).getSavedProductsByCategory("디지털/가전");
    }

    @Test
    void testGetSavedProductsByMall() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByMall(anyString()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/mall")
                        .param("mallName", "테스트몰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mallName").value("테스트몰"));

        verify(naverShoppingService).getSavedProductsByMall("테스트몰");
    }

    @Test
    void testGetSavedProductsByBrand() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getSavedProductsByBrand(anyString()))
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/brand")
                        .param("brand", "Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Apple"));

        verify(naverShoppingService).getSavedProductsByBrand("Apple");
    }

    @Test
    void testGetAllSavedProducts() throws Exception {
        // Given
        List<NaverShoppingItem> savedItems = Arrays.asList(testItem);
        when(naverShoppingService.getAllSavedProducts())
                .thenReturn(savedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/saved-products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("아이폰 15"));

        verify(naverShoppingService).getAllSavedProducts();
    }

    @Test
    void testGetSearchHistory() throws Exception {
        // Given
        NaverShoppingSearch searchRecord = createTestSearchRecord();
        List<NaverShoppingSearch> searchHistory = Arrays.asList(searchRecord);
        when(naverShoppingService.getSearchHistory())
                .thenReturn(searchHistory);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/search-history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].query").value("아이폰"))
                .andExpect(jsonPath("$[0].totalResults").value(100));

        verify(naverShoppingService).getSearchHistory();
    }

    @Test
    void testGetSearchHistoryByQuery() throws Exception {
        // Given
        NaverShoppingSearch searchRecord = createTestSearchRecord();
        List<NaverShoppingSearch> searchHistory = Arrays.asList(searchRecord);
        when(naverShoppingService.getSearchHistoryByQuery(anyString()))
                .thenReturn(searchHistory);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/search-history/query")
                        .param("query", "아이폰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].query").value("아이폰"));

        verify(naverShoppingService).getSearchHistoryByQuery("아이폰");
    }

    @Test
    void testGetDistinctMallNames() throws Exception {
        // Given
        List<String> mallNames = Arrays.asList("테스트몰1", "테스트몰2");
        when(naverShoppingService.getDistinctMallNames())
                .thenReturn(mallNames);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/malls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("테스트몰1"))
                .andExpect(jsonPath("$[1]").value("테스트몰2"));

        verify(naverShoppingService).getDistinctMallNames();
    }

    @Test
    void testGetDistinctBrands() throws Exception {
        // Given
        List<String> brands = Arrays.asList("Apple", "Samsung");
        when(naverShoppingService.getDistinctBrands())
                .thenReturn(brands);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Apple"))
                .andExpect(jsonPath("$[1]").value("Samsung"));

        verify(naverShoppingService).getDistinctBrands();
    }

    @Test
    void testGetDistinctCategory1() throws Exception {
        // Given
        List<String> categories = Arrays.asList("디지털/가전", "패션/의류");
        when(naverShoppingService.getDistinctCategory1())
                .thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("디지털/가전"))
                .andExpect(jsonPath("$[1]").value("패션/의류"));

        verify(naverShoppingService).getDistinctCategory1();
    }

    @Test
    void testGetSearchCountByQuery() throws Exception {
        // Given
        when(naverShoppingService.getSearchCountByQuery(anyString()))
                .thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/search-count")
                        .param("query", "아이폰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.query").value("아이폰"))
                .andExpect(jsonPath("$.count").value(5));

        verify(naverShoppingService).getSearchCountByQuery("아이폰");
    }

    @Test
    void testGetDatabaseStats() throws Exception {
        // Given
        Map<String, Object> stats = Map.of(
                "totalProducts", 100L,
                "totalSearches", 50L,
                "totalMalls", 10L,
                "totalBrands", 5L,
                "totalCategories", 3L
        );
        when(naverShoppingService.getAllSavedProducts()).thenReturn(Arrays.asList(testItem));
        when(naverShoppingService.getSearchHistory()).thenReturn(Arrays.asList(createTestSearchRecord()));
        when(naverShoppingService.getDistinctMallNames()).thenReturn(Arrays.asList("몰1", "몰2"));
        when(naverShoppingService.getDistinctBrands()).thenReturn(Arrays.asList("브랜드1"));
        when(naverShoppingService.getDistinctCategory1()).thenReturn(Arrays.asList("카테고리1"));

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProducts").value(1))
                .andExpect(jsonPath("$.totalSearches").value(1))
                .andExpect(jsonPath("$.totalMalls").value(2))
                .andExpect(jsonPath("$.totalBrands").value(1))
                .andExpect(jsonPath("$.totalCategories").value(1));
    }

    @Test
    void testGetUpdateStats() throws Exception {
        // Given
        Map<String, Object> updateStats = Map.of(
                "totalItems", 10L,
                "updatedItems", 5L,
                "newItems", 5L,
                "updateRate", 50.0
        );
        when(naverShoppingService.getUpdateStats())
                .thenReturn(updateStats);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/update-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(10))
                .andExpect(jsonPath("$.updatedItems").value(5))
                .andExpect(jsonPath("$.newItems").value(5))
                .andExpect(jsonPath("$.updateRate").value(50.0));

        verify(naverShoppingService).getUpdateStats();
    }

    @Test
    void testGetRecentlyUpdatedProducts() throws Exception {
        // Given
        List<NaverShoppingItem> recentItems = Arrays.asList(testItem);
        when(naverShoppingService.getRecentlyUpdatedProducts(anyInt()))
                .thenReturn(recentItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/recently-updated")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("아이폰 15"));

        verify(naverShoppingService).getRecentlyUpdatedProducts(10);
    }

    @Test
    void testGetMostSearchedProducts() throws Exception {
        // Given
        List<NaverShoppingItem> mostSearchedItems = Arrays.asList(testItem);
        when(naverShoppingService.getMostSearchedProducts(anyInt()))
                .thenReturn(mostSearchedItems);

        // When & Then
        mockMvc.perform(get("/api/naver-shopping/most-searched")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("아이폰 15"));

        verify(naverShoppingService).getMostSearchedProducts(10);
    }

    @Test
    void testForceUpdateProduct() throws Exception {
        // Given
        when(naverShoppingService.forceUpdateProduct(anyString(), anyString()))
                .thenReturn(testItem);

        // When & Then
        mockMvc.perform(post("/api/naver-shopping/force-update/product1")
                        .param("searchQuery", "아이폰"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("test-product-1"))
                .andExpect(jsonPath("$.title").value("아이폰 15"));

        verify(naverShoppingService).forceUpdateProduct("product1", "아이폰");
    }

    @Test
    void testForceUpdateProduct_NotFound() throws Exception {
        // Given
        when(naverShoppingService.forceUpdateProduct(anyString(), anyString()))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(post("/api/naver-shopping/force-update/nonexistent")
                        .param("searchQuery", "아이폰"))
                .andExpect(status().isNotFound());

        verify(naverShoppingService).forceUpdateProduct("nonexistent", "아이폰");
    }

    private NaverShoppingItem createTestItem() {
        return NaverShoppingItem.builder()
                .id(1L)
                .productId("test-product-1")
                .title("아이폰 15")
                .lprice(1000000)
                .hprice(1100000)
                .link("https://example.com/product1")
                .image("https://example.com/image1.jpg")
                .mallName("테스트몰")
                .productType("1")
                .brand("Apple")
                .maker("Apple Inc.")
                .category1("디지털/가전")
                .category2("휴대폰")
                .category3("스마트폰")
                .category4("아이폰")
                .searchQuery("아이폰")
                .searchCount(1)
                .lastSearchedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private NaverShoppingResponse createTestResponse() {
        NaverShoppingResponse response = new NaverShoppingResponse();
        response.setTotal(1);
        response.setStart(1);
        response.setDisplay(10);
        response.setLastBuildDate("2024-01-01T00:00:00");

        NaverShoppingResponse.Item item = new NaverShoppingResponse.Item();
        item.setProductId("test-product-1");
        item.setTitle("아이폰 15");
        item.setLprice(1000000);
        item.setHprice(1100000);
        item.setLink("https://example.com/product1");
        item.setImage("https://example.com/image1.jpg");
        item.setMallName("테스트몰");
        item.setProductType("1");
        item.setBrand("Apple");
        item.setMaker("Apple Inc.");
        item.setCategory1("디지털/가전");
        item.setCategory2("휴대폰");
        item.setCategory3("스마트폰");
        item.setCategory4("아이폰");

        response.setItems(Arrays.asList(item));
        return response;
    }

    private NaverShoppingSearch createTestSearchRecord() {
        return NaverShoppingSearch.builder()
                .id(1L)
                .query("아이폰")
                .totalResults(100)
                .displayCount(10)
                .startIndex(1)
                .lastBuildDate("2024-01-01T00:00:00")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
