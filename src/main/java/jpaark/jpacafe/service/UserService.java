package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.domain.Users;
import jpaark.jpacafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class UserService {

    private final UserRepository userRepository;


    // 회원 가입
    @Transactional(readOnly = false) // 쓰기에는 readOnly true 이면 안되므로 다시 정의
    public String join(Users user) {
        validateDuplicateUser(user); // 중복 유저 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(Users user) {
        List<Users> findUsers = userRepository.findById(user.getId());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Users> findUsers() {
        return userRepository.findAll();
    }

    // 개별 회원 조회
    public Users findOne(String userId) {
        return userRepository.findOne(userId);
    }

    public Users login(String loginId, String password) {
        Users findUser = userRepository.findOne(loginId);
        if (findUser != null){
            return getUser(password, findUser);
        } else {
            return null;
        }

    }

    private static Users getUser(String password, Users findUser) {
        boolean equals = findUser.getPassword().equals(password);
        if (equals == true){
            return findUser;
        } else {
            return null;
        }
    }

    private Optional<Users> findUserByIdAndPassword(String loginId, String password) {
        Users user = userRepository.findOne(loginId); // 사용자 조회
        // 사용자가 존재하고 비밀번호가 일치하는 경우에만 Optional 객체로 반환
        return Optional.ofNullable(user)
                .filter(u -> u.getPassword().equals(password));
    }


    private List<Member> findAllMember(String id) {
        return userRepository.findAllMember(id);
    }

    public Users getUser(String loginId) {
        return userRepository.findOne(loginId);
    }
}
