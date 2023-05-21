package jar.bytebite;

import com.github.britooo.looca.api.core.Looca;
import org.springframework.jdbc.core.JdbcTemplate;
import twilio.bytebite.TwilioSms;

/**
 *
 * @author ViniciusJesus
 */
public class Alerta {
    TwilioSms twilio = new TwilioSms();
    Conexao conexao = new Conexao();
    JdbcTemplate con = conexao.getConnection();    
    //Criticiade id 1= moderado 2= crítico
    
    public void alertaModerado(Integer fkLog, Integer descricao){
        try {
            con.update("insert into alerta values (?, ?, ?);", fkLog, 1, descricao );
            System.out.println("INSERIU ALERTAAAAAAAA");
        } catch (Exception e) {
            System.out.println("ERRO AO INSERIR ALERTA");
        }
    }
    
    public void alertaCritico(Integer fkLog, Integer descricao){
        try {
            con.update("insert into alerta values (?, ?, ?);", fkLog, 2, descricao );
            System.out.println("INSERIU ALERTAAAAAAAA");
        } catch (Exception e) {
            System.out.println("ERRO AO INSERIR ALERTA");
        }
//            twilio.enviaSms("Alerta critico na máquina xxx");
    }
    
}
