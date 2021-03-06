package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 品牌的FeignClient
 */
@FeignClient(value = "item-service")
public interface BrandClient extends BrandApi {
}