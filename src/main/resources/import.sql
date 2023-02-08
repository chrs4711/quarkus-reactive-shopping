insert into product(title,description,createdAt,updatedAt) values ('Product1','description',NOW(),NOW());
insert into product(title,description,createdAt,updatedAt) values ('Product2','description',NOW(),NOW());
insert into product(title,description,createdAt,updatedAt) values ('Product3','description',NOW(),NOW());
insert into product(title,description,createdAt,updatedAt) values ('Product4','description',NOW(),NOW());

insert into cart(name) values ('testcart');
insert into cart_item(total_price, quantity, product_id, cart_id) values (12, 2, 1, 1);
