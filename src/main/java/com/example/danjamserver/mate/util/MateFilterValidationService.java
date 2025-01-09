package com.example.danjamserver.mate.util;

import java.util.Set;

import static com.example.danjamserver.mate.util.MateFilterUtilService.convertoYear4Digits;

public class MateFilterValidationService {

    public static void validateBirthYear(String minBirthYear, String maxBirthYear, StringBuilder errorMessage) {
        boolean isValid = true;
        if(minBirthYear!=null){
            //2자리 숫자로 입력받아야함
            if(!minBirthYear.matches("^[0-9]{2}$")){
                errorMessage.append("출생연도는 2자리 숫자로 입력해주세요. ");
                isValid = false;
            }
        }

        //2자리 숫자로 입력받아야함
        if(maxBirthYear!=null) {
            if (!maxBirthYear.matches("^[0-9]{2}$")) {
                errorMessage.append("출생연도는 2자리 숫자로 입력해주세요. ");
                isValid = false;
            }
        }

        if(isValid && minBirthYear!=null && maxBirthYear!=null){
            if(Integer.parseInt(convertoYear4Digits(minBirthYear)) > Integer.parseInt(convertoYear4Digits(maxBirthYear))){
                errorMessage.append("최소 출생연도는 최대출생연도보다 작아야합니다. ");
            }
        }
    }

    public static void validateMbti(String mbti, StringBuilder errorMessage) {
        if(mbti == null){
            errorMessage.append("mbti는 필수 입력값입니다. ");
        }
        else if(!(mbti.length() >= 1 && mbti.length() <= 8)){
            errorMessage.append("mbti는 1자 이상 8자 이하로 선택해주세요. ");
        }
        else if(!mbti.matches("^[iensftpj]+$")){
            errorMessage.append("mbti는 i, e, n, s, f, t, p, j 만 가능합니다. ");
        }
    }

    public static void validateEntryYear(String minEntryYear, String maxEntryYear, StringBuilder errorMessage){

        boolean isValid = true;
        if(minEntryYear!=null){
            //2자리 숫자로 입력받아야함
            if(!minEntryYear.matches("^[0-9]{2}$")){
                errorMessage.append("입학연도는 2자리 숫자로 입력해주세요. ");
                isValid = false;
            }
        }

        if(maxEntryYear!=null){
            //2자리 숫자로 입력받아야함
            if(!maxEntryYear.matches("^[0-9]{2}$")){
                errorMessage.append("입학연도는 2자리 숫자로 입력해주세요. ");
                isValid = false;
            }
        }

        if(isValid && minEntryYear!=null && maxEntryYear!=null){
            if(Integer.parseInt(convertoYear4Digits(minEntryYear)) > Integer.parseInt(convertoYear4Digits(maxEntryYear))){
                errorMessage.append("최소 입학연도는 최대입학연도보다 작아야합니다. ");
            }
        }
    }

    public static void validateGender(Set<Integer> gender, StringBuilder errorMessage) {
        if(gender!=null) {
            //gender 크기가 2이하여야 함
            if(gender.size() > 2 || !gender.stream().allMatch(g -> g == 0 || g == 1)){
                errorMessage.append("성별은 0, 1로 입력해주세요. ");
            }
        }

    }
}
