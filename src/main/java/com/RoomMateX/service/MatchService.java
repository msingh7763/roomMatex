package com.RoomMateX.service;

import com.RoomMateX.dto.MatchDTO;
import com.RoomMateX.entity.Match;
import com.RoomMateX.entity.User;
import com.RoomMateX.repository.MatchRepository;
import com.RoomMateX.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepo;
    private final MatchRepository matchRepo;
    private final NotificationService notificationService;
    private final MLService mlService;

    // ---------------- GENERATE MATCHES ----------------

    public void generateMatches(User newUser){

        List<User> users = userRepo.findAll();

        for(User u : users){

            if(u.getId().equals(newUser.getId())) continue;

            if(matchRepo.existsByUser1AndUser2(newUser,u)) continue;

            int score = mlService.compatibility(newUser,u);

            if(score >= 50){

                matchRepo.save(
                        Match.builder()
                                .user1(newUser)
                                .user2(u)
                                .score(score)
                                .user1Approved(false)
                                .user2Approved(false)
                                .build()
                );
            }
        }
    }

    // ---------------- MY MATCHES ----------------

    @Cacheable(value="topMatches", key="#me.id")
    public List<MatchDTO> myMatches(User me){

        return matchRepo.findRankedMatches(me)
                .stream()
                .map(m -> {

                    User other =
                            m.getUser1().getId().equals(me.getId())
                                    ? m.getUser2()
                                    : m.getUser1();

                    return MatchDTO.builder()
                            .id(m.getId())
                            .userId(other.getId())
                            .name(other.getName())
                            .age(other.getAge())
                            .city(other.getAddress())
                            .budget(other.getBudget())
                            .score(m.getScore())
                            .build();

                }).toList();
    }

    // ---------------- LIKE USER ----------------

    @CacheEvict(value="topMatches", allEntries=true)
    @Transactional
    public void likeUser(User me, Long targetId){

        User other = userRepo.findById(targetId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Match match = matchRepo
                .findByUser1AndUser2(me,other)
                .orElseGet(() -> Match.builder()
                        .user1(me)
                        .user2(other)
                        .score(mlService.compatibility(me,other))
                        .user1Approved(false)
                        .user2Approved(false)
                        .build());

        // Approval
        if(match.getUser1().getId().equals(me.getId()))
            match.setUser1Approved(true);
        else
            match.setUser2Approved(true);

        matchRepo.save(match);

        // Mutual match
        if(Boolean.TRUE.equals(match.getUser1Approved())
                && Boolean.TRUE.equals(match.getUser2Approved())){

            notificationService.notify(other,"🎉 New Match!");
            notificationService.notify(me,"🎉 New Match!");
        }
    }

    // ---------------- TOP N ----------------

    public List<MatchDTO> topMatches(User me,int limit){
        return myMatches(me).stream().limit(limit).toList();
    }
}
