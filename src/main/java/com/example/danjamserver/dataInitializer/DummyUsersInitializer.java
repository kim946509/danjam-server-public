package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.mate.domain.SchoolMajors;
import com.example.danjamserver.mate.repository.SchoolMajorsRepository;
import com.example.danjamserver.springSecurity.role.Role;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@Order(2)
@RequiredArgsConstructor
public class DummyUsersInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final MyProfileRepository myProfileRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolMajorsRepository schoolMajorsRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public static Integer startDummyUserNumber = 1;
    public static Integer endDummyUserNumber = 10000;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // 더미 유저가 존재하는지 확인. 존재하지 않으면 더미 유저 생성
        String checkUsername = "test" + DummyUsersInitializer.startDummyUserNumber;
        User checkUser = userRepository.findByUsername(checkUsername).orElse(null);
        if (checkUser != null) {
            return;
        }
        String dummyUsername = "test";

        // Create dummy users 100개
        for (int i = startDummyUserNumber; i <= endDummyUserNumber; i++) {
            String username = dummyUsername + i;
            String nickname = dummyUsername + i;

            // 패스워드는 username + number를 암호화 해서 저장
            String password = username + i;
            password = passwordEncoder.encode(password);


            String email = dummyUsername + i + "@test.com";
            String authImgUrl = "https://drive.google.com/file/d/1Lku5vBQ5dNG1f8b6mhTyW-do1q8ykGWE/view?usp=sharing";
            String residentImgUrl = "https://drive.google.com/file/d/1EOZtBCujAQ5FiYfkKjUhuLaE3RdHbKOq/view?usp=sharing";
            Role role = Role.AUTH_USER;

            //학교는 0또는 1랜덤으로 선택
            School school = schoolRepository.findById((long) (Math.random() * 2) + 1).orElseThrow(() -> new IllegalArgumentException("더미 유저 생성 중 해당 학교가 없습니다."));

            // 성별은 0(여자) 1(남자) 랜덤으로 선택
            Integer gender = (int) (Math.random() * 2);

            //MyProfile 정보
            String birth = generateRandomBirth();

            //학번은 2015 ~ 2024 랜덤으로 선택
            Integer entryYear = (int) (Math.random() * 10) + 2015;

            //전공은 학교에 맞는 전공중 랜덤으로 선택
            String major = generateRandomMajor(school);

            //프로필이미지
            String profileImgUrl = "https://drive.google.com/file/d/1i1FHRJeipS1wYeLB5Z6WagrucTBG0FK6/view?usp=sharing";

            //mbti 랜덤으로 선택
            String mbti = generateRandomMbti();

            //greeting 랜덤으로 선택
            String greeting = generateRandomGreeting();

            //MyProfile 생성
            MyProfile myProfile = MyProfile.builder()
                    .birth(birth)
                    .entryYear(entryYear)
                    .major(major)
                    .profileImgUrl(profileImgUrl)
                    .mbti(mbti)
                    .greeting(greeting)
                    .build();

            myProfileRepository.save(myProfile);

            //User 생성
            userRepository.save(User.builder()
                    .username(username)
                    .gender(gender)
                    .nickname(nickname)
                    .password(password)
                    .email(email)
                    .authImgUrl(authImgUrl)
                    .residentImgUrl(residentImgUrl)
                    .role(role)
                    .school(school)
                    .myProfile(myProfile)
                    .build());
        }

    }

    // 학교에 맞는 랜덤 전공 생성
    private String generateRandomMajor(School school) {
        List<SchoolMajors> schoolMajors = schoolMajorsRepository.findBySchoolId(school.getId());
        SchoolMajors schoolMajor = schoolMajors.get((int) (Math.random() * schoolMajors.size()));
        return schoolMajor.getMajor();
    }

    // 랜덤 생년월일 생성
    public String generateRandomBirth() {
        Random random = new Random();
        // 첫째 자리: 9 또는 0
        int firstDigit = random.nextBoolean() ? 9 : 0;
        // 둘째 자리: 0 ~ 9
        int secondDigit = random.nextInt(10);
        // 셋째, 넷째 자리: 01 ~ 12 (월)
        int month = random.nextInt(12) + 1;
        // 다섯째, 여섯째 자리: 01 ~ 31 (일)
        int day = random.nextInt(31) + 1;
        // 문자열로 변환하여 2자리수 맞추기
        String birth = String.format("%d%d%02d%02d", firstDigit, secondDigit, month, day);
        return birth;
    }

    // 랜덤 greeting 생성
    public String generateRandomGreeting() {
        String[] greeting = {"안녕하세요 좋은 메이트 구해요", "반가워요", "안녕 방가방가", "반가워", "안녕하십니까", "반갑습니다", "안녕하십니까", "메롱롱"};
        return greeting[(int) (Math.random() * greeting.length)];
    }

    // 랜덤 mbti 생성
    public String generateRandomMbti() {
        String[] mbti = {"isfj", "isfp", "infj", "infp", "istj", "istp", "intj", "intp", "estp", "estj", "esfp", "esfj", "enfp", "enfj", "entp", "entj"};
        return mbti[(int) (Math.random() * mbti.length)];
    }
}