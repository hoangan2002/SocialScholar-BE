package com.social.app.service;

import com.social.app.entity.CommentResponse;
import com.social.app.entity.DocumentResponse;
import com.social.app.entity.LikeResponse;
import com.social.app.entity.PostResponse;

import com.social.app.entity.UserResponse;

import com.social.app.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseConvertService {
    public CommentResponse commentResponse(Comment comment) {
        var commentResponse = CommentResponse.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .commentParentId(comment.getCommentParentId())
                .content(comment.getContent())
                .time(comment.getTime())
                .author(comment.getUser().getUserName())
                .likes(commentLikeResponse(comment.getLikes()))
                .build();

        return commentResponse;
    }

    public PostResponse postResponse(Post post) {
        var postResponse = PostResponse.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .time(post.getTime())
                .imageURL(post.getImageURL())
                .author(post.getUser().getUserName())
                .groupName(post.getGroup().getGroupName())
                .groupId(post.getGroup().getGroupId())
                .comments(commentResponseArrayList(post.getComments()))
                .likes(postLikeResponse(post.getLikes()))
                .titles(post.getTitles())
                .build();

        return postResponse;
    }
    public UserResponse userResponse(User user) {
        var userResponse = UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .level(user.getLevel())
                .isLocked(user.isLocked())
                .build();
        return userResponse;
    }

    public LikeResponse likeResponse(PostLike like) {
        var likeResponse = LikeResponse.builder()
                .likeId(like.getLikeId())
                .auth(like.getUser().getUserName())
                .status(like.getStatus())
                .time(like.getTime())
                .build();
        return likeResponse;
    }

    public DocumentResponse documentResponse(Document document){
        var documentResponse = DocumentResponse.builder()
                .documentId(document.getDocumentId())
                .documentName(document.getDocumentName())
                .description(document.getDescription())
                .cost(document.getCost())
                .ratings(document.getRatings())
                .time(document.getTime())
                .author(document.getAuthor().getUserName())
                .groupName(document.getGroup().getGroupName())
                .build();
        return  documentResponse;
    }
    public LikeResponse likeResponse(CommentLike like) {
        var likeResponse = LikeResponse.builder()
                .likeId(like.getLikeId())
                .auth(like.getUser().getUserName())
                .status(like.getStatus())
                .time(like.getTime())
                .build();
        return likeResponse;
    }

    public ArrayList<UserResponse> userResponseArrayList (ArrayList<User> userArrayList)
    {
        ArrayList<UserResponse> userResponses = new ArrayList<>();

        for (User user : userArrayList) {
            UserResponse userResponse = userResponse(user);
            userResponses.add(userResponse);
        }


        return userResponses;
    }

    public ArrayList<DocumentResponse> documentResponseArrayList (ArrayList<Document> documentArrayList)
    {
        ArrayList<DocumentResponse> documentResponses = new ArrayList<>();


        for (Document document : documentArrayList) {

            DocumentResponse documentResponse = documentResponse(document);

            documentResponses.add(documentResponse);
        }


        return documentResponses;
    }
    public ArrayList<PostResponse> postResponseArrayList (ArrayList<Post> postArrayList)
    {
        ArrayList<PostResponse> postResponses = new ArrayList<>();

        for (Post post : postArrayList) {

            PostResponse postResponse = postResponse(post);

            postResponses.add(postResponse);
        }


        return postResponses;
    }
    public List<CommentResponse> commentResponseArrayList (List<Comment> commentArrayList)
    {
        ArrayList<CommentResponse> commentResponseArrayList = new ArrayList<>();

        for (Comment comment : commentArrayList) {

            CommentResponse commentResponse = commentResponse(comment);

            commentResponseArrayList.add(commentResponse);
        }


        return commentResponseArrayList;
    }

    public List<LikeResponse> commentLikeResponse (List<CommentLike> likes)
    {
        ArrayList<LikeResponse> likeResponses = new ArrayList<>();

        for (CommentLike like : likes) {

            LikeResponse likeResponse = likeResponse(like);

            likeResponses.add(likeResponse);
        }


        return likeResponses;
    }

    public List<LikeResponse> postLikeResponse (List<PostLike> likes)
    {
        ArrayList<LikeResponse> likeResponses = new ArrayList<>();

        for (PostLike like : likes) {

            LikeResponse likeResponse = likeResponse(like);

            likeResponses.add(likeResponse);
        }


        return likeResponses;
    }

}
