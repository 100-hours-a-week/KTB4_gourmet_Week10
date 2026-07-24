package KTB4_gourmet_Week10.Assignment.search;

import KTB4_gourmet_Week10.Assignment.entity.BoardType;
import lombok.Getter;

@Getter
public class PostSearchCondition {

    private final String keyword;
    private final SearchType searchType;
    private final BoardType boardType;
    private final PostSortType sortType;

    public PostSearchCondition(
            String keyword,
            SearchType searchType,
            BoardType boardType,
            PostSortType sortType
    ) {
        this.keyword = keyword.trim();

        this.searchType = searchType == null
                ? SearchType.ALL
                : searchType;

        this.boardType = boardType;

        this.sortType = sortType == null
                ? PostSortType.LATEST
                : sortType;
    }
}