package KTB4_gourmet_Week10.Assignment.repository;

import KTB4_gourmet_Week10.Assignment.entity.Post;
import KTB4_gourmet_Week10.Assignment.search.PostSearchCondition;
import KTB4_gourmet_Week10.Assignment.search.PostSortType;
import KTB4_gourmet_Week10.Assignment.search.SearchType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
@RequiredArgsConstructor
public class PostSearchRepositoryImpl
        implements PostSearchRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Post> search(
            PostSearchCondition condition,
            Pageable pageable
    ) {
        String whereClause =
                createWhereClause(condition);

        String orderByClause =
                createOrderByClause(condition.getSortType());

        String contentJpql = """
                SELECT post
                FROM Post post
                JOIN FETCH post.user user
                """ + whereClause + orderByClause;

        TypedQuery<Post> contentQuery =
                entityManager.createQuery(
                        contentJpql,
                        Post.class
                );

        setParameters(
                contentQuery,
                condition
        );

        contentQuery.setFirstResult(
                Math.toIntExact(pageable.getOffset())
        );

        contentQuery.setMaxResults(
                pageable.getPageSize()
        );

        List<Post> content =
                contentQuery.getResultList();

        long totalElements =
                countSearchResults(
                        condition,
                        whereClause
                );

        return new PageImpl<>(
                content,
                pageable,
                totalElements
        );
    }

    private long countSearchResults(
            PostSearchCondition condition,
            String whereClause
    ) {
        String countJpql = """
                SELECT COUNT(post.id)
                FROM Post post
                JOIN post.user user
                """ + whereClause;

        TypedQuery<Long> countQuery =
                entityManager.createQuery(
                        countJpql,
                        Long.class
                );

        setParameters(
                countQuery,
                condition
        );

        return countQuery.getSingleResult();
    }

    private String createWhereClause(
            PostSearchCondition condition
    ) {
        StringBuilder where =
                new StringBuilder(" WHERE 1 = 1 ");

        if (condition.getBoardType() != null) {
            where.append(
                    " AND post.boardType = :boardType "
            );
        }

        SearchType searchType =
                condition.getSearchType();

        switch (searchType) {
            case TITLE -> where.append("""
                     AND LOWER(post.title)
                         LIKE :keyword
                    """);

            case CONTENT -> where.append("""
                     AND LOWER(post.content)
                         LIKE :keyword
                    """);

            case NICKNAME -> where.append("""
                     AND LOWER(user.nickname)
                         LIKE :keyword
                    """);

            case ALL -> where.append("""
                     AND (
                            LOWER(post.title)
                                LIKE :keyword
                         OR LOWER(post.content)
                                LIKE :keyword
                         OR LOWER(user.nickname)
                                LIKE :keyword
                     )
                    """);
        }

        return where.toString();
    }

    private String createOrderByClause(
            PostSortType sortType
    ) {
        if (sortType == PostSortType.VIEW_COUNT) {
            return """
                     ORDER BY
                         post.viewCount DESC,
                         post.createdAt DESC,
                         post.id DESC
                    """;
        }

        return """
                 ORDER BY
                     post.createdAt DESC,
                     post.id DESC
                """;
    }

    private void setParameters(
            Query query,
            PostSearchCondition condition
    ) {
        String keywordPattern =
                "%" +
                        condition.getKeyword()
                                .toLowerCase(Locale.ROOT) +
                        "%";

        query.setParameter(
                "keyword",
                keywordPattern
        );

        if (condition.getBoardType() != null) {
            query.setParameter(
                    "boardType",
                    condition.getBoardType()
            );
        }
    }
}