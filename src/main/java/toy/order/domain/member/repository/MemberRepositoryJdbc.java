package toy.order.domain.member.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import toy.order.domain.member.Member;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Repository
public class MemberRepositoryJdbc implements MemberRepository {
    private final JdbcTemplate template;

    public MemberRepositoryJdbc(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(login_id, name, password, uuid) values (?, ?, ?, ?)";
        template.update(sql, member.getLoginId(), member.getName(), member.getPassword(), member.getUuid());
        return member;
    }

    @Override
    public void update(String uuid, String name, String loginId, String password) {
        String sql = "update member set login_id=?, name=?, password=? where uuid=?";
        template.update(sql, loginId, name, password, uuid);
    }

    @Override
    public void updateBalance(Long memberId, int amount){
        String sql = "update member set balance=? where member_id=?";
        template.update(sql, amount, memberId);
    }

    @Override
    public void delete(String loginId) {
        String sql = "delete from member where login_id=?";
        template.update(sql, loginId);
    }

    @Override
    public Long findMemberIdByLoginId(String loginId) {
        String sql = "select member_id from member where login_id=?";
        return template.queryForObject(sql, Long.class, loginId);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "select * from member where member_id = ?";
        return Optional.ofNullable(template.queryForObject(sql, memberRowMapper(), memberId));
    }

    @Override
    public Optional<Member> findByUuid(String uuid) {
        String sql = "select * from member where uuid = ?";
        return Optional.ofNullable(template.queryForObject(sql, memberRowMapper(), uuid));
    }

    //쿼리 실행 결과를 객체로 매핑하여 반환
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getLong(1));
            member.setLoginId(rs.getString(2));
            member.setName(rs.getString(3));
            member.setPassword(rs.getString(4));
            member.setUuid(rs.getString(5));
            member.setBalance(rs.getInt(6));
            return member;
        };
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = ?";
        return template.query(sql, memberRowMapper(), loginId).stream().findFirst();
    }

    @Override
    public Integer findBalanceByUuid(String uuid) {
        String sql = "select balance from member where uuid = ?";
        return template.queryForObject(sql, Integer.class, uuid);
    }

    @Override
    public String findNameByUuid(String uuid) {
        String sql = "select name from member where uuid = ?";
        return template.queryForObject(sql, String.class, uuid);
    }

    @Override
    public String findLoginIdByUuid(String uuid){
        String sql = "select login_id from member where uuid = ?";
        return template.queryForObject(sql, String.class, uuid);
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        return template.query(sql, memberRowMapper());
    }

    // 로그인 ID 중복 검사
    public boolean existsByLoginId(String loginId) {
        String sql = "SELECT COUNT(*) FROM member WHERE login_id = ?";
        Integer count = template.queryForObject(sql, Integer.class, loginId);
        return count != null && count > 0; // 중복이 있으면 true 반환
    }

    public boolean existsByUuid(String uuid) {
        String sql = "SELECT COUNT(*) FROM member WHERE uuid = ?";
        Integer count = template.queryForObject(sql, Integer.class, uuid);
        return count != null && count > 0;
    }
}
