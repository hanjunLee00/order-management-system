-- 사용자 1
INSERT INTO MEMBER (LOGIN_ID, NAME, PASSWORD, UUID, BALANCE)
VALUES
    ('test1', '유저1', 'test', '4fb76a44-4100-4c9e-9ddb-648ee579b46f', 50000);

-- 사용자 2
INSERT INTO MEMBER (LOGIN_ID, NAME, PASSWORD, UUID, BALANCE)
VALUES
    ('test2', '유저2', 'test', '015222c5-8048-4455-84f8-6afe5c3802a6', 0);


-- 아이템 (사용자 1)
INSERT INTO ITEM (ITEM_NAME, PRICE, QUANTITY, MEMBER_ID)
VALUES
    ('유저1_아이템1', 2000, 5, 1), -- 가격 * 수량 = 12000
    ('유저1_아이템2', 3000, 4, 1), -- 가격 * 수량 = 15000
    ('유저1_아이템3', 2500, 3, 1); -- 가격 * 수량 = 12500

-- 아이템 (사용자 2)
INSERT INTO ITEM (ITEM_NAME, PRICE, QUANTITY, MEMBER_ID)
VALUES
    ('유저2_아이템1', 2000, 5, 2), -- 가격 * 수량 = 12000
    ('유저2_아이템2', 3000, 4, 2), -- 가격 * 수량 = 15000
    ('유저2_아이템3', 2500, 5, 2); -- 가격 * 수량 = 12500
