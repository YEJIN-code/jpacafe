package jpaark.jpacafe.service;

import jpaark.jpacafe.domain.Cafe;
import jpaark.jpacafe.domain.Member;
import jpaark.jpacafe.repository.CafeRepository;
import jpaark.jpacafe.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Service
@Transactional(readOnly = true) // 조회 성능 최적화
@RequiredArgsConstructor // final 로 된 걸 생성해줌
public class CafeService {

    private final CafeRepository cafeRepository;

    // 카페 생성
    @Transactional(readOnly = false)
    public Long join(Cafe cafe) {
        validateDuplicateCafe(cafe);
        cafeRepository.save(cafe);
        return cafe.getId();
    }

    // 중복 카페 검증
    public void validateDuplicateCafe(Cafe cafe) {
        List<Cafe> cafeList = cafeRepository.findListByName(cafe.getName());
        if (!cafeList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 카페명입니다.");
        }
    }

    // 카페 전체 조회
    public List<Cafe> findAll(Long cafeId) {
        return cafeRepository.findAll();
    }

    // 카페 이름으로 조회
    public List<Cafe> findOne(String name) {
        return cafeRepository.findListByName(name);
    }

}
