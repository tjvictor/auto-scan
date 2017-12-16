package autoscan.rest;

import autoscan.Biz.BarCodeBiz;
import autoscan.dao.InvoiceDao;
import autoscan.dao.StaffDao;
import autoscan.dao.TicketDao;
import autoscan.model.BarCode;
import autoscan.model.Invoice;
import autoscan.model.InvoicePackage;
import autoscan.model.PoTicket;
import autoscan.model.ResponseEntity;
import autoscan.model.Staff;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/invoiceService")
public class invoiceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${image.MappingPath}")
    private String imageMappingPath;

    @Value("${image.MappingUrl}")
    private String imageMappingUrl;

    @Autowired
    private InvoiceDao invoiceDaoImp;

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private StaffDao staffDaoImp;

    @Autowired
    private BarCodeBiz barCodeBiz;

    @RequestMapping(value = "/getInvoiceByCode", method = RequestMethod.GET)
    public ResponseEntity getInvoiceByCode(@RequestParam("code") String code, @RequestParam("number") String number) {

        try {
            Invoice item = invoiceDaoImp.getInvoiceByCode(code, number);
            if(StringUtils.isNotEmpty(item.getInvoiceCode()))
                return new ResponseEntity("ok", "SEARCH SUCCESSFULLY", item);
            else
                return new ResponseEntity("warn", "SEARCH FAILED", item);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getInvoiceListByStatus", method = RequestMethod.GET)
    public ResponseEntity getInvoiceListByStatus(@RequestParam("status") String status) {

        try {
            List<Invoice> items = invoiceDaoImp.getInvoiceListByStatus(status);
            return new ResponseEntity("ok", "SEARCH SUCCESSFULLY", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getInvoicePackage", method = RequestMethod.GET)
    public ResponseEntity getInvoicePackage() {

        try {
            List<InvoicePackage> items = invoiceDaoImp.getInvoicePackage();
            return new ResponseEntity("ok", "SEARCH SUCCESSFULLY", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getInvoiceByPackageId", method = RequestMethod.GET)
    public ResponseEntity getInvoiceByPackageId(@RequestParam("packageId") String packageId) {

        try {
            List<Invoice> items = invoiceDaoImp.getInvoiceByPackageId(packageId);
            return new ResponseEntity("ok", "SEARCH SUCCESSFULLY", items);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/packageVerifiedInvoice", method = RequestMethod.POST)
    public ResponseEntity packageVerifiedInvoice(@FormParam("invoiceList") String invoiceList) {

        try {
            invoiceDaoImp.packageVerifiedInvoice(invoiceList);
            return new ResponseEntity("ok", "PACKAGE SUCCESSFULLY, BUT EMAIL IS NOT SUPPORTED NOW.", "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/verifyPOTicket", method = RequestMethod.POST)
    public ResponseEntity verifyPOTicket(@FormParam("ticketId") String ticketId, @FormParam("invoiceList") String invoiceList) {

        try {

            ticketDao.verifyPOTicket(ticketId);
            invoiceDaoImp.verifyInvoices(invoiceList);
            return new ResponseEntity("ok", "VERIFY SUCCESSFULLY", "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity sendEmail(@FormParam("codeList") String codeList) {

        try {
            invoiceDaoImp.sendEmail(codeList);
            return new ResponseEntity("ok", "SEND SUCCESSFULLY", "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getBarCode", method = RequestMethod.GET)
    public ResponseEntity getBarCode(HttpServletRequest request) {

        try {
            String savePath = imageMappingPath;
            String saveUrl = request.getContextPath() + imageMappingUrl;

            String msg = barCodeBiz.generateCNSString();
            String fileName = String.format("%s.png", msg);
            barCodeBiz.generateBarCode(msg, savePath+fileName);
            BarCode bc = new BarCode();
            bc.setMessage(msg);
            bc.setImagePath(savePath+fileName);
            bc.setImageUrl(saveUrl+fileName);
            return new ResponseEntity("ok", "SEND SUCCESSFULLY", bc);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getBarCodeString", method = RequestMethod.GET)
    public ResponseEntity getBarCodeString() {

        try {

            String msg = barCodeBiz.generateCNSString();
            return new ResponseEntity("ok", "GENERATE BARCODE STRING SUCCESSFULLY", msg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/insertPoTicket", method = RequestMethod.POST)
    public ResponseEntity insertPoTicket(@FormParam("barCode") String barCode, @FormParam("vendorId") String vendorId,
                                         @FormParam("claimCurrency") String claimCurrency, @FormParam("claimAmount") String claimAmount,
                                         @FormParam("bu") String bu, @FormParam("poNumber") String poNumber,
                                         @FormParam("poReceivedAmount") String poReceivedAmount, @FormParam("comment") String comment,
                                         @FormParam("staffId") String staffId, @FormParam("telNumber") String telNumber) {

        try {
            PoTicket item = new PoTicket();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            item.setId(UUID.randomUUID().toString());
            item.setVendorId(vendorId);
            item.setClaimCurrency(claimCurrency);
            item.setClaimAmount(claimAmount);
            item.setBu(bu);
            item.setPoNumber(poNumber);
            item.setPoReceivedAmount(poReceivedAmount);
            item.setComment(comment);
            item.setStaffId(staffId);
            item.setTelNumber(telNumber);
            item.setSubmitDate(sdf.format(new Date()));
            item.setStatus("1");
            item.setBarCode(barCode);

            ticketDao.insertPoTicket(item);

            return new ResponseEntity("ok", "SUBMIT SUCCESSFULLY", null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity login(@RequestParam(value = "bankId") String bankId,
                                @RequestParam(value = "password") String password,
                                @RequestParam(value = "role") String role) {

        try {
            Staff item = staffDaoImp.login(bankId, password, role);
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(item.getBankId()))
                return new ResponseEntity("ok", "SEARCH SUCCESSFULLY", item);
            else
                return new ResponseEntity("error", "BANK ID DOES NOT EXIST OR PASSWORD IS NOT CORRECT!", "");
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }

    @RequestMapping(value = "/getPOTicketByBarCode", method = RequestMethod.GET)
    public ResponseEntity getPOTicketByBarCode(@RequestParam(value = "barCode") String barCode) {

        try {
            PoTicket item = ticketDao.getPOTicketByBarCode(barCode);
            String reminder = "THIS TICKET DOESN'T EXIST IN SYSTEM.";
            if(StringUtils.isNotEmpty(item.getId())){
                if("0".equals(item.getStatus())){
                    reminder = "THIS TICKET IS IN DRAFT STATUS.";
                } else if("1".equals(item.getStatus())){
                    reminder = "THIS TICKET IS IN PROGRESSING BY AO.";
                } else if("2".equals(item.getStatus())){
                    reminder = "THIS TICKET IS IN RELEASE PAYMENT STATUS.";
                } else if("3".equals(item.getStatus())){
                    reminder = "THIS TICKET IS COMPLETED.";
                }

                return new ResponseEntity("ok", reminder, item);
            }
            else
                return new ResponseEntity("error", reminder, "");
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity("error", "SYSTEM ERROR, PLEASE CONTACT ADMINISTRATOR");
        }
    }
}
