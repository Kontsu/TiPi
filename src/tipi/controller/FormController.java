package tipi.controller; 

/**
 * @author Lauri Soivi, Joona Viertola, Samuel Kontiomaa
 * @version 1.0
 * @since 18.12.2013
 * Controller creates new empty orderform, shows orderform and validates it, sends validated form to service
 */

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import tipi.bean.DateTimeCheck;
import tipi.bean.DateTimeCheckImpl;
import tipi.bean.OrderForm;
import tipi.bean.OrderFormImpl;
import tipi.bean.UserProfileImpl;
import tipi.service.FormSendService;
//import tipi.service.OrdersTimeCheckService;
import tipi.service.OrderFormValidationService;


@Controller
@RequestMapping(value = "/user") 
@SessionAttributes({"orderForm", "userProfile", "pageIdentifier"}) 
public class FormController { 
      
    boolean orderSuccessful=false; 
      
    @Inject
    private FormSendService formSendService;
      
    public FormSendService getFormSendService() { 
        return formSendService; 
    } 
  
    public void setFormSendService(FormSendService formSendService) { 
        this.formSendService = formSendService; 
    }
    
    @Inject
    private OrderFormValidationService orderFormValidationService;
    
    public OrderFormValidationService getOrderFormValidationService() {
    	return orderFormValidationService;
    }
    
    public void setOrderFormValidationService(OrderFormValidationService orderFormValidationService) {
    	this.orderFormValidationService = orderFormValidationService;
    }
    
    /**
     * Method creates new empty form
     */
    @RequestMapping(value = "orderFormEmpty", method = RequestMethod.GET) 
    public String getCreateForm(Model model) { 
        OrderForm orderForm = new OrderFormImpl(); 
        model.addAttribute("orderForm", orderForm); 
        model.addAttribute("pageIdentifier", "orderForm"); 
        return "redirect:/user/orderForm"; 
    } 
  
    /**
     * Method validates orderForm and sends it to orderConfirmation.jsp
     */
    @RequestMapping(value = "orderForm", method = RequestMethod.POST) 
    public String create(Model model, @ModelAttribute(value = "orderForm") @Valid OrderFormImpl orderForm, BindingResult result) { 
    	
    	DateTimeCheck checkValid=new DateTimeCheckImpl();
    	
    	checkValid = orderFormValidationService.checkDateAndTimeCorrectness(orderForm);
    	System.out.println("True/False: "+checkValid.isEverythingOk());
        model.addAttribute("pageIdentifier", "orderForm");
        
        if (result.hasErrors()||!checkValid.isEverythingOk()) {
            model.addAttribute("isItValid", checkValid);
            return "/user/orderForm"; 
        } else { 
            return "/user/orderConfirmation"; 
        } 
    }
  
    /**
     * Method shows orderform. If orderform object doesn't exist. Create new empty form.
     */
    @RequestMapping(value = "orderForm", method = RequestMethod.GET) 
    public String getOldForm(Model model) { 
  
        if (!model.containsAttribute("orderForm")) { 
            return "redirect:/user/orderFormEmpty"; 
        }

        if(orderSuccessful){ 
            model.addAttribute("orderSuccessful", "true"); 
        } 
        orderSuccessful=false; 
        
    	DateTimeCheck checkValid=new DateTimeCheckImpl();
        model.addAttribute("isItValid", checkValid);
        model.addAttribute("pageIdentifier", "orderForm"); 
        return "/user/orderForm"; 
    } 
      
    /**
     * Method sends orderform to service.
     */
    @RequestMapping(value = "orderSend", method = RequestMethod.POST) 
    public String sendOrderForm(Model model, @ModelAttribute(value = "orderForm") @Valid OrderFormImpl orderForm, 
            BindingResult result, @ModelAttribute(value = "userProfile") UserProfileImpl userProfile) { 
  
  		formSendService.sendFormToDAO(orderForm, userProfile.getUser_id(), userProfile.getMyCompany()); 
        orderSuccessful=true;      
        model.addAttribute("pageIdentifier", "orderForm");
        
        return "redirect:/user/orderFormEmpty"; 
    } 
      
}