package com.be16_2nd.SmartFridge.Post.service;

import com.be16_2nd.SmartFridge.Post.domain.Post;
import com.be16_2nd.SmartFridge.Post.domain.PostCategory;
import com.be16_2nd.SmartFridge.Post.domain.PostComment;
import com.be16_2nd.SmartFridge.Post.domain.PostImage;
import com.be16_2nd.SmartFridge.Post.dto.PostCommentCreateDto;
import com.be16_2nd.SmartFridge.Post.dto.PostCreateDto;
import com.be16_2nd.SmartFridge.Post.repository.PostCategoryRepository;
import com.be16_2nd.SmartFridge.Post.repository.PostCommentRepository;
import com.be16_2nd.SmartFridge.Post.repository.PostImageRepository;
import com.be16_2nd.SmartFridge.Post.repository.PostRepository;
import com.be16_2nd.SmartFridge.fridge.domain.Fridge;
import com.be16_2nd.SmartFridge.fridge.domain.FridgeMember;
import com.be16_2nd.SmartFridge.fridge.domain.Type;
import com.be16_2nd.SmartFridge.fridge.repository.FridgeMemberRepository;
import com.be16_2nd.SmartFridge.fridge.repository.FridgeRepository;
import com.be16_2nd.SmartFridge.member.domain.Member;
import com.be16_2nd.SmartFridge.member.repository.MemberRepository;
import com.be16_2nd.SmartFridge.notification.domain.Notification;
import com.be16_2nd.SmartFridge.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    public final MemberRepository memberRepository;
    public final PostCategoryRepository postCategoryRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3Client s3Client;
    private final FridgeRepository fridgeRepository;
    private final NotificationService notificationService;
    private final FridgeMemberRepository fridgeMemberRepository;
    private final PostCommentRepository postCommentRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public Long createPost(PostCreateDto postCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member =  memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 사용자입니다."));
        PostCategory postCategory = postCategoryRepository.findById(postCreateDto.getCategoryId()).orElseThrow(()-> new EntityNotFoundException("없는 카테고리입니다."));
        Post post = postCreateDto.toEntity(member,postCategory);
        postRepository.save(post);
        List<MultipartFile> postImages = postCreateDto.getImages();
        if(!postCreateDto.getImages().isEmpty() && postCreateDto.getImages() != null ) {
            for(MultipartFile postImage : postImages) {
                String fileName = "post-" + post.getId() + "-imagepath" + postImage.getOriginalFilename();
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType(postImage.getContentType())
                        .build();
                try{
                    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(postImage.getBytes()));
                } catch(Exception e) {
                    throw new IllegalArgumentException("이미지 업로드 실패");
                }
                String imgUrl = s3Client.utilities().getUrl(a->a.bucket(bucket).key(fileName)).toExternalForm();
                PostImage image = PostImage.toEntity(imgUrl, post);
                postImageRepository.save(image);
            }

        }
        return  post.getId();
    }

    public Long createAnnPost(PostCreateDto dto, Long fridgeId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 사용자입니다."));
        Fridge fridge = fridgeRepository.findById(fridgeId).orElseThrow(() -> new EntityNotFoundException("없는 냉장고 입니다."));

        FridgeMember fridgeMember = fridgeMemberRepository.findByFridgeAndMember(fridge, member);
        if (fridgeMember.getType() != Type.MANAGER) {
            throw new SecurityException("공지사항을 작성할 권한이 없습니다.");
        }

        PostCategory category = postCategoryRepository.getReferenceById(dto.getCategoryId());
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(category)
                .member(member)
                .fridge(fridge)
                .build();

        Post newPost = postRepository.save(post);

        if ("공지사항".equals(newPost.getCategory().getCategory())) {

            List<FridgeMember> fridgeMemberList = fridgeMemberRepository.findByFridge(fridge);

            for (FridgeMember fridgeMembers : fridgeMemberList) {

                if (fridgeMembers.getType() == Type.COMMON) {
                    Member receiver = fridgeMembers.getMember();

                    Notification notification = Notification.fromAnnounce(member, receiver, newPost);

                    notificationService.create(notification);
                }
            }
        }

        return newPost.getId();
    }

    public Long createComment(PostCommentCreateDto postCommentCreateDto, Long postId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("없는 사용자입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        PostComment postComment = postCommentCreateDto.toEntity(post, member);

        postCommentRepository.save(postComment);

        Member postAuthor = post.getMember();

        if (!member.equals(postAuthor)) {
            Notification notification = Notification.fromComment(member, postAuthor, postComment);
            notificationService.create(notification);
        }
        return postComment.getId();
    }
}
