package com.example.danjamserver.mate.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.example.danjamserver.common.service.MinioService;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.mate.dto.FilterOutputDTO;
import com.example.danjamserver.util.exception.InvalidResourceException;
import com.example.danjamserver.util.exception.ResultCode;
import com.example.danjamserver.util.exception.S3ServerException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MateFilterService {

    private final MinioService minioService;
    private final SchoolRepository schoolRepository;

    @Transactional
    public FilterOutputDTO getCollegesByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // 사용자 정보로부터 학교 정보 가져오기
        User user = customUserDetails.getUser();
        Long schoolId = user.getSchool().getId();
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new InvalidResourceException(ResultCode.CAN_NOT_FIND_SCHOOL));


        // 단과대 정보를 얻기 위한 학교 이름을 통해 파일 경로 생성
        String schoolName = school.getName();
        String schoolKorName = school.getKorName();//학교 한글 이름
        String filePath = schoolName + ".json";
        Set<String> collegNames = new HashSet<>();


        // 기숙사 정보를 얻기 위한 성별에 따른 파일 이름 생성
        String gender = user.getGender() == 1 ? "male" : "female";
        String bucketName = "dormitory";
        String objectName = gender + "_" + schoolName + ".json";

        try {
            // 단과대 정보 가져오기
            JsonNode majors = minioService.getFile("majors", filePath);

            // JSON 데이터에서 값만 리스트로 추출
            Iterator<JsonNode> elements = majors.elements();
            while (elements.hasNext()) {
                JsonNode department = elements.next();
                collegNames.add(department.asText());
            }

            // 기숙사 정보 가져오기
            JsonNode dormitoryInfo = minioService.getFile(bucketName, objectName);

            // DTO에 담아 반환
            collegNames.remove("고려대학교 세종캠퍼스");
            FilterOutputDTO filterOutputDTO = new FilterOutputDTO();
            filterOutputDTO.setCollegeNames(collegNames);
            filterOutputDTO.setSchoolName(schoolKorName);
            filterOutputDTO.setDormitoryInfo(dormitoryInfo);
            filterOutputDTO.setGender(user.getGender());

            return filterOutputDTO;

        } catch (Exception e) {
            e.printStackTrace();
            // 오류 시 JSON 오류 메시지 반환
            throw new S3ServerException();
        }
    }
}
