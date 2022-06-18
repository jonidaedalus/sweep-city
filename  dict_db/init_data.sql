INSERT INTO public.naklei_d_class (id, name, code) VALUES ('76ae3aec-faff-a08d-a45e-7b26a9097322', 'B', 'B');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('530065b8-ea8b-34eb-4ff6-089cf6e0bac7', 'C', 'C');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('f8b19dbc-e50f-71b6-87ea-7739d59089d3', 'D', 'D');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('60b3c696-7904-9032-6ba6-821ac8faeac5', 'A', 'A');

INSERT INTO public.naklei_d_mark (id, name, code) VALUES ('3c60d299-e2f8-9cd1-d037-04bdd3411f07', 'Rio', 'KIA_RIO');

INSERT INTO public.naklei_d_model (id, name, mark_id, code) VALUES ('ba7103cc-5ca1-d33d-46fc-7e7702576114', 'Kia', '3c60d299-e2f8-9cd1-d037-04bdd3411f07', 'KIA_RIO');

INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('cdf2de7c-352f-96e7-6dd3-2d5223237155', '40%', 'MEDIUM');
INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '100%', 'LARGE');
INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('1fbfe461-da03-440a-f1d1-d17d92824623', '20%', 'SMALL');

INSERT INTO public.naklei_d_car_classification (id, classs_id, mark_id, year_) VALUES ('d87ba5a2-1101-e978-f5b8-1899240e5b50', '60b3c696-7904-9032-6ba6-821ac8faeac5', 'ba7103cc-5ca1-d33d-46fc-7e7702576114', 2004);

INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('00cbc4b3-be96-19b9-3bbd-b32b52c3239e', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', '60b3c696-7904-9032-6ba6-821ac8faeac5', 15.00, 35.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('66e26cfe-16a1-ca77-0d2f-8c6cc85916ed', '1fbfe461-da03-440a-f1d1-d17d92824623', '60b3c696-7904-9032-6ba6-821ac8faeac5', 15.00, 30.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('21b00b32-91b1-f5ab-6643-40ff37740f04', '1fbfe461-da03-440a-f1d1-d17d92824623', '76ae3aec-faff-a08d-a45e-7b26a9097322', 15.00, 35.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('e180c690-bf7d-d7b2-1521-6e1eb3ca343f', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', '76ae3aec-faff-a08d-a45e-7b26a9097322', 15.00, 40.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('b63693c1-4f28-6675-6cc9-0add4ea59c66', '1fbfe461-da03-440a-f1d1-d17d92824623', '530065b8-ea8b-34eb-4ff6-089cf6e0bac7', 15.00, 40.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('a06eb288-10b7-45dd-7990-edd0ade0093a', '1fbfe461-da03-440a-f1d1-d17d92824623', 'f8b19dbc-e50f-71b6-87ea-7739d59089d3', 15.00, 40.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('dc0be9e2-d08a-78c7-1ca8-24766ea11a42', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', '530065b8-ea8b-34eb-4ff6-089cf6e0bac7', 15.00, 45.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('bbc9a8c9-6343-ddf6-f572-d6aa7d53d196', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', 'f8b19dbc-e50f-71b6-87ea-7739d59089d3', 15.00, 45.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('b65a7b74-d940-1e18-092f-26f18dc19275', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '60b3c696-7904-9032-6ba6-821ac8faeac5', 15.00, 45.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('1be6f53d-6eb1-4e2b-f3e7-8ba296eef366', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '76ae3aec-faff-a08d-a45e-7b26a9097322', 15.00, 50.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('64d68052-cd33-b730-24a5-fb513019b1d4', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '530065b8-ea8b-34eb-4ff6-089cf6e0bac7', 15.00, 55.00);
INSERT INTO public.naklei_d_point_cost (id, sticker_type_id, car_class_id, driver_cost, advertiser_cost) VALUES ('9f5fe2cf-87a5-ed45-7711-d9e7d2ed1299', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', 'f8b19dbc-e50f-71b6-87ea-7739d59089d3', 15.00, 55.00);

INSERT INTO public.naklei_d_color (id, name, code) VALUES ('9b62dd72-1beb-3352-0298-a767dab822b8', 'Белый', 'WHITE');

INSERT INTO public.naklei_d_city (id, version, create_ts, created_by, update_ts, updated_by, delete_ts, deleted_by, name, code, xpoint, ypoint, zoom_level) VALUES ('9ec77590-079d-4b6c-6b2d-49988e70a858', 1, '2021-08-08 18:45:37.958000', 'admin', '2021-08-08 18:45:37.958000', null, null, null, 'Алматы', 'ALA', 76.851, 43.222, 12);
INSERT INTO public.naklei_d_city (id, version, create_ts, created_by, update_ts, updated_by, delete_ts, deleted_by, name, code, xpoint, ypoint, zoom_level) VALUES ('19e945de-4785-9cca-94b7-584db31932bf', 1, '2021-08-08 18:46:06.614000', 'admin', '2021-08-08 18:46:06.614000', null, null, null, 'Нур-Султан', 'TSE', 71.47, 51.16, 12);

INSERT INTO public.naklei_d_zone (id, name, polygon, code, custom, city_id, is_city) VALUES ('23e4c2d7-04b7-f102-2217-e48f8b9285f2', 'Город', 'POLYGON ((76.987209 43.283829, 76.986008 43.263456, 76.985664 43.255955, 76.982918 43.245203, 76.977081 43.237199, 76.976395 43.223942, 76.983604 43.207929, 76.992188 43.193413, 76.958885 43.204425, 76.922493 43.198919, 76.91185 43.197042, 76.901722 43.191035, 76.893482 43.177016, 76.831512 43.184652, 76.791 43.236699, 76.928329 43.276705, 76.987209 43.283829))', 'ALA', false, '9ec77590-079d-4b6c-6b2d-49988e70a858', true);
INSERT INTO public.naklei_d_zone (id, name, polygon, code, custom, city_id, is_city) VALUES ('234e8eb1-5b9d-11e1-1306-d01981290541', 'Иле', 'POLYGON ((76.966438 43.222441, 76.963692 43.236949, 76.924896 43.234448, 76.928673 43.219438, 76.941719 43.22119, 76.966438 43.222441))', 'ILE', false, '9ec77590-079d-4b6c-6b2d-49988e70a858', false);
