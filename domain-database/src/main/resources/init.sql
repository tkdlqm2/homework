/* delete-all.sql */
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS comic_genres;
DROP TABLE IF EXISTS comic_view_logs;
DROP TABLE IF EXISTS comic_view_stats;
DROP TABLE IF EXISTS purchase;
DROP TABLE IF EXISTS purchase_stats;
DROP TABLE IF EXISTS user_subscribe_comics;
DROP TABLE IF EXISTS comic;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

