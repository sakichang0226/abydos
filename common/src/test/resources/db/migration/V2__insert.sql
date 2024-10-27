/* 商品情報用 */
INSERT INTO ITEMS (name, shop_id, category_id, price, purchase_num, stock, is_stopped, description) VALUES
('Sample Item 1', 1, 101, 1000, 10, 50, FALSE, 'Sample description for item 1'),
('Sample Item 2', 2, 102, 2000, 5, 20, FALSE, 'Sample description for item 2'),
('Sample Item 3', 1, 103, 1500, 7, 30, TRUE, 'Sample description for item 3'),
('Sample Item 4', 3, 101, 2500, 0, 10, FALSE, 'Sample description for item 4'),
('Sample Item 5', 2, 102, 3000, 2, 15, FALSE, 'Sample description for item 5');

/* 商品画像用 */
INSERT INTO IMAGES (item_id, name, image_url) VALUES
(1, 'image1_item1.jpg', ''),
(2, 'image1_item2.jpg', ''),
(3, 'image1_item3.jpg', ''),
(4, 'image1_item4.jpg', ''),
(5, 'image1_item5.jpg', '');

/* 店舗用 */
INSERT INTO SHOPS (name) VALUES
('Shop Alpha'),
('Shop Beta'),
('Shop Gamma'),
('Shop Delta'),
('Shop Epsilon');
