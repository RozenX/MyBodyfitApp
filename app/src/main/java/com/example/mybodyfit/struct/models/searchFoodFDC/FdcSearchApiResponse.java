package com.example.mybodyfit.struct.models.searchFoodFDC;

import java.util.ArrayList;

public class FdcSearchApiResponse {

    public int totalHits;
    public int currentPage;
    public int totalPages;
    public ArrayList<Integer> pageList;
    public FoodSearchCriteria foodSearchCriteria;
    public ArrayList<Food> foods;
}

