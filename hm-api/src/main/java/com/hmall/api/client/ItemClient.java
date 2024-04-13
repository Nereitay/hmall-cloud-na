package com.hmall.api.client;

import com.hmall.api.client.fallback.ItemClientFallback;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
/*步骤三：在hm-api模块中的ItemClient接口中使用ItemClientFallbackFactory*/
@FeignClient(value = "item-service", fallbackFactory = ItemClientFallback.class)
public interface ItemClient { // @RequestMapping annotation not allowed on @FeignClient interfaces

    @GetMapping("/items")
    List<ItemDTO> queryItemByIds(@RequestParam("ids") Collection<Long> ids);

    @PutMapping("/items/stock/deduct")
    @TimeLimiter(name = "tlApi")
    @Retry(name = "retryApi")
    @CircuitBreaker(name = "cbApi")
    void deductStock(@RequestBody List<OrderDetailDTO> items);
}
