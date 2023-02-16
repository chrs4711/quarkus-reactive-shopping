insert into product(title,description,createdAt,updatedAt,price) values ('Product1','description',NOW(),NOW(),13.37);
insert into product(title,description,createdAt,updatedAt,price) values ('Product2','description',NOW(),NOW(),13.37);
insert into product(title,description,createdAt,updatedAt,price) values ('Product3','description',NOW(),NOW(),13.37);
insert into product(title,description,createdAt,updatedAt,price) values ('Product4','description',NOW(),NOW(),13.37);

insert into cart(name) values ('testcart');
insert into cart_item(total_price, quantity, product_id, cart_id) values (26.74, 2, 1, 1);
