package KTB4_gourmet_Week10.Assignment.repository;

import KTB4_gourmet_Week10.Assignment.entity.Post;
import KTB4_gourmet_Week10.Assignment.search.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostSearchRepository {

    Page<Post> search(
            PostSearchCondition condition,
            Pageable pageable
    );
}

/*
LIKE
→ Full Text Search

Offset
→ Cursor 기반 조회
 */