package cn.lunadeer.miniplayertitle.managers;

import cn.lunadeer.miniplayertitle.utils.databse.DatabaseManager;
import cn.lunadeer.miniplayertitle.utils.databse.FIelds.*;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Alter.Alter;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Insert;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Select;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Show.Show;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Table.Column;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Table.Create;
import cn.lunadeer.miniplayertitle.utils.databse.syntax.Update;

import java.sql.Connection;

public class DatabaseTables {
    public static void migrate() throws Exception {

        // title table
        Column mplt_title_id = Column.of(new FieldInteger("id")).primary().serial().notNull().unique();
        Column mplt_title_title = Column.of(new FieldString("title")).notNull().defaultSqlVal("'unknown'");
        Column mplt_title_description = Column.of(new FieldString("description")).notNull().defaultSqlVal("'No description.'");
        Column mplt_title_enabled = Column.of(new FieldBoolean("enabled")).notNull().defaultSqlVal("true");
        Column mplt_title_created_at = Column.of(new FieldTimestamp("created_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Column mplt_title_updated_at = Column.of(new FieldTimestamp("updated_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Create.create().table("mplt_title")
                .column(mplt_title_id)
                .column(mplt_title_title)
                .column(mplt_title_description)
                .column(mplt_title_enabled)
                .column(mplt_title_created_at)
                .column(mplt_title_updated_at)
                .execute();


        // title shop table
        Column mplt_title_shop_id = Column.of(new FieldInteger("id")).primary().serial().notNull().unique();
        Column mplt_title_shop_title_id = Column.of(new FieldInteger("title_id")).notNull().foreign("mplt_title", new FieldInteger("id"));
        Column mplt_title_shop_price = Column.of(new FieldInteger("price")).notNull().defaultSqlVal("0");
        Column mplt_title_shop_days = Column.of(new FieldInteger("days")).notNull().defaultSqlVal("0");
        Column mplt_title_shop_amount = Column.of(new FieldInteger("amount")).notNull().defaultSqlVal("-1");
        Column mplt_title_shop_sale_end_at = Column.of(new FieldLong("sale_end_at")).notNull().defaultSqlVal("-1");
        Column mplt_title_shop_created_at = Column.of(new FieldTimestamp("created_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Column mplt_title_shop_updated_at = Column.of(new FieldTimestamp("updated_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Create.create().table("mplt_title_shop")
                .column(mplt_title_shop_id)
                .column(mplt_title_shop_title_id)
                .column(mplt_title_shop_price)
                .column(mplt_title_shop_days)
                .column(mplt_title_shop_amount)
                .column(mplt_title_shop_sale_end_at)
                .column(mplt_title_shop_created_at)
                .column(mplt_title_shop_updated_at)
                .execute();

        // player info table
        Column mplt_player_info_uuid = Column.of(new FieldString("uuid")).primary().notNull().unique();
        Column mplt_player_info_coin = Column.of(new FieldInteger("coin")).notNull().defaultSqlVal("0");
        Column mplt_player_info_using_title_id = Column.of(new FieldInteger("using_title_id")).notNull().defaultSqlVal("-1").foreign("mplt_title", new FieldInteger("id"));
        Column mplt_player_info_created_at = Column.of(new FieldTimestamp("created_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Column mplt_player_info_updated_at = Column.of(new FieldTimestamp("updated_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Create.create().table("mplt_player_info")
                .column(mplt_player_info_uuid)
                .column(mplt_player_info_coin)
                .column(mplt_player_info_using_title_id)
                .column(mplt_player_info_created_at)
                .column(mplt_player_info_updated_at)
                .execute();

        // player title table
        Column mplt_player_title_id = Column.of(new FieldInteger("id")).primary().serial().notNull().unique();
        Column mplt_player_title_player_uuid = Column.of(new FieldString("player_uuid")).notNull().foreign("mplt_player_info", new FieldString("uuid"));
        Column mplt_player_title_title_id = Column.of(new FieldInteger("title_id")).notNull().foreign("mplt_title", new FieldInteger("id"));
        Column mplt_player_title_expire_at = Column.of(new FieldLong("expire_at")).notNull().defaultSqlVal("-1");
        Column mplt_player_title_created_at = Column.of(new FieldTimestamp("created_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Column mplt_player_title_updated_at = Column.of(new FieldTimestamp("updated_at")).notNull().defaultSqlVal("'1970-01-01 00:00:00'");
        Create.create().table("mplt_player_title")
                .column(mplt_player_title_id)
                .column(mplt_player_title_player_uuid)
                .column(mplt_player_title_title_id)
                .column(mplt_player_title_expire_at)
                .column(mplt_player_title_created_at)
                .column(mplt_player_title_updated_at)
                .execute();

        if (Show.show().columns().from("mplt_title").execute().containsKey("created_at")) {
            Alter.alter().table("mplt_title").drop().column(new FieldTimestamp("created_at")).execute();
            Alter.alter().table("mplt_title").drop().column(new FieldTimestamp("updated_at")).execute();
            Alter.alter().table("mplt_player_info").drop().column(new FieldTimestamp("created_at")).execute();
            Alter.alter().table("mplt_player_info").drop().column(new FieldTimestamp("updated_at")).execute();
            Alter.alter().table("mplt_player_title").drop().column(new FieldTimestamp("created_at")).execute();
            Alter.alter().table("mplt_player_title").drop().column(new FieldTimestamp("updated_at")).execute();
        }

        if (Show.show().columns().from("mplt_title_shop").execute().containsKey("sale_end_at")) {
            Column mplt_title_shop_sale_end_at_y = Column.of(new FieldInteger("sale_end_at_y")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_title_shop").add().column(mplt_title_shop_sale_end_at_y).execute();
            Column mplt_title_shop_sale_end_at_m = Column.of(new FieldInteger("sale_end_at_m")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_title_shop").add().column(mplt_title_shop_sale_end_at_m).execute();
            Column mplt_title_shop_sale_end_at_d = Column.of(new FieldInteger("sale_end_at_d")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_title_shop").add().column(mplt_title_shop_sale_end_at_d).execute();

            try (Connection conn = DatabaseManager.instance.getConnection()) {
                String sql = "UPDATE mplt_title_shop SET " +
                        "sale_end_at_y = (sale_end_at / 10000), " +
                        "sale_end_at_m = (sale_end_at % 10000 / 100), " +
                        "sale_end_at_d = (sale_end_at % 100) " +
                        "WHERE sale_end_at != -1;";
                conn.createStatement().executeQuery(sql);
            } catch (Exception ignored) {
            }
            Alter.alter().table("mplt_title_shop").drop().column(new FieldLong("sale_end_at")).execute();
        }

        if (Show.show().columns().from("mplt_player_title").execute().containsKey("expire_at")) {
            Column mplt_player_title_expire_at_y = Column.of(new FieldInteger("expire_at_y")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_player_title").add().column(mplt_player_title_expire_at_y).execute();
            Column mplt_player_title_expire_at_m = Column.of(new FieldInteger("expire_at_m")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_player_title").add().column(mplt_player_title_expire_at_m).execute();
            Column mplt_player_title_expire_at_d = Column.of(new FieldInteger("expire_at_d")).notNull().defaultSqlVal("-1");
            Alter.alter().table("mplt_player_title").add().column(mplt_player_title_expire_at_d).execute();

            try (Connection conn = DatabaseManager.instance.getConnection()) {
                String sql = "UPDATE mplt_player_title SET " +
                        "expire_at_y = (expire_at / 10000), " +
                        "expire_at_m = (expire_at % 10000 / 100), " +
                        "expire_at_d = (expire_at % 100) " +
                        "WHERE expire_at != -1;";
                conn.createStatement().executeQuery(sql);
            } catch (Exception ignored) {
            }
            Alter.alter().table("mplt_player_title").drop().column(new FieldLong("expire_at")).execute();
        }

        // Setup default title
        FieldInteger mplt_default_title_id = new FieldInteger("id", -1);
        FieldString mplt_default_title_title = new FieldString("title", Configuration.defaultTitle.title);
        FieldString mplt_default_title_description = new FieldString("description", Configuration.defaultTitle.description);
        if (Select.select(new FieldInteger("id")).from("mplt_title").where("id = -1").execute().isEmpty()) {
            // Insert Default title
            Insert.insert().into("mplt_title").values(
                    mplt_default_title_id,
                    mplt_default_title_title,
                    mplt_default_title_description
            ).execute();
        } else {
            // Update default title if exists
            Update.update("mplt_title").set(
                    mplt_default_title_title,
                    mplt_default_title_description
            ).where("id = -1").execute();
        }

        // Adding last_use_name column to mplt_player_info table
        if (!Show.show().columns().from("mplt_player_info").execute().containsKey("last_use_name")) {
            Column mplt_player_info_last_use_name = Column.of(new FieldString("last_use_name")).notNull().defaultSqlVal("'unknown'");
            Alter.alter().table("mplt_player_info").add().column(mplt_player_info_last_use_name).execute();
        }


        // Convert coin and price columns to float
        if (Show.show().columns().from("mplt_player_info").execute().containsKey("coin")) {
            Column mplt_player_info_coin_d = Column.of(new FieldFloat("coin_d")).notNull().defaultSqlVal("0.0");
            Alter.alter().table("mplt_player_info").add().column(mplt_player_info_coin_d).execute();
            Column mplt_title_shop_price_d = Column.of(new FieldFloat("price_d")).notNull().defaultSqlVal("0.0");
            Alter.alter().table("mplt_title_shop").add().column(mplt_title_shop_price_d).execute();
            try (Connection conn = DatabaseManager.instance.getConnection()) {
                String sql =  "UPDATE mplt_player_info SET coin_d = coin;";
                conn.createStatement().executeQuery(sql);
                Alter.alter().table("mplt_player_info").drop().column(new FieldInteger("coin")).execute();
                sql =  "UPDATE mplt_title_shop SET price_d = price;";
                conn.createStatement().executeQuery(sql);
                Alter.alter().table("mplt_title_shop").drop().column(new FieldInteger("price")).execute();
            } catch (Exception ignored) {
            }
        }
    }
}
