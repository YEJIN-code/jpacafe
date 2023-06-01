package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Users;
import jpaark.jpacafe.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {

        // give
        Users user = new Users();
        user.setId("asdf123");

        // when
        String savedId = userService.join(user);

        // then
        assertEquals(user, userRepository.findOne(savedId));


    }


    @Test(expected = IllegalStateException.class)
    public void 중복회원_예외() throws Exception {
        // given
        Users user1 = new Users();
        user1.setId("asdf");

        Users user2 = new Users();
        user2.setId("asdf");

        userService.join(user1);
        userService.join(user2);

        // when & then
        Assert.fail("예외가 발생해야 한다.");
    }

}