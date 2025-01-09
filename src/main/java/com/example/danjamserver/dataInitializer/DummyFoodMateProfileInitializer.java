package com.example.danjamserver.dataInitializer;

import com.example.danjamserver.foodMate.domain.FoodMateProfile;
import com.example.danjamserver.foodMate.domain.HopeMenu;
import com.example.danjamserver.foodMate.domain.MealMateTime;
import com.example.danjamserver.foodMate.enums.DiningManner;
import com.example.danjamserver.foodMate.enums.MateTime;
import com.example.danjamserver.foodMate.enums.MealTime;
import com.example.danjamserver.foodMate.enums.Menu;
import com.example.danjamserver.foodMate.repository.FoodMateProfileRepository;
import com.example.danjamserver.foodMate.repository.HopeMenuRepository;
import com.example.danjamserver.foodMate.repository.MealMateTimeRepository;
import com.example.danjamserver.mate.domain.MateType;
import com.example.danjamserver.user.domain.User;
import com.example.danjamserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(3)
public class DummyFoodMateProfileInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FoodMateProfileRepository foodMateProfileRepository;
    private final HopeMenuRepository hopeMenuRepository;
    private final MealMateTimeRepository mealMateTimeRepository;
    private final Random random = new Random();

    @Override
    public void run(ApplicationArguments args) {

        // 더미 유저가 존재하는지 확인. 존재하지 않으면 더미 푸드 메이트 프로필 생성하지 않음
        String checkUsername = "test" + DummyUsersInitializer.startDummyUserNumber;
        User checkUser = userRepository.findByUsername(checkUsername).orElse(null);
        if (checkUser == null) {
            return;
        }

        // 이미 더미 유저의 푸드 메이트 프로필이 존재하는지 확인. 존재하면 더미 푸드 메이트 프로필 생성하지 않음
        FoodMateProfile checkFoodMateProfile = foodMateProfileRepository.findByUserId(checkUser.getId()).orElse(null);
        if(checkFoodMateProfile != null) {
            return;
        }

        // 더미 푸드 메이트 프로필 생성
        String dummyUsername = "test";
        for(int i = DummyUsersInitializer.startDummyUserNumber; i<=DummyUsersInitializer.endDummyUserNumber; i++) {
            User user = userRepository.findByUsername(dummyUsername + i).orElse(null);
            if(user == null) {
                continue;
            }

            // 이미 해당 더미유저의 FoodMateProfile이 존재하는지 확인
            FoodMateProfile checkUserFoodMateProfile = foodMateProfileRepository.findByUserId(user.getId()).orElse(null);
            if(checkUserFoodMateProfile != null) {
                continue;
            }

            // FoodMateProfile 생성 및 저장
            FoodMateProfile foodMateProfile = setRandomFoodMateProfile(user);

            //선호 메뉴 저장
            setRandomHopeMenu(foodMateProfile);

            //선호 MateTime 저장
            setRandomMateTime(foodMateProfile);

        }
    }

    // FoodMateProfile 생성 및 저장
    private FoodMateProfile setRandomFoodMateProfile(User user) {
        FoodMateProfile foodMateProfile = FoodMateProfile.builder()
                .mealTime(getRandomMealTime())
                .diningManner(getRandomDiningManner())
                .mateType(MateType.FOODMATE)
                .user(user)
                .build();

        foodMateProfile.setAllergiesMap(getRandomAllergies());
        foodMateProfileRepository.save(foodMateProfile);
        return foodMateProfile;
    }

    //선호 메뉴 저장
    private void setRandomHopeMenu(FoodMateProfile foodMateProfile) {
        Set<Menu> selectedHopeMenus = getRandomHopeMenu(foodMateProfile);
        for (Menu menu : selectedHopeMenus) {
            HopeMenu hopeMenu = HopeMenu.builder()
                    .foodMateProfile(foodMateProfile)
                    .menus(menu)
                    .build();
            hopeMenuRepository.save(hopeMenu);
        }
    }

    //선호 MateTime 저장
    private void setRandomMateTime(FoodMateProfile foodMateProfile) {
        Set<MateTime> selectedMateTimes = getRandomMateTime();
        for (MateTime mateTime : selectedMateTimes) {
            mealMateTimeRepository.save(MealMateTime.builder()
                    .foodMateProfile(foodMateProfile)
                    .mateTime(mateTime)
                    .build());
        }
    }

    private MealTime getRandomMealTime() {
        MealTime[] mealTimes = MealTime.values();
        return mealTimes[random.nextInt(mealTimes.length)];
    }

    private DiningManner getRandomDiningManner() {
        DiningManner[] diningmanners = DiningManner.values();
        return diningmanners[random.nextInt(diningmanners.length)];
    }

    private Map<String,Object> getRandomAllergies() {
        List<String> allergies = List.of("계란", "우유", "땅콩", "대두", "밀", "고등어", "게", "새우", "돼지고기", "복숭아", "토마토", "갑각류", "호두", "닭고기", "쇠고기", "오징어", "조개류(굴,전복,홍합 등)");
        Set<String> allergySet = new HashSet<>();

        //랜덤으로 0~3개의 알러지를 가지도록 설정
        int randomAllergyCount = random.nextInt(4);
        while (allergySet.size() < randomAllergyCount) {
            allergySet.add(allergies.get(random.nextInt(allergies.size())));
        }

        Map<String, Object> allergiesMap = new HashMap<>();
        String number = "1";
        for(String allergy : allergySet) {
            allergiesMap.put(number, allergy);
            number = String.valueOf(Integer.parseInt(number) + 1);
        }
        return allergiesMap;
    }

    //선호 메뉴 랜덤 생성
    private Set<Menu> getRandomHopeMenu(FoodMateProfile foodMateProfile) {
        List<Menu> hopeMenus = Arrays.stream(Menu.values()).toList();
        Set<Menu> selectedHopeMenus = new HashSet<>();

        //랜덤으로 1~6개의 선호 메뉴를 선택
        int randomHopeMenuCount = random.nextInt(6) + 1;
        while (selectedHopeMenus.size() < randomHopeMenuCount) {
            selectedHopeMenus.add(hopeMenus.get(random.nextInt(hopeMenus.size())));
        }
        return selectedHopeMenus;
    }


    //랜덤 MateTime 생성
    private Set<MateTime> getRandomMateTime() {
        List<MateTime> mateTimes = Arrays.stream(MateTime.values()).toList();
        Set<MateTime> selectedMateTimes = new HashSet<>();

        //랜덤으로 1~4개의 MateTime을 선택
        int randomMateTimeCount = random.nextInt(4) + 1;
        while (selectedMateTimes.size() < randomMateTimeCount) {
            selectedMateTimes.add(mateTimes.get(random.nextInt(mateTimes.size())));
        }
        return selectedMateTimes;
    }
}
