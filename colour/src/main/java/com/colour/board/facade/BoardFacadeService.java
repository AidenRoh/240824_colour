package com.colour.board.facade;

import com.colour.board.hashtag.dto.HashtagVo;
import com.colour.board.hashtag.entity.Hashtag;
import com.colour.board.hashtag.service.HashtagService;
import com.colour.board.post.dto.PostDto;
import com.colour.board.post.entity.Post;
import com.colour.board.post.service.PostService;
import com.colour.board.tagtopost.dto.TagPostDto;
import com.colour.board.tagtopost.entity.TagPost;
import com.colour.board.tagtopost.service.TagPostService;
import com.colour.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardFacadeService {

    private final MemberService memberService;
    private final PostService postService;
    private final HashtagService hashtagService;
    private final TagPostService tagPostService;

    public BoardFacadeService(MemberService memberService, PostService postService,
                              HashtagService hashtagService, TagPostService tagPostService) {
        this.memberService = memberService;
        this.postService = postService;
        this.hashtagService = hashtagService;
        this.tagPostService = tagPostService;
    }

    public void createBoard(PostDto dto, Long memberId) {
        //get post elements
        String writer = memberService.findMemberById(memberId).getUsername();
        String colorPalette = dto.getColors().toString();
        //create post
        Post post = postService.createPost(new Post(writer, dto.getTitle(), dto.getContent(), colorPalette));
        registerHashtag(dto.getHashtags(), post.getPostId(), memberId);
    }

    public void updateBoard(PostDto dto, Long postId, Long memberId) {
        List<HashtagVo> newHashtagVo = dto.getHashtags();

        postService.updatePost(postId, dto);
        if (newHashtagVo != null) {
            abandonHashtag(postId);
            registerHashtag(newHashtagVo, postId, memberId);
        }
    }

    public void deleteBoard(Long postId) {
        abandonHashtag(postId);
        postService.deletePost(postId);
    }

    //Read methods
    public List<Post> getPosts(List<TagPost> tagPosts) {
        List<Post> container =new ArrayList<>();
        for (TagPost tagPost : tagPosts) {
            Long postId = tagPost.getPostId();
            container.add( postService.findPostById(postId));
        }
        return container;
    }

    public List<Hashtag> getHashtags(List<TagPost> tagPosts) {
        List<Hashtag> container =new ArrayList<>();
        for (TagPost tagPost : tagPosts) {
            Long tagId = tagPost.getHashtagId();
            container.add(hashtagService.findById(tagId));
        }
        return container;
    }

    public List<TagPost> findByPostId(Long postId) {
        TagPostDto dto = new TagPostDto();
        dto.setPostId(postId);
        return tagPostService.findByCond(dto);
    }

    public List<TagPost> findByHashtagId(Long hashtagId) {
        TagPostDto dto = new TagPostDto();
        dto.setTagId(hashtagId);
        return tagPostService.findByCond(dto);
    }

    public List<TagPost> findByMemberId(Long memberId) {
        TagPostDto dto = new TagPostDto();
        dto.setMemberId(memberId);
        return tagPostService.findByCond(dto);
    }

    public List<Post> findByTitle(String title) {
        return postService.findPostByTitle(title);
    }

    //internal logic
    private void registerHashtag(List<HashtagVo> tags, Long postId, Long memberId) {
        //create hashtag
        List<Hashtag> newHashtags = createHashtag(tags);
        //create tag_post
        createTagPost(newHashtags, postId, memberId);
    }

    private void abandonHashtag(Long postId) {
        List<TagPost> tagPostList = findByPostId(postId);
        for (TagPost tagPost : tagPostList) {
            hashtagService.delete(tagPost.getHashtagId());
            tagPostService.delete(tagPost.getTagPostId());
        }
    }

    private List<Hashtag> createHashtag(List<HashtagVo> tags) {
        List<Hashtag> tagList = new ArrayList<>();
        for (HashtagVo tag : tags) {
            Hashtag savedTag = hashtagService.save(new Hashtag(tag.getHashtag()));
            tagList.add(savedTag);
        }
        return tagList;
    }

    private void createTagPost(List<Hashtag> tagList, Long postId, Long memberId) {
        for (Hashtag hashtag : tagList) {
            tagPostService.save(new TagPost(hashtag.getHashtagId(), postId, memberId));
        }
    }

    /*
    // 게시판에 등록된 해시태그만 수정할 시
    public void removeHashtag(Long postId, Long hashtagId) {
        hashtagService.delete(hashtagId);
        tagPostService.delete(hashtagId, postId);
    }

    public void addHashtag(Long postId, Long memberId, HashtagVo dto) {
        Hashtag tag = new Hashtag(dto.getHashTag());
        Hashtag savedTag = hashtagService.save(tag);
        tagPostService.save(new TagPost(savedTag.getHashtagId(), postId, memberId));
    }
    */
}
