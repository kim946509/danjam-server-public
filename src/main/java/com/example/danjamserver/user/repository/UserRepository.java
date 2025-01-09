package com.example.danjamserver.user.repository;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 중복된 아이디가 있는지 확인하기 위한 메소드. JPA에서 제공하는 메소드명을 통해 쿼리를 생성한다. 구현하지 않아도 된다.
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    // username을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Optional<User> findByUsername(String username);

    List<User> findUsersByRole(Role role);

    //nickname 으로 유저 조회
    Optional<User> findByNickname(String nickname);

    // 특정 학교에 속한 모든 사용자를 조회
    List<User> findAllBySchool(School school);

    // id로 유저 조회
    Optional<User> findById(Long id);

    //Candidate 후보 검색에서 상세 정보를 조회할때 User 정보를 조회함.
    //기존에는 한명씩 반복문을 통해 조회했지만, 한번에 여러명을 조회하기 위해 List로 반환
    @Query("SELECT u FROM User u JOIN FETCH u.myProfile mp WHERE u.id IN :ids")
    List<User> findUsersByIds(@Param("ids") List<Long> ids);

    // 매핑테이블의 topic Id 값 찾기
    @Query("SELECT t.id FROM User u JOIN u.topics t WHERE u.username = :username")
    Long findTopicIdsByUsername(@Param("username") String username);
}