package toy.order.domain.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;
import toy.order.domain.item.dto.ItemUpdateDto;
import toy.order.domain.member.Member;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@Repository
@Slf4j
public class ItemRepositoryJdbc implements ItemRepository {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    KeyHolder keyHolder = new GeneratedKeyHolder(); //id로 key 자동 생성

    public ItemRepositoryJdbc(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("item")
                .usingGeneratedKeyColumns("ITEM_ID");
    }

    @Override
    public Item save(Item item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        Number key = jdbcInsert.executeAndReturnKey(param);
        item.setItemId(key.longValue());
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto form) {
        String sql = "update item" +
                     " set item_name=:itemName, price=:price, quantity=:quantity" +
                     " where item_id=:itemId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", form.getItemName())
                .addValue("price", form.getPrice())
                .addValue("quantity", form.getQuantity())
                .addValue("itemId", itemId);

        jdbcTemplate.update(sql, param);
    }

    @Override
    public void updateCnt(Item item, Integer quantity) {
        String sql = "update item set quantity =:quantity " +
                     "where item_id =:itemId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemId", item.getItemId())
                .addValue("quantity", quantity);

        jdbcTemplate.update(sql, param);
    }

    @Override
    public void delete(Long itemId) {
        String sql = "delete from item where item_id =:itemId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemId", itemId);
        jdbcTemplate.update(sql, param);
    }

    @Override
    public Long findItemIdByItemNameAndMember(String itemName, Member member) {
        String sql = """
        SELECT i.item_id
        FROM item i
        JOIN member m ON i.member_id = m.member_id
        WHERE i.item_name =:itemName AND m.member_id =:memberId
    """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("itemName", itemName)
                .addValue("member", member);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    @Override
    public Member findMemberByItemId(Long itemId) {
        String sql = """
    SELECT m.*
    FROM member m
    JOIN item i ON i.member_id = m.member_id
    WHERE i.item_id = :itemId
    """;

        Map<String, Object> params = new HashMap<>();
        params.put("itemId", itemId);

        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Member.class));
    }



    @Override
    public Optional<Item> findByItemId(Long itemId){

        String sql = "select item_id, item_name, price, quantity, member_id from item where item_id =:itemId";
        try{
            Map<String, Object> param = Map.of("itemId", itemId);
            Item item = jdbcTemplate.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        // MapSqlParameterSource로 변경
        MapSqlParameterSource param = new MapSqlParameterSource();
        if (StringUtils.hasText(itemName)) {
            param.addValue("itemName", itemName);
        }
        if (maxPrice != null) {
            param.addValue("maxPrice", maxPrice);
        }

        String sql = "select item_id, item_name, price, quantity, member_id from item";

        // 동적 쿼리 구성
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("sql={}", sql);
        return jdbcTemplate.query(sql, param, itemRowMapper());
    }

    @Override
    public Double findPriceByItemId(Long itemId) {
        String sql = "select price from item where item_id = :itemId";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemId", itemId);

        return jdbcTemplate.queryForObject(sql, param, Double.class);
    }


    @Override
    public List<Item> findAll() {
        String sql = "select * from item";
        return jdbcTemplate.query(sql, itemRowMapper());
    }

    //쿼리 실행 결과를 객체로 매핑하여 반환
    private RowMapper<Item> itemRowMapper() {
            return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
