package com.example.danjamserver.foodMate.service;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.domain.HopeMenu;
import com.example.danjamserver.foodMate.domain.MealMateTime;
import com.example.danjamserver.foodMate.dto.FoodMateProfileInputDTO;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.Menu;
import com.example.danjamserver.foodMate.repository.FoodMateProfileRepository;
import com.example.danjamserver.foodMate.repository.HopeMenuRepository;
import com.example.danjamserver.foodMate.repository.MealMateTimeRepository;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.springSecurity.dto.CustomUserDetails;
import com.example.danjamserver.user.domain.MyProfile;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.MyProfileRepository;
import com.example.danjamserver.util.exception.InvalidRequestException;
import com.example.danjamserver.util.exception.ResultCode;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FoodMateService {
    private final FoodMateProfileRepository foodMateProfileRepository;
    private final HopeMenuRepository hopeMenuRepository;
    private final MealMateTimeRepository mealMateTimeRepository;
    private final MyProfileRepository myProfileRepository;

    @Transactional
    public void createFoodMateProfile(FoodMateProfileInputDTO foodMateProfileInputDto, CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();

        //이미 MyProfile이 존재하는지 확인
        if(!isMyProfileAlreadySetting(user)){
            throw new InvalidRequestException(ResultCode.MYPROFILE_REQUIRED);
        }

        //FoodMate 가 이미 존재하면 Exception 처리 40300
        FoodMateProfile foodMateProfileCheck = foodMateProfileRepository.findByUserId(user.getId()).orElse(null);
        if (foodMateProfileCheck != null) {
            throw new InvalidRequestException(ResultCode.ALREADY_EXIST_PROFILE);
        }

        // FoodMateDto 를 builder 패턴으로 Entity 저장
        FoodMateProfile foodMateProfile = FoodMateProfile.builder()
                .mealTime(foodMateProfileInputDto.getMealTime())
                .diningManner(foodMateProfileInputDto.getDiningManner())
                .mateType(MateType.FOODMATE)
                .user(user)
                .build();

        foodMateProfile.setAllergiesMap(foodMateProfileInputDto.getAllergies());

        foodMateProfileRepository.save(foodMateProfile);

        //식사메이트 필요시간 저장
        Set<MateTime> mateTime = foodMateProfileInputDto.getMateTime();

        for (MateTime mateTimes : mateTime) {
            MealMateTime mealMateTimeEntity = MealMateTime.builder()
                    .foodMateProfile(foodMateProfile)
                    .mateTime(mateTimes)
                    .build();
            mealMateTimeRepository.save(mealMateTimeEntity);
        }

        //선호 음식 저장
        Set<Menu> menuList = foodMateProfileInputDto.getMenu();

        for (Menu menus : menuList) {
            HopeMenu menuEntity = HopeMenu.builder()
                    .foodMateProfile(foodMateProfile)
                    .menus(menus)
                    .build();
            hopeMenuRepository.save(menuEntity);
        }
    }

    public boolean isMyProfileAlreadySetting(User user){
        boolean isAlreadySetting = true;
        MyProfile myProfile = myProfileRepository.findByUserId(user.getId()).orElse(null);
        if(myProfile == null || myProfile.getMbti() == null){
            isAlreadySetting = false;
        }
        return isAlreadySetting;
    }
}

