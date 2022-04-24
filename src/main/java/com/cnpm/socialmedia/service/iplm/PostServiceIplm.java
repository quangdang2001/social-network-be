package com.cnpm.socialmedia.service.iplm;

import com.cnpm.socialmedia.model.Post;
import com.cnpm.socialmedia.repo.PostRepo;
import com.cnpm.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceIplm implements PostService {

    private final PostRepo postRepo;

    @Override
    public Post save(Post post) {
        return postRepo.save(post);
    }

    @Override
    public List<Post> findPostHomePage(Long userId) {
        return null;
    }

    @Override
    public List<Post> findPostOfUser(Long userId) {
        return postRepo.findPostsByUsers_IdOrderByCreateTimeDesc(userId);
    }

    @Override
    public Post findPostById(Long id) {
        Optional<Post> post = postRepo.findById(id);
        return post.orElse(null);
    }

    @Override
    public void deletePostById(Long id) {
        postRepo.deleteById(id);
    }
}
