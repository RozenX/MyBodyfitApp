package com.example.mybodyfit.struct.Listeners;

import com.example.mybodyfit.struct.models.searchFoodFDC.FdcSearchApiResponse;

public interface FdcFoodSearchResponseListener {

    void didFetch(FdcSearchApiResponse response, String msg);

    void didError(String msg);
}
