package KTB4_gourmet_Week10.Assignment.service;

import KTB4_gourmet_Week10.Assignment.dto.PostPageResponseDto;
import KTB4_gourmet_Week10.Assignment.dto.PostResponseDto;
import KTB4_gourmet_Week10.Assignment.entity.BoardType;
import KTB4_gourmet_Week10.Assignment.entity.Post;
import KTB4_gourmet_Week10.Assignment.repository.PostRepository;
import KTB4_gourmet_Week10.Assignment.search.PostSearchCondition;
import KTB4_gourmet_Week10.Assignment.search.PostSortType;
import KTB4_gourmet_Week10.Assignment.search.SearchType;
import KTB4_gourmet_Week10.Assignment.service.assembler.PostResponseAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchService {

    private final PostRepository postRepository;
    private final PostResponseAssembler postResponseAssembler;

    public PostPageResponseDto searchPosts(
            String keyword,
            SearchType searchType,
            BoardType boardType,
            PostSortType sortType,
            int page,
            int size
    ) {
        PostSearchCondition condition =
                new PostSearchCondition(
                        keyword,
                        searchType,
                        boardType,
                        sortType
                );

        PageRequest pageable =
                PageRequest.of(
                        page,
                        size
                );

        Page<Post> postPage =
                postRepository.search(
                        condition,
                        pageable
                );

        List<PostResponseDto> content =
                postResponseAssembler.toDtos(
                        postPage.getContent()
                );

        return new PostPageResponseDto(
                content,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.hasNext(),
                postPage.hasPrevious()
        );
    }
}