package com.social.app.service;

import com.social.app.dto.PostDTO;
import com.social.app.model.Post;
import com.social.app.model.PostLike;
import com.social.app.model.User;
import com.social.app.repository.PostLikeRepository;
import com.social.app.repository.PostRepository;
import com.social.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

@Service
public class PostServices {


    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    ModelMapper modelMapper;

    public PostDTO MapPostDTO(Post post){
        PostDTO postDTO = modelMapper.map(post,PostDTO.class);
        return postDTO;
    }

    public ArrayList<PostDTO> ArrayListPostDTO(ArrayList<Post> posts){
        ArrayList<PostDTO> postDTOS = new ArrayList<>();
        for(Post p: posts){
            PostDTO pDTO = MapPostDTO(p);
            postDTOS.add(pDTO);
        }
        return postDTOS;
    }
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
    public ArrayList<Post> retriveGroupPostFromDB(Long groupId){
        ArrayList<Post> result = postRepository.findAllByGroupGroupId(groupId);
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
        else throw new RuntimeException("Can't find post");
    }



    public long findDifference(String start_date,String end_date)
    {
        long difference_In_Hours=0;
        int hours=0;
        SimpleDateFormat sdf
                = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            int seconds = (int) difference_In_Time / 1000;
            hours = seconds / 3600;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return hours;
    }

    public ArrayList<Post> printHotPost(boolean authentication) {
        ArrayList<Post> allPost = retrivePostFromDB();
        Map<Post, Double> hotPost = new HashMap<>();
        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);
        String timeNow = datetime.toString();
        Map<Post, Double> sorted = null;
        for (Post p : allPost) {
            int like = 0;
            //calculate scores
            int likeComment = p.getComments().size() + p.countLike();
            long diffTime = findDifference(p.getTime().toString(), timeNow);
            Double scores = (likeComment - 1) / Math.pow(diffTime + 2, 1.8);
            hotPost.put(p, scores);
        }
        sorted = hotPost
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));
        sorted = hotPost
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        ArrayList<Post> valueList = new ArrayList<Post>(sorted.keySet());
        if (authentication == false) {
            for (Post p : valueList) {
                p.setComments(null);
            }
        }
        return valueList;
    }
    public ArrayList<Post> getAllPostByLike() {
        ArrayList<Post> posts = retrivePostFromDB();
        posts.sort(((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size())));
        return posts;
    }

    public ArrayList<Post> getAllPostByComment() {
        ArrayList<Post> posts = retrivePostFromDB();
        Collections.sort(posts, ((o1, o2) -> Integer.compare(o2.getComments().size(), o1.getComments().size())));
        return posts;
    }

    public ArrayList<Post> getAllPostByTime() {
        ArrayList<Post> posts = retrivePostFromDB();
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return posts;
    }
    public ArrayList<Post> retriveUserPostFromDB(int userId){
        ArrayList<Post> result = postRepository.findAllByUserUserId(userId);
        return result;
    }

    public ArrayList<Post> getAllPostByLikeUser(int userid) {
        ArrayList<Post> posts = retriveUserPostFromDB(userid);
        posts.sort(((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size())));
        return posts;
    }
    public ArrayList<Post> getAllPostByCommentUser(int userid) {
        ArrayList<Post> posts = retriveUserPostFromDB(userid);
        Collections.sort(posts, ((o1, o2) -> Integer.compare(o2.getComments().size(), o1.getComments().size())));
        return posts;
    }
    public ArrayList<Post> getAllPostByTimeUser(int userid) {
        ArrayList<Post> posts = retriveUserPostFromDB(userid);
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });
        return posts;
    }
}
