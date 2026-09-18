package com.RoomMateX.repository;

import com.RoomMateX.entity.Match;
import com.RoomMateX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface MatchRepository extends JpaRepository<Match,Long> {

    @Query("""
      SELECT m FROM Match m
      WHERE m.user1 = :user OR m.user2 = :user
      ORDER BY m.score DESC
    """)
    List<Match> findMyMatches(@Param("user") User user);

    boolean existsByUser1AndUser2(User u1, User u2);
    Optional<Match> findByUser1AndUser2(User u1, User u2);

    @Query("""
 SELECT m FROM Match m
 WHERE (m.user1=:u OR m.user2=:u)
 AND m.user1Approved=true
 AND m.user2Approved=true
""")
    List<Match> findConfirmedMatches(User u);

    @Query("""
 SELECT m FROM Match m
 WHERE (m.user1=:u OR m.user2=:u)
 AND m.user1Approved=true
 AND m.user2Approved=true
 ORDER BY m.score DESC
""")
    List<Match> findRankedMatches(User u);

}
