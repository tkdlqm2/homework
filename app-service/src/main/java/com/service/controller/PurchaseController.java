package com.service.controller;

import com.service.dto.request.PurchaseComicRequestDto;
import com.service.dto.response.AggregatePurchaseCountResponseDto;
import com.service.dto.response.GetPopularComicByPurchasesResponseDto;
import com.service.dto.response.PurchaseComicResponseDto;
import com.service.dto.response.ViewAggreateResponseDto;
import com.service.service.purchase.PurchaseCountService;
import com.service.service.purchase.PurchaseManagerService;
import com.service.service.purchase.impl.PurchaseCountServiceImpl;
import com.service.service.purchase.impl.PurchaseQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Tag(name = "purchase API", description = "purchase API")
@RestController  // 추가
@RequestMapping("/api/v1/purchase")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

    private final PurchaseQueryServiceImpl purchaseQueryService;
    private final PurchaseManagerService purchaseManagerService;
    private final PurchaseCountService purchaseCountService;

    @Operation(summary = "구매순 인기 작품 목록 조회", description = "구매순으로 인기 작품 목록을 조회합니다")
    @GetMapping("/popular/purchases")
    public ResponseEntity<GetPopularComicByPurchasesResponseDto> getPopularComicsByPurchases() {
        return ResponseEntity.ok(purchaseQueryService.getPopularComicsByPurchases());
    }

    @Operation(summary = "작품 구매", description = "작품을 구매합니다")
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseComicResponseDto> purchaseComic(
            @RequestBody @Valid PurchaseComicRequestDto request) {
        return ResponseEntity.ok(purchaseManagerService.purchaseComic(request));
    }
    @Operation(summary = "구매수 어그리게이트 집계", description = "코믹 구매수 데이터를 집계합니다")
    @GetMapping("/aggregate-views-stats")
    public ResponseEntity<AggregatePurchaseCountResponseDto> aggregatePurchasesCountStats() {
        return ResponseEntity.ok(AggregatePurchaseCountResponseDto.builder().result(purchaseCountService.aggregatePurchaseCount()).build());
    }

}
