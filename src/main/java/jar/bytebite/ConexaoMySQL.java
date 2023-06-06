/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jar.bytebite;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author ViniciusJesus
 */
public class ConexaoMySQL {

    private JdbcTemplate connectionMySQL;

    public ConexaoMySQL() {

        BasicDataSource dataSourceMysql = new BasicDataSource();

        dataSourceMysql.setDriverClassName("com.mysql.cj.jdbc.Driver");

        dataSourceMysql.setUrl("jdbc:mysql://localhost:3306/ByteBite");

        dataSourceMysql.setUsername("root");

        dataSourceMysql.setPassword("urubu100");

        this.connectionMySQL = new JdbcTemplate(dataSourceMysql);
    }

    public JdbcTemplate getConnectionMySQL() {
        return connectionMySQL;
    }
}
