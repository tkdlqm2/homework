package com.service.service.purchase.impl;

import com.service.database.entity.comic.Comic;
import com.service.database.entity.purchase.Purchase;
import com.service.database.entity.user.User;
import com.service.database.entity.user.UserSubscribeComic;
import com.service.database.repository.comic.ComicRepository;
import com.service.database.repository.purchase.PurchaseRepository;
import com.service.database.repository.user.UserRepository;
import com.service.database.repository.user.UserSubscribeComicRepository;
import com.service.dto.request.PurchaseComicRequestDto;
import com.service.dto.response.PurchaseComicResponseDto;
import com.service.exception.comic.ComicErrorCode;
import com.service.exception.comic.ComicException;
import com.service.exception.purchase.PurchaseErrorCode;
import com.service.exception.purchase.PurchaseException;
import com.service.exception.user.UserErrorCode;
import com.service.exception.user.UserException;
import com.service.service.purchase.PurchaseManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseManagerServiceImpl implements PurchaseManagerService {

    private final ComicRepository comicRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserSubscribeComicRepository userSubscribeComicRepository;
    private final PurchaseCountServiceImpl purchaseCountService;

    @Transactional
    @Override
    public PurchaseComicResponseDto purchaseComic(PurchaseComicRequestDto purchaseComicRequestDto) {

        // 1. 조회 및 검증
        User user = userRepository.findById(purchaseComicRequestDto.getUserId())
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        Comic comic = comicRepository.findById(purchaseComicRequestDto.getComicId())
                .orElseThrow(() -> new ComicException(ComicErrorCode.NOT_FOUND_COMIC));

        // 2. 이미 구독 중인지 확인
        if (userSubscribeComicRepository.existsByUserIdAndComicId(user.getId(), comic.getId())) {
            throw new PurchaseException(PurchaseErrorCode.DUPLICATE_PURCHASE);
        }

        // 3. 연관 엔티티 생성 및 저장
        UserSubscribeComic subscription = UserSubscribeComic.createSubscription(user, comic);
        userSubscribeComicRepository.save(subscription);

        // 4. 구매 정보 생성 및 저장
        Purchase purchase = Purchase.builder()
                .purchasedAt(LocalDate.now())
                .price(comic.getPrice())
                .comic(comic)
                .user(user)
                .build();

        Purchase savedPurchase = purchaseRepository.save(purchase);

        // 5. 양쪽 연관관계 맵핑
        comic.addSubscriber(subscription);
        user.addSubscription(subscription);
        comicRepository.save(comic);

        // 6. 구매건 캐싱
        purchaseCountService.incrementPurchaseCount(comic);

        return PurchaseComicResponseDto.builder()
                .purchaseId(savedPurchase.getId())
                .build();
    }
}
