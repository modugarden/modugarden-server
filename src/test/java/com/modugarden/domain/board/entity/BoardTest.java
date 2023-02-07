//package com.modugarden.domain.board.entity;
//
//import com.modugarden.domain.auth.service.AuthService2;
//import com.modugarden.domain.board.repository.BoardRepository;
//import com.modugarden.domain.comment.entity.Comment;
//import com.modugarden.domain.comment.repository.CommentRepository;
//import com.modugarden.domain.fcm.entity.FcmToken;
//import com.modugarden.domain.fcm.repository.FcmRepository;
//import com.modugarden.domain.follow.entity.Follow;
//import com.modugarden.domain.follow.repository.FollowRepository;
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
//@Transactional
//@Commit
//@SpringBootTest
//class BoardTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private BoardRepository boardRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private FollowRepository followRepository;
//
//    @Autowired
//    private FcmRepository fcmRepository;
//
//    @Autowired
//    private AuthService2 authService;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private UserNotificationRepository userNotificationRepository;
//
//
//    @Test
//    void 유저삭제시_게시물까지_삭제() throws Exception{
//        //given
//        UserNotification userNotification = new UserNotification(true, true, true, true);
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
//                .build();
//
//        Board newBoard = new Board("타이틀", 0L, "img", newUser);
//        Comment comment1 = new Comment("부모 댓글", null, newBoard, newUser);
//        Comment comment2 = new Comment("자식 댓글2", comment1.getCommentId(), newBoard, newUser);
//
//        Follow follow = new Follow(newUser, newUser2);
//        Follow follow1 = new Follow(newUser2, newUser);
//
//        FcmToken fcmToken1 = new FcmToken("fcmToken1", newUser);
//        FcmToken fcmToken2 = new FcmToken("fcmToken2", newUser);
//
//
//        //when
//        userNotificationRepository.save(userNotification);
//        userRepository.save(newUser);
//        userRepository.save(newUser2);
//        boardRepository.save(newBoard);
//        commentRepository.save(comment1);
//        commentRepository.save(comment2);
//        followRepository.save(follow1);
//        followRepository.save(follow);
//        fcmRepository.save(fcmToken1);
//        fcmRepository.save(fcmToken2);
//        em.flush();
//        em.clear();
//
//        //then
//        authService.deleteCurrentUser(newUser);
//    }
//
//}
