package KTB4_gourmet_Week10.Assignment.dto;

import lombok.Getter;

@Getter
public class PostLikeResponseDto {

    private final boolean liked;
    private final long likeCount;

    public PostLikeResponseDto(boolean liked, long likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }
}