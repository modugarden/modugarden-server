package com.modugarden.domain.curation.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.s3.FileService;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.auth.entity.ModugardenUser;
import com.modugarden.domain.category.repository.InterestCategoryRepository;
import com.modugarden.domain.curation.dto.request.CurationCreateRequestDto;
import com.modugarden.domain.curation.dto.request.CurationLikeRequestDto;
import com.modugarden.domain.curation.dto.response.*;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.curation.repository.CurationRepository;
import com.modugarden.domain.like.repository.LikeRepository;
import com.modugarden.domain.storage.entity.CurationStorage;
import com.modugarden.domain.storage.entity.repository.CurationStorageRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final FileService fileService;
    private final CurationStorageRepository curationStorageRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    //큐레이션 생성
    @Transactional
    public CurationCreateResponseDto createCuration(CurationCreateRequestDto createRequestDto, MultipartFile file, ModugardenUser user) throws IOException {

        if (file.isEmpty())
            throw new IOException(new BusinessException(ErrorMessage.WRONG_CURATION_FILE));

        InterestCategory interestCategory = interestCategoryRepository.findByCategory(createRequestDto.getCategory()).get();
        String profileImageUrl = fileService.uploadFile(file, user.getUserId(), "curationImage");

        Curation curation = Curation.builder()
                .title(createRequestDto.getTitle())
                .link(createRequestDto.getLink())
                .previewImage(profileImageUrl)
                .user(user.getUser())
                .likeNum((long) 0)
                .category(interestCategory)
                .build();

        return new CurationCreateResponseDto(curationRepository.save(curation).getId());
    }

    //큐레이션 좋아요 달기
    @Transactional
    public CurationLikeResponseDto createLikeCuration(Long curation_id, ModugardenUser user) {
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        if (!likeRepository.findByUserAndCuration(users, curation).isPresent()) {
            Curation modifyCuration = new Curation(curation.getId(), curation.getTitle(), curation.getLink(), curation.getPreviewImage(), curation.getLikeNum() + 1, curation.getUser(), curation.getCategory());
            CurationLikeRequestDto curationLikeRequestDto = new CurationLikeRequestDto(users, modifyCuration);

            likeRepository.save(curationLikeRequestDto.toEntity());
            curationRepository.save(modifyCuration);
        }
        return new CurationLikeResponseDto(curation.getId(), curation.getLikeNum());
    }

    //큐레이션 보관
    public CurationStorageResponseDto storeCuration(ModugardenUser user, Long curation_id) {
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        if(curationStorageRepository.findByUserAndCuration(user.getUser(),curation).isPresent())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_STORAGE);

        CurationStorage curationStorage = new CurationStorage(user.getUser(), curation);
        curationStorageRepository.save(curationStorage);
        return new CurationStorageResponseDto(curationStorage.getUser().getId(),curationStorage.getCuration().getId());
    }

    //큐레이션 하나 조회 api
    public CurationGetResponseDto getCuration(long id) {
        Curation curation = curationRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        return new CurationGetResponseDto(curation);
    }

    //회원 큐레이션 조회
    public Slice<CurationUserGetResponseDto> getUserCuration(long user_id, Pageable pageable) {
        Slice<Curation> userCurationList = curationRepository.findAllByUser_Id(user_id, pageable);
        return userCurationList.map(CurationUserGetResponseDto::new);
    }

    //제목별 큐레이션 검색
    public Slice<CurationSearchResponseDto> searchCuration(String title, Pageable pageable) {
        Slice<Curation> SearchCurationList = curationRepository.findAllByTitleLikeOrderByCreatedDateDesc('%' + title + '%', pageable);
        if (SearchCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);

        return SearchCurationList.map(CurationSearchResponseDto::new);
    }

    //카테고리,날짜별 큐레이션 조회
    public Slice<CurationSearchResponseDto> getFeed(String category, Pageable pageable) {
        InterestCategory interestCategory = interestCategoryRepository.findByCategory(category).get();
        Slice<Curation> getFeedCurationList = curationRepository.findAllByCategoryOrderByCreatedDateDesc(interestCategory, pageable);

        if (getFeedCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);

        return getFeedCurationList.map(CurationSearchResponseDto::new);
    }

    //큐레이션 좋아요 개수 조회
    public CurationLikeResponseDto getLike(long id) {
        Curation curation = curationRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        return new CurationLikeResponseDto(curation.getId(), curation.getLikeNum());
    }

    //내 프로필 큐레이션 조회 api
    public Page<CurationUserGetResponseDto> getMyCuration(long user_id, Pageable pageable) {
        Page<Curation> myCurationList = curationRepository.findAllByUser_Id(user_id, pageable);
        if (myCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);
        return myCurationList.map(CurationUserGetResponseDto::new);
    }

    //내 프로필 큐레이션 좋아요 조회 api
    public CurationGetMyLikeResponseDto getMyLikeCuration(long curation_id,ModugardenUser users) {
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        if(likeRepository.findByUserAndCuration(users.getUser(), curation).isPresent())
            return new CurationGetMyLikeResponseDto(users.getUserId(),curation.getId(), true);

        return new CurationGetMyLikeResponseDto(users.getUserId(),curation.getId(), false);
    }
    
    //내 프로필 저장한 큐레이션 조회
    public Page<CurationGetStorageResponseDto> getStorageCuration(long user_id, Pageable pageable) {
        Page<CurationGetStorageResponseDto> myCurationStorageList = curationRepository.QueryfindAllByUser_Id(user_id, pageable);

        if (myCurationStorageList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);

        return myCurationStorageList;
    }

    //큐레이션 삭제
    @Transactional
    public CurationDeleteResponseDto delete(long id, ModugardenUser user) {
        Curation curation = curationRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION_DELETE));

        if (curation.getUser().getId().equals(user.getUserId())) {
            // 보관 모두 삭제
            curationStorageRepository.deleteAllByCuration_Id(curation.getId());
            // 좋아요 모두 삭제
            likeRepository.deleteAllByCuration_Id(curation.getId());
            curationRepository.delete(curation);
        }
        else
            throw new BusinessException(ErrorMessage.WRONG_CURATION_DELETE);

        return new CurationDeleteResponseDto(curation.getId());
    }

    //큐레이션 좋아요 취소
    @Transactional
    public CurationLikeResponseDto createUnlikes(Long curation_id, ModugardenUser user) {
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        User users = userRepository.findById(user.getUserId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        likeRepository.findByUserAndCuration(users, curation)
                .ifPresent(it -> {
                    Curation modifyCuration = new Curation(curation.getId(), curation.getTitle(), curation.getLink(), curation.getPreviewImage(), curation.getLikeNum() - 1, curation.getUser(), curation.getCategory());
                    likeRepository.delete(it);
                    curationRepository.save(modifyCuration);
                });

        return new CurationLikeResponseDto(curation.getId(), curation.getLikeNum());
    }

    //큐레이션 보관 취소
    public CurationStorageResponseDto storeCancelCuration(ModugardenUser user, Long curation_id) {
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        curationStorageRepository.findByUserAndCuration(user.getUser(),curation).ifPresent(
                it->{
                    curationStorageRepository.delete(it);
                }
        );
        return new CurationStorageResponseDto(curation.getUser().getId(), curation.getId());
    }
}
