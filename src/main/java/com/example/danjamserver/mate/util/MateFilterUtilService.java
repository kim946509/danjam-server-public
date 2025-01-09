package com.example.danjamserver.mate.util;

import com.example.danjamserver.mate.domain.SchoolMajors;
import com.example.danjamserver.mate.repository.SchoolMajorsRepository;
import com.example.danjamserver.util.exception.InvalidResourceException;
import com.example.danjamserver.util.exception.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MateFilterUtilService {

    private final SchoolMajorsRepository schoolMajorsRepositors;
    // MBTI 필터링 수정(반대되는 mbti를 모두 가지고 있을경우 조건 제거)
    public static String fixFilterlingMBTI(String originMbti) {

        String mbti = originMbti;
        mbti = mbti.chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());

        if (mbti == null) return "";

        // i와e를 모두 포함하는 경우 모두 제거
        if (mbti.contains("i") && mbti.contains("e")) {
            mbti = mbti.replace("i", "").replace("e", "");
        }

        // n과s를 모두 포함하는 경우 모두 제거
        if (mbti.contains("n") && mbti.contains("s")) {
            mbti = mbti.replace("n", "").replace("s", "");
        }

        // t와f를 모두 포함하는 경우 모두 제거
        if (mbti.contains("t") && mbti.contains("f")) {
            mbti = mbti.replace("t", "").replace("f", "");
        }

        // j와p를 모두 포함하는 경우 모두 제거
        if (mbti.contains("j") && mbti.contains("p")) {
            mbti = mbti.replace("j", "").replace("p", "");
        }
        return mbti;
    }

    public static String convertoYear4Digits(String num) {
        // 필터링의 2자리수로 입력된 생년을 4자리수로 변경. 50년 이전은 20년대로, 50년 이후는 19년대로 변경
        if (Integer.parseInt(num) > 50)
            num = "19" + num;
        else
            num = "20" + num;
        return num;
    }

    public static int convertoYear4Digits(int num) {
        // 필터링의 2자리수로 입력된 생년을 4자리수로 변경. 50년 이전은 20년대로, 50년 이후는 19년대로 변경
        if (num > 50)
            num =1900 + num;
        else
            num = 2000 + num;
        return num;
    }

    // db에서 단과대명 가져오기
    public String getCollegeByMajor(Long schoolId, String major) {
        // schoolId와 major로 단과대명 가져오기
        SchoolMajors schoolMajors = schoolMajorsRepositors.findBySchoolIdAndMajor(schoolId, major).orElse(null);
        if(schoolMajors == null) throw new InvalidResourceException(ResultCode.CAN_NOT_FIND_MAJOR);
        return schoolMajors.getCollege();
    }
}
