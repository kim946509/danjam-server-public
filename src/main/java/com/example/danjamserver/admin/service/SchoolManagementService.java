package com.example.danjamserver.admin.service;


import com.example.danjamserver.admin.dto.SchoolRequest;
import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolManagementService {

    private final SchoolRepository schoolRepository;


    /**
     * 새로운 학교를 저장합니다.
     *
     * @param schoolRequest 학교 정보 요청 객체 ( 학교 이름, 학교 한글 이름 )
     */
    public void saveSchool(SchoolRequest schoolRequest) {
        School school = new School();
        school.setName(schoolRequest.getSchoolName());
        school.setKorName(schoolRequest.getSchoolKorName());
        schoolRepository.save(school);
    }

    /**
     * 주어진 ID를 가진 학교를 삭제합니다.
     *
     * @param schoolId 삭제할 학교의 ID
     */
    public void deleteById(String schoolId) {
        long id;
        try {
            id = Long.parseLong(schoolId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 학교 ID 형식입니다.");
        }

        if (!schoolRepository.existsById(id)) {
            throw new IllegalArgumentException("ID가 " + schoolId + "인 학교를 찾을 수 없습니다.");
        }

        schoolRepository.deleteById(id);
    }
}