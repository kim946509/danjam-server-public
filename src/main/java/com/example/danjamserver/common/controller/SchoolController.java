package com.example.danjamserver.common.controller;

import com.example.danjamserver.common.dto.SchoolResponseDTO;
import com.example.danjamserver.common.service.SchoolService;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/school")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping("/{schoolId}/major")
    public ResponseEntity<JsonNode> getMajorsBySchool(@PathVariable long schoolId) {
        JsonNode majorsInfo = schoolService.getMajorsBySchool(schoolId);

        if (majorsInfo.has("error")) {
            return ResponseEntity.internalServerError().body(majorsInfo);
        }

        return ResponseEntity.ok(majorsInfo);
    }

    @GetMapping
    public ResponseEntity<List<SchoolResponseDTO>> getSchoolList() {
        List<SchoolResponseDTO> schools = schoolService.getSchools();
        if (schools.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(schools, HttpStatus.OK);
    }
}