package autoscan.rest;

import autoscan.dao.InvoiceDao;
import autoscan.model.Invoice;
import autoscan.model.ResponseEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.FormParam;
import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/invoiceService")
public class invoiceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InvoiceDao invoiceDaoImp;

    @RequestMapping(value = "/getInvoiceByCode", method = RequestMethod.GET)
    public ResponseEntity getInvoiceByCode(@RequestParam("code") String code) {

        try {
            Invoice item = invoiceDaoImp.getInvoiceByCode(code);
            if(StringUtils.isNotEmpty(item.getInvoiceCode()))
                return new ResponseEntity("ok", "查询成功", item);
            else
                return new ResponseEntity("warn", "未匹配成功", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity sendEmail(@FormParam("codeList") String codeList) {

        try {
            invoiceDaoImp.sendEmail(codeList);
            return new ResponseEntity("ok", "发送成功", "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "系统错误，请联系系统管理员");
        }
    }
}
