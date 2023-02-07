//package com.modugarden.domain.fcm.repository;
//
//import com.modugarden.domain.fcm.entity.FcmToken;
//import com.modugarden.domain.user.entity.User;
//import com.modugarden.domain.user.entity.enums.UserAuthority;
//import com.modugarden.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//
//import javax.transaction.Transactional;
//
//@Commit
//@Transactional
//@SpringBootTest
//class FcmRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private FcmRepository fcmRepository;
//
//    @Test
//    void deleteAll과deleteBy비교() throws Exception{
//        //given
//        User newUser = User.builder()
//                .email("alsjung8@gmail.com")
//                .authority(UserAuthority.ROLE_GENERAL)
//                .build();
//
//        FcmToken fcmToken1 = new FcmToken("fcmToken1", newUser);
//        FcmToken fcmToken2 = new FcmToken("fcmToken2", newUser);
//
//        //when
//        userRepository.save(newUser);
//        fcmRepository.save(fcmToken1);
//        fcmRepository.save(fcmToken2);
//
//        //then
//        fcmRepository.deleteAllByUser(newUser);
//    }
//
//}
