//package com.modugarden.domain.comment.service;
//
//import com.modugarden.domain.board.entity.Board;
//import com.modugarden.domain.board.repository.BoardRepository;
//import com.modugarden.domain.comment.entity.Comment;
//import com.modugarden.domain.comment.repository.CommentRepository;
//import com.modugarden.domain.user.entity.User;
//import com.modugarden.domain.user.entity.UserNotification;
//import com.modugarden.domain.user.entity.enums.UserAuthority;
//import com.modugarden.domain.user.repository.UserNotificationRepository;
//import com.modugarden.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Commit
//@Transactional
//@SpringBootTest
//class CommentServiceTest {
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BoardRepository boardRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private UserNotificationRepository userNotificationRepository;
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    EntityManager em;
//
//    @Test
//    void 유저삭제시_게시물까지_삭제() throws Exception{
//        //given
//        UserNotification userNotification = new UserNotification(true, true, true, true);
//        UserNotification userNotification2 = new UserNotification(true, true, true, true);
//
//        User newUser = User.builder()
//                .email("alsjung8@gmail.com")
//                .authority(UserAuthority.ROLE_GENERAL)
//                .notification(userNotification)
//                .build();
//
//        User newUser2 = User.builder()
//                .email("alsjung9@gmail.com")
//                .authority(UserAuthority.ROLE_GENERAL)
//                .notification(userNotification2)
//                .build();
//        userNotificationRepository.save(userNotification);
//        userNotificationRepository.save(userNotification2);
//        userRepository.save(newUser);
//        userRepository.save(newUser2);
//
//        Board newBoard = new Board("타이틀", newUser2);
//        boardRepository.save(newBoard);
//
//        Comment comment1 = new Comment("탈퇴 부모 댓글", null, newBoard, newUser);
//        commentRepository.save(comment1);
//        //System.out.println("comment1.getCommentId() = " + comment1.getCommentId());
//        Comment comment2 = new Comment("탈퇴 X 자식 댓글1", comment1.getCommentId(), newBoard, newUser2);
//        commentRepository.save(comment2);
//
//        Comment comment3 = new Comment("탈퇴 X 부모 댓글", null, newBoard, newUser2);
//        commentRepository.save(comment3);
//        Comment comment4 = new Comment("탈퇴 자식 댓글1", comment3.getCommentId(), newBoard, newUser);
//        commentRepository.save(comment4);
//        em.clear();
//
//
//
//
//        //then
//        commentService.deleteAllCommentOfUser(newUser);
//
//    }
//
//}