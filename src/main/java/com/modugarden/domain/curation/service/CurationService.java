package com.modugarden.domain.curation.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.category.entity.InterestCategory;
import com.modugarden.domain.curation.dto.*;
import com.modugarden.domain.curation.entity.Curation;
import com.modugarden.domain.curation.repository.CurationRepository;
import com.modugarden.domain.like.repository.LikeRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CurationService {

    private final CurationRepository curationRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    //큐레이션 생성
    @Transactional
    public CurationCreateResponseDto create(CurationCreateRequestDto createRequestDto, MultipartFile file, User user) throws IOException {
        if(createRequestDto.getTitle().length()>40)
            throw new IOException(new BusinessException(ErrorMessage.WRONT_CURATION_TITLE));

        // 현재 해당하는 user 정보 가져와서 curation 저장
        createRequestDto.setUser(user);
        // 사진 입력
        String projectPath = "C:\\";
        //UUID uuid = UUID.randomUUID();
        String fileName = "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        //위에 파일객체의 경로와 리네임으로 실제 업로드 하기위해 transferTo()메서드 사용
        file.transferTo(saveFile);
        //db에 파일 저장하기
        createRequestDto.setPreviewImage(fileName);
        // 파일 저장
        Curation curation = createRequestDto.toEntity();

        return new CurationCreateResponseDto(curationRepository.save(curation).getId());
    }

    //큐레이션 좋아요 달기
    @Transactional
    public CurationLikeResponseDto createLikes(Long curation_id, User user){
        Curation curation = curationRepository.findById(curation_id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        User users = userRepository.findById(user.getId()).orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));

        boolean isAlreadyLike = likeRepository.findByUserAndCuration(users, curation).isPresent();

        if (!isAlreadyLike) {
            Curation modifyCuration = new Curation(curation.getId(),curation.getTitle(),curation.getLink(),curation.getPreviewImage(),curation.getLikeNum()+1,curation.getUser(),curation.getCategory());
            CurationLikeRequestDto curationLikeRequestDto = new CurationLikeRequestDto(users,modifyCuration);

            likeRepository.save(curationLikeRequestDto.toEntity());
            curationRepository.save(modifyCuration);
        }
        return new CurationLikeResponseDto(curation.getId(),curation.getLikeNum());
    }

    //큐레이션 하나 조회 api
    public CurationGetResponseDto get(long id) {
        Curation curation = curationRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION));
        return new CurationGetResponseDto(curation);
    }

    //회원 큐레이션 조회
    public Page<CurationUserGetResponseDto> getUser(long user_id, Pageable pageable) {
        Page<Curation> userCurationList = curationRepository.findAllByUser_Id(user_id, pageable);
        if(userCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);
        return userCurationList.map(CurationUserGetResponseDto::new);
    }

    //카테고리, 제목별 큐레이션 검색
    public Slice<CurationSearchResponseDto> search(InterestCategory category, String title, Pageable pageable){
        Slice<Curation> SearchCurationList = curationRepository.findAllByCategoryAndTitleLikeOrderByCreatedDateDesc(category,'%'+title+'%', pageable);
        if(SearchCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);
        return SearchCurationList.map(CurationSearchResponseDto::new);
    }

    //카테고리,날짜별 큐레이션 조회
    public Slice<CurationSearchResponseDto> getFeed(InterestCategory category, Pageable pageable){
        Slice<Curation> getFeedCurationList = curationRepository.findAllByCategoryOrderByCreatedDateDesc(category, pageable);
        if(getFeedCurationList.isEmpty())
            throw new BusinessException(ErrorMessage.WRONG_CURATION_LIST);
        return getFeedCurationList.map(CurationSearchResponseDto::new);
    }

    //큐레이션 삭제
    @Transactional
    public CurationDeleteResponseDto delete(long id){
        Curation curation = curationRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessage.WRONG_CURATION_DELETE));
        curationRepository.delete(curation);
        return new CurationDeleteResponseDto(curation.getId());
    }
}
