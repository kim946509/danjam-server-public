package com.example.danjamserver.common.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.common.dto.SchoolResponseDTO;
import com.example.danjamserver.common.repository.SchoolRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final MinioService minioService;
    private final SchoolRepository schoolRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public List<SchoolResponseDTO> getSchools() {
        // 학교 정보에서 id와 한글 이름만 가져옴
        List<School> schools = schoolRepository.findAll();
        return schools.stream()
                .map(school -> modelMapper.map(school, SchoolResponseDTO.class))
                .toList();
    }

    public JsonNode getMajorsBySchool(long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("School not found"));
        String schoolName = school.getName();
        String filePath = schoolName + ".json";

        try {
            return minioService.getFile("majors", filePath);
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 시 JSON 오류 메시지 반환
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("error", "Error retrieving majors information for " + schoolName);
            return errorNode;
        }
    }
}