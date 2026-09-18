package com.RoomMateX.service;

import com.RoomMateX.entity.User;
import com.RoomMateX.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MLService {

    private final UserRepository userRepo;

    // ================= RETRAIN =================

    @CacheEvict(value = {"matches", "topMatches"}, allEntries = true)
    public void retrain() {
        System.out.println("ML retrain triggered (heuristic mode)");
    }

    // ================= COMPATIBILITY =================

    public int compatibility(User a, User b){
        return heuristic(a, b);
    }

    // ================= HEURISTIC =================

    private int heuristic(User a, User b){

        int score = 0;

        if(a == null || b == null) return 0;

        // Age
        if(Math.abs(a.getAge() - b.getAge()) < 5)
            score += 20;

        // Budget
        if(Math.abs(a.getBudget() - b.getBudget()) < 3000)
            score += 20;

        // City
        if(a.getAddress() != null && b.getAddress() != null &&
                a.getAddress().equalsIgnoreCase(b.getAddress()))
            score += 20;

        // Occupation
        if(a.getOccupation() != null && b.getOccupation() != null &&
                a.getOccupation().equalsIgnoreCase(b.getOccupation()))
            score += 20;

        // Gender preference
        if(a.getPreference() != null &&
                b.getGender() != null &&
                a.getPreference().getGenderPreference() != null &&
                a.getPreference().getGenderPreference()
                        .equalsIgnoreCase(b.getGender()))
            score += 20;

        return Math.min(score, 100);
    }

    // ================= RECOMMEND =================

    @Cacheable("matches")
    public List<User> recommend(User me, int n){

        return userRepo.findAll()
                .stream()
                .filter(u -> !u.getId().equals(me.getId()))   // remove self SAFELY
                .sorted((a,b) -> compatibility(me,b) - compatibility(me,a)) // DESC
                .limit(n)
                .toList();
    }
}
