package com.example.jpa.notice.repository;

import com.example.jpa.user.model.UserLogCount;
import com.example.jpa.user.model.UserNoticeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserCustomRepository {   // JPA 레파지토리를 상속받지 않는 커스텀 레파지토리

    private final EntityManager entityManager;

    public List<UserNoticeCount> findByUserNoticeCount() {

        String sql = " select u.id, u.email, u.user_name, "
                + "(select count(*) from Notice n where n.user_id = u.id) notice_count from User u ";

        List<UserNoticeCount> list = entityManager.createNativeQuery(sql).getResultList();

        return list;
    }

    public List<UserLogCount> findUserLogCount() {
        String sql = " select u.id, u.email, u.user_name "
                + ", (select count(*) from Notice n where n.user_id = u.id) notice_count "
                + ", (select count(*) from Notice_Like nl where nl.user_id = u.id) notice_like_count "
                + "from user u ";

        List<UserLogCount> list = entityManager.createNativeQuery(sql).getResultList();

        return list;
    }

    public List<UserLogCount> findUserLikeBest() {
        String sql = "" +
                " select t1.id, t1.email, t1.user_name, t1.notice_like_count " +
                " from " +
                " ( " +
                " select u.*, (select count(*) from notice_like nl where nl.user_id = u.id) as notice_like_count " +  // noticeLikeCount 를 가져와서
                " from user u" +
                " ) t1" +
                " order by t1.notice_like_count desc";   // 내림차순 정렬한다.

        List<UserLogCount> list = entityManager.createNativeQuery(sql).getResultList();

        return list;
    }
}
