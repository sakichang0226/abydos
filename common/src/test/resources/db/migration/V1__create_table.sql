CREATE TABLE ITEMS (
   item_id       INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
   name          VARCHAR(100) NOT NULL,
   shop_id       INT NOT NULL,
   category_id   INT NOT NULL,
   price         INT NOT NULL,
   purchase_num  INT DEFAULT 0 NOT NULL,
   stock         INT DEFAULT 0 NOT NULL,
   is_stopped    BOOLEAN DEFAULT FALSE,
   description   CLOB NOT NULL,
   create_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   update_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IMAGES (
   image_id     INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
   item_id      INT NOT NULL,
   name         VARCHAR(100) NOT NULL,
   image_url    CLOB NOT NULL,
   create_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   update_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (item_id) REFERENCES ITEMS (item_id)
);
