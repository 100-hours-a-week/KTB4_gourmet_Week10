package KTB4_gourmet_Week10.Assignment.service;

import KTB4_gourmet_Week10.Assignment.dto.PostLikeResponseDto;
import KTB4_gourmet_Week10.Assignment.entity.Post;
import KTB4_gourmet_Week10.Assignment.entity.PostLike;
import KTB4_gourmet_Week10.Assignment.entity.User;
import KTB4_gourmet_Week10.Assignment.exception.PostNotFoundException;
import KTB4_gourmet_Week10.Assignment.exception.UserNotFoundException;
import KTB4_gourmet_Week10.Assignment.repository.PostLikeRepository;
import KTB4_gourmet_Week10.Assignment.repository.PostRepository;
import KTB4_gourmet_Week10.Assignment.repository.UserRepository;
import KTB4_gourmet_Week10.Assignment.auth.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeResponseDto getLikeStatus(Long postId, Long userId) {
        SecurityUtil.validateLoginUser(userId);

        boolean liked = postLikeRepository.existsByUser_IdAndPost_Id(userId, postId);
        long likeCount = postLikeRepository.countByPost_Id(postId);

        return new PostLikeResponseDto(liked, likeCount);
    }

    @Transactional
    public PostLikeResponseDto toggleLike(Long postId, Long userId) {

        SecurityUtil.validateLoginUser(userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("회원을 찾을 수 없습니다."));

        return postLikeRepository.findByUser_IdAndPost_Id(userId, postId)
                .map(postLike -> {
                    postLikeRepository.delete(postLike);

                    long likeCount = postLikeRepository.countByPost_Id(postId);

                    return new PostLikeResponseDto(false, likeCount);
                })
                .orElseGet(() -> {
                    PostLike postLike = new PostLike(user, post);
                    postLikeRepository.save(postLike);

                    long likeCount = postLikeRepository.countByPost_Id(postId);

                    return new PostLikeResponseDto(true, likeCount);
                });
    }
}