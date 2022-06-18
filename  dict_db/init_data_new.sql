INSERT INTO public.naklei_d_class (id, name, code) VALUES ('76ae3aec-faff-a08d-a45e-7b26a9097322', 'B', 'B');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('530065b8-ea8b-34eb-4ff6-089cf6e0bac7', 'C', 'C');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('f8b19dbc-e50f-71b6-87ea-7739d59089d3', 'D', 'D');
INSERT INTO public.naklei_d_class (id, name, code) VALUES ('60b3c696-7904-9032-6ba6-821ac8faeac5', 'A', 'A');

INSERT INTO public.naklei_d_mark (id, name, code) VALUES ('3c60d299-e2f8-9cd1-d037-04bdd3411f07', 'Rio', 'KIA_RIO');

INSERT INTO public.naklei_d_model (id, name, mark_id, code) VALUES ('ba7103cc-5ca1-d33d-46fc-7e7702576114', 'Kia', '3c60d299-e2f8-9cd1-d037-04bdd3411f07', 'KIA_RIO');

INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('cdf2de7c-352f-96e7-6dd3-2d5223237155', '40%', 'MEDIUM');
INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '100%', 'LARGE');
INSERT INTO public.naklei_d_sticker_type (id, name, code) VALUES ('1fbfe461-da03-440a-f1d1-d17d92824623', '20%', 'SMALL');

INSERT INTO public.naklei_d_zone (id, points, name, code, custom) VALUES ('f69faa89-5f1e-abf8-0ea6-fa922efaa12d', 'test', 'Золотой квадрат', 'GOLD_ZONE', false);
INSERT INTO public.naklei_d_zone (id, points, name, code, custom) VALUES ('67fdb75b-cdd2-75f3-ba96-0f4408808c56', 'test', 'город', 'SILVER_ZONE', false);
INSERT INTO public.naklei_d_zone (id, points, name, code, custom) VALUES ('baed831d-731e-d8e7-07c3-113fdd8659b4', 'test', 'область', 'BRONZE_ZONE', false);

INSERT INTO public.naklei_d_car_classification (id, classs_id, mark_id, year_) VALUES ('d87ba5a2-1101-e978-f5b8-1899240e5b50', '60b3c696-7904-9032-6ba6-821ac8faeac5', 'ba7103cc-5ca1-d33d-46fc-7e7702576114', 2004);

INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('1ebb9c7c-f037-318f-9926-f576ccaee925', '1fbfe461-da03-440a-f1d1-d17d92824623', 'f69faa89-5f1e-abf8-0ea6-fa922efaa12d', 18000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('d6815386-e319-6977-aad3-01a8eb491a81', '1fbfe461-da03-440a-f1d1-d17d92824623', '67fdb75b-cdd2-75f3-ba96-0f4408808c56', 15000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('710376f1-c8aa-2230-c3cf-6cb032e3d6b8', '1fbfe461-da03-440a-f1d1-d17d92824623', 'baed831d-731e-d8e7-07c3-113fdd8659b4', 10000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('9d3abbd9-6a7f-83f5-5fb3-f36d0d73bb65', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', 'f69faa89-5f1e-abf8-0ea6-fa922efaa12d', 20000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('834ba5d4-bb9b-8583-0dbc-08378dda6111', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', '67fdb75b-cdd2-75f3-ba96-0f4408808c56', 16000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('0d4f903a-7f1c-ec26-fcdf-1622791838d5', 'cdf2de7c-352f-96e7-6dd3-2d5223237155', 'baed831d-731e-d8e7-07c3-113fdd8659b4', 12000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('7911680d-6344-e47c-226e-1033eb7b6a97', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', 'f69faa89-5f1e-abf8-0ea6-fa922efaa12d', 25000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('1ae8f1ef-7270-849c-413b-c6456a1af282', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', '67fdb75b-cdd2-75f3-ba96-0f4408808c56', 20000.00);
INSERT INTO public.naklei_d_coverage (id, sticker_type_id, zone_id, value_) VALUES ('38daf331-014d-89ee-c545-f75dd7003f82', 'b1f95bae-5c5f-8ef1-22da-c391dcec89b3', 'baed831d-731e-d8e7-07c3-113fdd8659b4', 15000.00);


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