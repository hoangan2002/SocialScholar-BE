package com.social.app.service;

import com.social.app.model.Post;
import com.social.app.model.User;
import com.social.app.repository.PostRepository;
import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class PostServices {


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageStorageService imageStorageService;
    public Post submitPostToDB(Post postData){

        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);

        postData.setTime(datetime);


        return postRepository.save(postData);
    }

    public ArrayList<Post> retrivePostFromDB(){
        ArrayList<Post> result = postRepository.findAll();
        return result;
    }

    public ArrayList<Post> deletePostDB(long postID){
        postRepository.deleteById(postID);
        ArrayList<Post> result = retrivePostFromDB();
        return result;
    }

    public Post editPostDB(Post posts){
        return postRepository.findById(posts.getPostId())
                .map(post -> {
                    post.setContent(posts.getContent());
                    post.setImageURL(posts.getImageURL());
                    post.setTime(post.getTime());
                    post.setUser(post.getUser());
                    post.setGroup(post.getGroup());
                    return  postRepository.save(post);
                }).orElseThrow(()-> new RuntimeException("Post not exits !"));
    }

    public Post loadPostById(long postId) throws UsernameNotFoundException {
        Post post = postRepository.findByPostId(postId);
        if(post!=null)
            return post;
        else throw new RuntimeException("Not valid user");
    }




}
