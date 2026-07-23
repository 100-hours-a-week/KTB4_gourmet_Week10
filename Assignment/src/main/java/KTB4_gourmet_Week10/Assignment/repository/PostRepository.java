package KTB4_gourmet_Week10.Assignment.repository;

import KTB4_gourmet_Week10.Assignment.entity.BoardType;
import KTB4_gourmet_Week10.Assignment.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser_IdOrderByIdAsc(Long userId);

    @Override
    @EntityGraph(attributePaths = "user")
    Page<Post> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Post> findByBoardType(
            BoardType boardType,
            Pageable pageable
    );
}