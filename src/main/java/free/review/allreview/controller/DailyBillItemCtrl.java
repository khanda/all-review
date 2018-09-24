package free.review.allreview.controller;

import free.review.allreview.entity.DailyBillItem;
import free.review.allreview.service.DailyBillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/")
public class DailyBillItemCtrl {
    @Autowired
    private DailyBillItemService dailyBillItemService;

    // List All
    @RequestMapping(value = "dailyBillItem", method = RequestMethod.GET)
    public ResponseEntity<Iterable<DailyBillItem>> getAllContacts() {
        return dailyBillItemService.getAllResponse();
    }

    // List One
    @RequestMapping(value = "dailyBillItem/{id}", method = RequestMethod.GET)
    public ResponseEntity<DailyBillItem> getSingleContact(@PathVariable Long id) {
        return dailyBillItemService.getOneResponse(id);
    }

    // Create New
    @RequestMapping(value = "dailyBillItem", method = RequestMethod.POST)
    public ResponseEntity<DailyBillItem> createNewContact(@RequestBody DailyBillItem contact, HttpServletRequest req) {
        return dailyBillItemService.createNew(contact, req);
    }

    // Update with PATCH
    @RequestMapping(value = "dailyBillItem/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<DailyBillItem> putUpdateContact(@PathVariable Long id, @RequestBody DailyBillItem contact) {
        return dailyBillItemService.patchUpdate(id, contact);
    }

    // Delete 
    @RequestMapping(value = "dailyBillItem/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DailyBillItem> deleteContact(@PathVariable Long id) {
        return dailyBillItemService.delete(id);
    }
}
