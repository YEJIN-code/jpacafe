package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.User;
import jpaark.jpacafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    @Transactional(readOnly = false) // 쓰기에는 readOnly true 이면 안되므로 다시 정의
    public String join(User user) {
        validateDuplicateUser(user); // 중복 유저 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        List<User> findUsers = userRepository.findById(user.getId());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    // 개별 회원 조회
    public User findOne(String userId) {
        return userRepository.findOne(userId);
    }

}
