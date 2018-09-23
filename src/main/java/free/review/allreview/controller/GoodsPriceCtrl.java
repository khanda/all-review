package free.review.allreview.controller;

import free.review.allreview.entity.GoodsPrice;
import free.review.allreview.service.GoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/")
public class GoodsPriceCtrl {
    @Autowired
    private GoodsPriceService goodsPriceService;

    // List All
    @RequestMapping(value = "goodsPrices", method = RequestMethod.GET)
    public ResponseEntity<Iterable<GoodsPrice>> getAllContacts() {
        return goodsPriceService.getAllResponse();
    }

    // List One
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.GET)
    public ResponseEntity<GoodsPrice> getSingleContact(@PathVariable Long id) {
        return goodsPriceService.getOneResponse(id);
    }

    // Create New
    @RequestMapping(value = "goodsPrices", method = RequestMethod.POST)
    public ResponseEntity<GoodsPrice> createNewContact(@RequestBody GoodsPrice contact, HttpServletRequest req) {
        return goodsPriceService.createNew(contact, req);
    }

    // Update with PUT
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GoodsPrice> putUpdateContact(@PathVariable Long id, @RequestBody GoodsPrice contact) {
        return goodsPriceService.putUpdate(id, contact);
    }

    // Delete 
    @RequestMapping(value = "goodsPrices/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GoodsPrice> deleteContact(@PathVariable Long id) {
        return goodsPriceService.delete(id);
    }
}
