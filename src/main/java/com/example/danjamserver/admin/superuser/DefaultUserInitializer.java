package com.example.danjamserver.admin.superuser;

import com.example.danjamserver.mate.domain.SchoolMajors;
import com.example.danjamserver.mate.repository.SchoolMajorsRepository;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.springSecurity.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DefaultUserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SchoolMajorsRepository schoolMajorsRepository;

    @Autowired
    public DefaultUserInitializer(UserRepository userRepository, SchoolRepository schoolRepository, SchoolMajorsRepository schoolMajorsRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.schoolMajorsRepository = schoolMajorsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Check if the superuser already exists
        if (!userRepository.existsByUsername("admin")) {

            // Create a school
            //고려대학교 세종
            if (schoolRepository.findByName("korea_sejong").isEmpty()) {
                School basicSchool = new School();
                basicSchool.setName("korea_sejong");
                basicSchool.setKorName("고려대학교 세종캠퍼스");
                schoolRepository.save(basicSchool);
                // 학과 데이터 추가
                // 학과 데이터 추가
                String[][] koreaSejongMajors = {
                        {"응용수리과학부", "데이터계산과학과"},
                        {"디스플레이·반도체물리학부", "디스플레이융합학과"},
                        {"디스플레이·반도체물리학부", "반도체물리학과"},
                        {"과학기술대학", "신소재화학과"},
                        {"과학기술대학", "컴퓨터융합소프트웨어학과"},
                        {"과학기술대학", "전자및정보공학과"},
                        {"과학기술대학", "생명정보공학과"},
                        {"과학기술대학", "식품생명공학과"},
                        {"과학기술대학", "전자·기계융합공학과"},
                        {"과학기술대학", "환경시스템공학과"},
                        {"과학기술대학", "미래모빌리티학과"},
                        {"과학기술대학", "지능형반도체공학과"},
                        {"과학기술대학", "인공지능사이버보안학과"},
                        {"약학대학", "약학과"},
                        {"글로벌학부", "한국학전공"},
                        {"글로벌학부", "중국학전공"},
                        {"글로벌학부", "영미학전공"},
                        {"융합경영학부", "글로벌경영전공"},
                        {"융합경영학부", "디지털경영전공"},
                        {"글로벌비즈니스대학", "표준·지식학과"},
                        {"정부행정학부", "공공정책대학"},
                        {"공공사회·통일외교학부", "공공사회학전공"},
                        {"공공사회·통일외교학부", "통일외교안보"},
                        {"경제통계학부", "경제정책학전공"},
                        {"공공정책대학", "빅데이터사이언스학부"},
                        {"국제스포츠학부", "스포츠과학전공"},
                        {"국제스포츠학부", "스포츠비즈니스전공"},
                        {"문화스포츠대학", "문화유산융합학부"},
                        {"문화창의학부", "미디어문예창작"},
                        {"문화창의학부", "문화콘텐츠"},
                        {"스마트도시학부", "스마트도시학부"}
                };
                for (String[] major : koreaSejongMajors) {
                    SchoolMajors schoolMajor = new SchoolMajors();
                    schoolMajor.setCollege(major[0]);
                    schoolMajor.setMajor(major[1]);
                    schoolMajor.setSchool(schoolRepository.findByName("korea_sejong").get());
                    schoolMajorsRepository.save(schoolMajor);
                }
            }
            // 홍익대학교 세종
            if (schoolRepository.findByName("hongik_sejong").isEmpty()) {
                School basicSchool = new School();
                basicSchool.setName("hongik_sejong");
                basicSchool.setKorName("홍익대학교 세종캠퍼스");
                schoolRepository.save(basicSchool);

                // 학과 데이터 추가
                // 학과 데이터 추가
                String[][] hongikSejongMajors = {
                        {"과학기술대학", "자율전공"},
                        {"과학기술대학", "전자전기융합공학과"},
                        {"과학기술대학", "소프트웨어융합학과"},
                        {"과학기술대학", "나노신소재학과"},
                        {"과학기술대학", "기계정보공학과"},
                        {"과학기술대학", "조선해양공학과"},
                        {"과학기술대학", "바이오화학공학과"},
                        {"건축공학부", "건축공학전공"},
                        {"건축공학부", "건축디자인전공"},
                        {"상경학부", "글로벌경영전공"},
                        {"상경학부", "회계학전공"},
                        {"상경학부", "금융보험학전공"},
                        {"게임학부", "게임소프트웨어전공"},
                        {"게임학부", "게임그래픽디자인전공"},
                        {"조형대학", "디자인컨버전스학부"},
                        {"조형대학", "영상/애니메이션학부"},
                        {"광고홍보학부", "광고홍보학부"},
                        {"캠퍼스자율전공", "캠퍼스자율전공"},
                        {"산업스포츠학과", "산업스포츠학과"}
                };
                for (String[] major : hongikSejongMajors) {
                    SchoolMajors schoolMajor = new SchoolMajors();
                    schoolMajor.setCollege(major[0]);
                    schoolMajor.setMajor(major[1]);
                    schoolMajor.setSchool(schoolRepository.findByName("hongik_sejong").get());
                    schoolMajorsRepository.save(schoolMajor);
                }
            };

            // Create a dummy school
            School dummySchool = new School();
            dummySchool.setName("Dummy School");
            dummySchool = schoolRepository.save(dummySchool);

            // Create MyProfile for admin
            MyProfile adminProfile = new MyProfile();
            adminProfile.setMajor("Administrator"); // 기본 정보로 임의 값 설정
            // 추가로 필요한 MyProfile 필드 설정 가능

            // Create the superuser
            User admin = new User();
            admin.setUsername("admin");
            admin.setAuthImgUrl("");
            admin.setNickname("admin");
            admin.setResidentImgUrl("");
            admin.setEmail("admin@admin.com");
            admin.setGender(9999);
            admin.setMyProfile(adminProfile); // 생성한 MyProfile 설정
            admin.setSchool(dummySchool);
            admin.setPassword(bCryptPasswordEncoder.encode("q1w2e3r4!")); // Encode the password
            admin.setRole(Role.ADMIN); // Assuming you have a Role enum
            userRepository.save(admin);
        }
    }
}