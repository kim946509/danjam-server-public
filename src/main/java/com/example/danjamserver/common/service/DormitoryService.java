package com.example.danjamserver.common.service;

import com.example.danjamserver.common.domain.School;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import com.example.danjamserver.util.exception.S3ServerException;
import com.example.danjamserver.util.response.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DormitoryService {

    private final MinioService minioService;
    private final UserRepository userRepository;

    @Autowired
    public DormitoryService(MinioService minioService, UserRepository userRepository) {
        this.minioService = minioService;
        this.userRepository = userRepository;
    }

    public RestResponse<?> getDormitoryInfo(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        JsonNode dormitoryInfo = getDormitoryFile(user);
        return RestResponse.success(dormitoryInfo);
    }

    public JsonNode getDormitoryFile(User user) {
        School school = user.getSchool();
        String schoolName = school.getName();

        String gender = user.getGender() == 1 ? "male" : "female";

        String bucketName = "dormitory";
        String objectName = gender + "_" + schoolName + ".json";
        try {
            JsonNode dormitoryInfo = minioService.getFile(bucketName, objectName);
            return dormitoryInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new S3ServerException();
        }
    }
}
