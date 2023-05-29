package jar.bytebite;

import org.springframework.jdbc.core.JdbcTemplate;
import twilio.bytebite.TwilioSms;

/**
 *
 * @author ViniciusJesus
 */
public class Alerta extends Conexao {

    TwilioSms twilio = new TwilioSms();
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConnection();
    //Criticiade id 1= moderado 2= crítico

    public Integer fkJanela(String data, String hora) {
        return con.queryForObject("select idJanelas from [dbo].[janelas] where data_ = ? and hora = ?;", Integer.class, data, hora);
    }

    public void alertaModerado(Integer fkLog, Integer descricao, String data, String hora) {
        try {
            con.update("insert into alerta values (?, ?, ?, ?);", fkLog, 1, descricao, fkJanela(data, hora));
            System.out.println("Alerta emitido.");
        } catch (Exception e) {
            System.out.println("Erro ao inserir alertas.");
        }
    }

    public void alertaCritico(Integer fkLog, Integer descricao, String data, String hora) {
        try {
            con.update("insert into alerta values (?, ?, ?, ?);", fkLog, 2, descricao, fkJanela(data, hora));
            System.out.println("Alerta emitido.");
        } catch (Exception e) {
            System.out.println("Erro ao inserir alertas.");
        }
//            twilio.enviaSms("Alerta critico na máquina xxx");
    }

}
